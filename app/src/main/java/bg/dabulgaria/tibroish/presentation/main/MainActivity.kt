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
import bg.dabulgaria.tibroish.presentation.base.BaseActivity
import bg.dabulgaria.tibroish.presentation.navigation.NavItemAction
import bg.dabulgaria.tibroish.presentation.navigation.NavigationDrawerFragment
import bg.dabulgaria.tibroish.presentation.navigation.OnMenuClickListener
import bg.dabulgaria.tibroish.presentation.providers.IResourceProvider
import bg.dabulgaria.tibroish.presentation.ui.home.HomeFragment
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

interface IMainScreenView{

    fun showScreen(content : Fragment,
                   contentTag : String,
                   addToBackStack : Boolean,
                   transitionContent : Boolean)

    fun setRequestedOrientation( orientation:Int)

    val supportFragmentMngr: FragmentManager?

    val appCompatActivity: AppCompatActivity
}

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

    private var drawerLayout :DrawerLayout? = null
    private var navigationDrawerFragment :NavigationDrawerFragment ? = null

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration :AppBarConfiguration
    private var mContentFragment: Fragment? = null

    //region AppCompatActivity overrides
    public override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigationDrawerFragment = supportFragmentManager.findFragmentById(R.id.navigation_drawer) as NavigationDrawerFragment?

        drawerLayout = findViewById(R.id.drawerLayout)
        // Set up the drawer.
        navigationDrawerFragment?.setUp(R.id.navigation_drawer, drawerLayout, this)

        drawerLayout?.closeDrawers()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState)
        // Restore state members from saved instance
        mContentFragment = supportFragmentManager.getFragment(savedInstanceState!!, MainActivity.KEY_LAST_FRAGMENT)
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {

        if (mContentFragment != null && mContentFragment?.isAdded() == true) {
            supportFragmentManager.putFragment(savedInstanceState,
                    KEY_LAST_FRAGMENT, mContentFragment!!)
        }
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        // make sure we always have some default
        if (mContentFragment == null)
            mContentFragment = HomeFragment.newInstance()

        // no backstack here
        supportFragmentManager.beginTransaction()
                .replace(R.id.container, mContentFragment!!)
                .commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (navigationDrawerFragment?.isDrawerOpen == false) {
            menuInflater.inflate(R.menu.main, menu)
            restoreActionBar()
            return true
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onSupportNavigateUp(): Boolean {

        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onStart() {

        super.onStart()
        mainNavigator.showHomeScreen()
    }
    //region AppCompatActivity overrides

    //region OnMenuClickListener implementation
    override fun onItemClicked(action: NavItemAction) {

        drawerLayout?.closeDrawers()
        mainNavigator.onItemClicked(action)
    }
    //endregion OnMenuClickListener implementation

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

    override val appCompatActivity: AppCompatActivity
        get() = this

    override val supportFragmentMngr: FragmentManager?
      = this.supportFragmentManager

    companion object{

        val KEY_LAST_FRAGMENT = "MainActivity.LastFragment"
    }
}
