package bg.dabulgaria.tibroish.presentation.main


import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import bg.dabulgaria.tibroish.R
import bg.dabulgaria.tibroish.domain.providers.ILogger
import bg.dabulgaria.tibroish.infrastructure.permission.IPermissionResponseHandler
import bg.dabulgaria.tibroish.presentation.base.BaseActivity
import bg.dabulgaria.tibroish.presentation.event.CameraPhotoTakenEvent
import bg.dabulgaria.tibroish.presentation.navigation.NavigationDrawerFragment
import bg.dabulgaria.tibroish.presentation.providers.IResourceProvider
import bg.dabulgaria.tibroish.presentation.push.IPushActionRouter
import bg.dabulgaria.tibroish.presentation.ui.common.item.send.SendItemInteractor
import bg.dabulgaria.tibroish.presentation.ui.photopicker.gallery.PhotoPickerConstants
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
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
    protected lateinit var pushActionRouter: IPushActionRouter
    @Inject
    protected lateinit var logger: ILogger

    private var drawerLayout :DrawerLayout? = null
    private var navigationDrawerFragment :NavigationDrawerFragment ? = null

    private lateinit var navController: NavController

    private var isStarted = false
    private var lastIntent :Intent? = null

    //region AppCompatActivity overrides
    public override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigationDrawerFragment = supportFragmentManager.findFragmentById(R.id.navigation_drawer) as NavigationDrawerFragment?

        drawerLayout = findViewById(R.id.drawerLayout)
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
        mainActivityProcessOverlay.visibility = visibility
        mainActivityProgressBar.visibility = visibility
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
}
