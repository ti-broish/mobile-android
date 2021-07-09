package bg.dabulgaria.tibroish.presentation.navigation

import android.content.res.Configuration
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bg.dabulgaria.tibroish.R
import kotlinx.android.synthetic.main.fragment_navigation_drawer.*
import kotlinx.android.synthetic.main.fragment_navigation_drawer.view.*

/**
 * Fragment used for managing interactions for and presentation of a navigation drawer.
 * See the [
 * design guidelines](https://developer.android.com/design/patterns/navigation-drawer.html#Interaction) for a complete explanation of the behaviors implemented here.
 */
class NavigationDrawerFragment : Fragment() {
    /**
     * Helper component that ties the action bar to the navigation drawer.
     */
    private var drawerToggle: ActionBarDrawerToggle? = null
    private var drawerLayout: DrawerLayout? = null
    private var listener: OnMenuClickListener? = null
    private var fragmentContainerView: View? = null

    private var currentSelectedPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        currentSelectedPosition = savedInstanceState?.getInt(STATE_SELECTED_POSITION)?:0
        selectItem(currentSelectedPosition)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(
                R.layout.fragment_navigation_drawer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val navItems = listOf(NavItem( NavItemAction.Home, R.string.start, null ),
                NavItem( NavItemAction.SendProtocol, R.string.send_protocol, null ),
                NavItem( NavItemAction.SendSignal, R.string.send_signal, null ),
                NavItem( NavItemAction.YouCountLive, R.string.ti_broish, R.string.live ),
                NavItem( NavItemAction.CheckIn, R.string.check_in, null ),
                NavItem( NavItemAction.MyProtocols, R.string.my_protocols, null ),
                NavItem( NavItemAction.MySignals, R.string.my_signals, null ),
                NavItem( NavItemAction.RightsAndObligations, R.string.rights_and_obligations, null ),
                NavItem( NavItemAction.Licenses, R.string.licenses, null ),
                NavItem( NavItemAction.Profile, R.string.profile, null ),
                NavItem( NavItemAction.Exit, R.string.exit, null )
        )

        view.navItemsRecyclerView?.layoutManager = LinearLayoutManager( this.context, RecyclerView.VERTICAL, false )
        view.navItemsRecyclerView?.adapter = NavItemsAdapter( navItems, object:OnMenuClickListener{

            override fun onNavigateToItem(action: NavItemAction) {

                view.navItemsRecyclerView?.postDelayed( {
                    drawerLayout?.closeDrawers()
                    listener?.onNavigateToItem(action)
                }, 200)
            }
        })
    }

    val isDrawerOpen: Boolean
        get() = drawerLayout != null && drawerLayout!!.isDrawerOpen(fragmentContainerView!!)

    /**
     * Users of this fragment must call this method to set up the navigation drawer interactions.
     *
     * @param fragmentId   The android:id of this fragment in its activity's layout.
     * @param drawerLayout The DrawerLayout containing this fragment's UI.
     */
    fun setUp(fragmentId: Int, drawerLayout: DrawerLayout?, listener: OnMenuClickListener? ) {

        this.listener = listener
        fragmentContainerView = activity?.findViewById(fragmentId) ?:return
        this.drawerLayout = drawerLayout?: return

        // set a custom shadow that overlays the main content when the drawer opens
        this.drawerLayout!!.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START)
        // set up the drawer's list view with items and click listener
        val actionBar = actionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)
        actionBar.setHomeButtonEnabled(true)

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the navigation drawer and the action bar app icon.
        drawerToggle = object : ActionBarDrawerToggle(
                activity,  /* host Activity */
                this.drawerLayout,  /* DrawerLayout object */
                R.string.navigation_drawer_open,  /* "open drawer" description for accessibility */
                R.string.navigation_drawer_close /* "close drawer" description for accessibility */
        ) {
            override fun onDrawerClosed(drawerView: View) {
                super.onDrawerClosed(drawerView)
                if (!isAdded) {
                    return
                }
                activity!!.invalidateOptionsMenu() // calls onPrepareOptionsMenu()
            }

            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
                if (!isAdded) {
                    return
                }
                activity!!.invalidateOptionsMenu() // calls onPrepareOptionsMenu()
            }
        }

        // Defer code dependent on restoration of previous instance state.
        this.drawerLayout?.post { drawerToggle?.syncState() }
        drawerToggle?.let{ this.drawerLayout?.addDrawerListener(it)}
    }

    private fun selectItem(position: Int) {

        currentSelectedPosition = position

        if (drawerLayout != null) {
            drawerLayout!!.closeDrawer(fragmentContainerView!!)
        }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(STATE_SELECTED_POSITION, currentSelectedPosition)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        // Forward the new configuration the drawer toggle component.
        drawerToggle!!.onConfigurationChanged(newConfig)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        // If the drawer is open, show the global app actions in the action bar. See also
        // showGlobalContextActionBar, which controls the top-left area of the action bar.
//        if (mDrawerLayout != null && isDrawerOpen()) {
//            inflater.inflate(R.menu.global, menu);
//            showGlobalContextActionBar();
//        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (drawerToggle!!.onOptionsItemSelected(item)) {
            true
        } else super.onOptionsItemSelected(item)
    }

    fun setEnabled(enabled:Boolean){

        drawerToggle?.setDrawerIndicatorEnabled(enabled)
        navItemsRecyclerView?.visibility = if(enabled) View.VISIBLE else View.GONE
        actionBar?.setHomeButtonEnabled(enabled)
        actionBar?.setDisplayHomeAsUpEnabled(enabled)
    }

    /**
     * Per the navigation drawer design guidelines, updates the action bar to show the global app
     * 'context', rather than just what's in the current screen.
     */
    private fun showGlobalContextActionBar() {
        val actionBar = actionBar
        actionBar?.setDisplayShowTitleEnabled(true)
        actionBar?.navigationMode = ActionBar.NAVIGATION_MODE_STANDARD
        actionBar?.setTitle(R.string.app_name)
    }

    private val actionBar: ActionBar?
        private get() = (activity as AppCompatActivity?)!!.supportActionBar


    companion object {
        /**
         * Remember the position of the selected item.
         */
        private const val STATE_SELECTED_POSITION = "selected_navigation_drawer_position"

        /**
         * Per the design guidelines, you should show the drawer on launch until the user manually
         * expands it. This shared preference tracks this.
         */
        private const val PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned"
    }
}