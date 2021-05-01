package bg.dabulgaria.tibroish.presentation.main


import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.ui.AppBarConfiguration
import bg.dabulgaria.tibroish.R
import bg.dabulgaria.tibroish.infrastructure.permission.IPermissionResponseHandler
import bg.dabulgaria.tibroish.presentation.base.BaseActivity
import bg.dabulgaria.tibroish.presentation.navigation.NavItemAction
import bg.dabulgaria.tibroish.presentation.navigation.NavigationDrawerFragment
import bg.dabulgaria.tibroish.presentation.navigation.OnMenuClickListener
import bg.dabulgaria.tibroish.presentation.providers.IResourceProvider
import bg.dabulgaria.tibroish.presentation.ui.home.HomeFragment
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class MainActivity : BaseActivity(),
        IMainScreenView,
        HasAndroidInjector,
        OnMenuClickListener {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>
    @Inject
    lateinit var resourceProvider: IResourceProvider
    @Inject
    lateinit var mainNavigator: IMainNavigator
    @Inject
    lateinit var permissionsResponseHandler: IPermissionResponseHandler

    private lateinit var auth: FirebaseAuth

    private var drawerLayout :DrawerLayout? = null
    private var navigationDrawerFragment :NavigationDrawerFragment ? = null

    private lateinit var navController: NavController

    //region AppCompatActivity overrides
    public override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        navigationDrawerFragment = supportFragmentManager.findFragmentById(R.id.navigation_drawer) as NavigationDrawerFragment?

        drawerLayout = findViewById(R.id.drawerLayout)
        // Set up the drawer.
        navigationDrawerFragment?.setUp(R.id.navigation_drawer, drawerLayout, this)

        drawerLayout?.closeDrawers()

        mainNavigator.setView(this)

        onAuthEvent(coldStart = savedInstanceState == null )
    }

    override fun onDestroy() {
        mainNavigator.setView(null)
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
    //region AppCompatActivity overrides

    //region OnMenuClickListener implementation
    override fun onNavigateToItem(action: NavItemAction) {

        drawerLayout?.closeDrawers()
        mainNavigator.onNavigateToItem(action)
    }
    //endregion OnMenuClickListener implementation

    //region IMainView implementation
    override fun onAuthEvent(coldStart:Boolean) {

        val user = auth.currentUser
        if( user == null ){

            mainNavigator.showLoginScreen()
            drawerLayout?.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            navigationDrawerFragment?.setEnabled( false )
        }
        else if(coldStart) {

            mainNavigator.showHomeScreen()
            drawerLayout?.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            navigationDrawerFragment?.setEnabled( true )
        }
    }
    //endregion IMainView implementation

    private fun restoreActionBar() {
        val actionBar = supportActionBar
        actionBar!!.navigationMode = ActionBar.NAVIGATION_MODE_STANDARD
        actionBar.setDisplayShowTitleEnabled(true)
    }

    fun openDrawer() {
        drawerLayout?.openDrawer(GravityCompat.START)
    }

    fun closeDrawer() {
        drawerLayout?.closeDrawers()
    }

    @Override
    override fun onOptionsItemSelected(item: MenuItem):Boolean {
        when (item.getItemId()) {
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


    companion object{

        val KEY_LAST_FRAGMENT = "MainActivity.LastFragment"
    }
}
