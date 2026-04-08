package bg.dabulgaria.tibroish.presentation.main


import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import bg.dabulgaria.tibroish.R
import bg.dabulgaria.tibroish.domain.providers.ILogger
import bg.dabulgaria.tibroish.infrastructure.permission.IPermissionResponseHandler
import bg.dabulgaria.tibroish.presentation.base.BaseActivity
import bg.dabulgaria.tibroish.presentation.navigation.NavigationDrawerFragment
import bg.dabulgaria.tibroish.presentation.providers.IResourceProvider
import bg.dabulgaria.tibroish.presentation.push.IPushActionRouter
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import bg.dabulgaria.tibroish.databinding.ActivityMainBinding
import javax.inject.Inject

class MainActivity : BaseActivity(),
        IMainScreenView,
        HasAndroidInjector{

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>
    @Inject
    lateinit var resourceProvider: IResourceProvider
    @Inject
    lateinit var mainPresenter: IMainPresenter
    @Inject
    lateinit var permissionsResponseHandler: IPermissionResponseHandler
    @Inject
    lateinit var pushActionRouter: IPushActionRouter
    @Inject
    lateinit var logger: ILogger

    private var drawerLayout :DrawerLayout? = null
    private var navigationDrawerFragment :NavigationDrawerFragment ? = null

    private lateinit var navController: NavController

    private var isStarted = false
    private var lastIntent :Intent? = null
    private lateinit var binding: ActivityMainBinding

    //region AppCompatActivity overrides
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        configureEdgeToEdgeBars()

        navigationDrawerFragment = supportFragmentManager.findFragmentById(R.id.navigation_drawer) as NavigationDrawerFragment?

        drawerLayout = binding.drawerLayout
        // Set up the drawer.
        navigationDrawerFragment?.setUp(R.id.navigation_drawer, drawerLayout, mainPresenter)

        drawerLayout?.closeDrawers()

        mainPresenter.view = this

        mainPresenter.onAuthEvent(coldStart = savedInstanceState == null )

        if(intent != null && lastIntent == null)
            lastIntent = intent
    }

    override fun onDestroy() {
        mainPresenter.view = null
        navigationDrawerFragment = null
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (navigationDrawerFragment?.isDrawerOpen == false) {
            menuInflater.inflate(R.menu.main, menu)
            restoreActionBar()
            return true
        }
        return super.onCreateOptionsMenu(menu)
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        permissionsResponseHandler.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onSupportNavigateUp(): Boolean {

        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        logger.i(TAG, "onActivityResult(requestCode: $requestCode, resultCode: $resultCode)" )
    }

    override fun onNewIntent(newIntent: Intent?) {

        super.onNewIntent(newIntent)
        if(newIntent != null && lastIntent == null)
            lastIntent = newIntent
    }

    override fun onStart() {

        super.onStart()
        isStarted = true
    }

    override fun onResume() {
        super.onResume()
        lastIntent?.let { pushActionRouter.onIntent(it) }
        lastIntent = null
    }

    override fun onStop() {

        mainPresenter.dispose()
        isStarted = false
        super.onStop()
    }
    //region AppCompatActivity overrides

    override fun showNavigation(show: Boolean) {

        drawerLayout?.setDrawerLockMode( if(show)
                DrawerLayout.LOCK_MODE_UNLOCKED
            else
                DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        navigationDrawerFragment?.setEnabled( show )
    }
    //endregion IMainView implementation

    private fun restoreActionBar() {
        val actionBar = supportActionBar?: return
        actionBar.navigationMode = ActionBar.NAVIGATION_MODE_STANDARD
        actionBar.setDisplayShowTitleEnabled(true)
        if (shouldShowEdgeToEdgeTopBar()) {
            actionBar.elevation = 0f
        }
    }

    private fun openDrawer() {
        drawerLayout?.openDrawer(GravityCompat.START)
    }

    override fun closeDrawer() {
        drawerLayout?.closeDrawers()
    }

    @Override
    override fun onOptionsItemSelected(item: MenuItem):Boolean {
        when (item.itemId) {
             android.R.id.home->{
                 if(drawerLayout?.isOpen() == true)
                     closeDrawer()
                 else
                     openDrawer()
                 return true
             }
        }
        return super.onOptionsItemSelected(item);
    }

    override fun androidInjector(): AndroidInjector<Any> {

        return dispatchingAndroidInjector
    }

    override fun showProcessing(processing: Boolean) {

        val visibility = if(processing) View.VISIBLE else View.GONE
        binding.mainActivityProcessOverlay.visibility = visibility
        binding.mainActivityProgressBar.visibility = visibility
    }

    override fun showDismissableDialog(message: String, dismissCallback: () -> Unit) {

        AlertDialog.Builder(this)
                .setMessage(message)
                .setOnCancelListener { dismissCallback.invoke() }
                .setPositiveButton(android.R.string.ok) { _, _ -> dismissCallback.invoke() }
                .show()
    }

    override fun onAuthEvent() {

        navigationDrawerFragment = supportFragmentManager.findFragmentById(R.id.navigation_drawer) as NavigationDrawerFragment?

        navigationDrawerFragment?.reloadNavigationItems()
    }

    companion object{
        val TAG = MainActivity::class.java.simpleName
    }

    private fun configureEdgeToEdgeBars() {
        if (!shouldShowEdgeToEdgeBars()) {
            binding.edgeToEdgeTopBar.visibility = View.GONE
            binding.edgeToEdgeBottomBar.visibility = View.GONE
            return
        }

        binding.edgeToEdgeTopBar.visibility = View.VISIBLE
        binding.edgeToEdgeBottomBar.visibility = View.VISIBLE
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = Color.TRANSPARENT
        WindowInsetsControllerCompat(window, binding.root).apply {
            isAppearanceLightStatusBars = false
            isAppearanceLightNavigationBars = false
        }
        supportActionBar?.elevation = 0f
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            updateSpacerHeight(binding.edgeToEdgeTopBar, systemBarsInsets.top)
            updateSpacerHeight(binding.edgeToEdgeBottomBar, systemBarsInsets.bottom)
            insets
        }
        ViewCompat.requestApplyInsets(binding.root)
    }

    private fun shouldShowEdgeToEdgeTopBar(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM
    }

    private fun shouldShowEdgeToEdgeBars(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM
    }

    private fun updateSpacerHeight(view: View, height: Int) {
        val layoutParams = view.layoutParams
        if (layoutParams.height != height) {
            layoutParams.height = height
            view.layoutParams = layoutParams
        }
    }
}
