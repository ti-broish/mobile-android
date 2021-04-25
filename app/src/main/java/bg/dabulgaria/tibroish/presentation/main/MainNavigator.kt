package bg.dabulgaria.tibroish.presentation.main

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.FragmentManager
import bg.dabulgaria.tibroish.infrastructure.di.annotations.AppContext
import bg.dabulgaria.tibroish.presentation.navigation.NavItemAction
import bg.dabulgaria.tibroish.presentation.ui.home.HomeFragment
import bg.dabulgaria.tibroish.presentation.ui.protocol.add.AddProtocolFragment
import bg.dabulgaria.tibroish.presentation.ui.protocol.add.AddProtocolViewData
import javax.inject.Inject

class MainNavigator @Inject constructor(@AppContext private val appContext: Context )
    :IMainNavigator{

    private var view: IMainScreenView? = null

    override fun setView(view: IMainScreenView?) {

        this.view = view
    }

    override fun onNavigateToItem(action: NavItemAction) {

        Log.i( TAG, action.name )

        when(action){
            NavItemAction.Home -> {
                clearBackStack()
                showHomeScreen()
            }
            NavItemAction.Profile ->{}
            NavItemAction.SendProtocol -> {}
            NavItemAction.SendSignal -> {}
            NavItemAction.MyProtocols -> {}
            NavItemAction.MySignals -> {}
            NavItemAction.RightsAndObligations -> {}
            NavItemAction.YouCountLive -> {}
            NavItemAction.Exit -> {}
        }
    }
    //region IMainNavigator implementation
    override fun showHomeScreen() {

        view ?: return

        clearBackStack()

        var homeFragment = view?.supportFragmentMngr?.findFragmentByTag(HomeFragment.TAG )
        if (homeFragment == null) {

            homeFragment = HomeFragment.newInstance()
        }

        view?.showScreen(homeFragment, HomeFragment.TAG, false, false)
    }

    override fun openAppSettings() {

        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.data = Uri.parse("package:" + appContext.packageName)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
        appContext.startActivity(intent)
    }

    override fun showAddProtocol() {

        var comicDetailsFragment = view?.supportFragmentMngr?.findFragmentByTag(AddProtocolFragment.TAG )
        if (comicDetailsFragment == null) {

            clearBackStack()
            comicDetailsFragment = AddProtocolFragment.newInstance(AddProtocolViewData())
        }

        view?.showScreen(comicDetailsFragment, AddProtocolFragment.TAG, addToBackStack = true, transitionContent = true)
    }

    private fun clearBackStack() {

        try {
            val fragmentManager = view?.supportFragmentMngr ?: return
            //this will clear the back stack and displays no animation on the screen
            fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        } catch (ex: Exception) {
            Log.e(TAG, "Error occurred while trying to navigate to product chooser screen.", ex)
        }
    }
    //endregion IMainNavigator implementation

    companion object {
        val TAG = MainNavigator::class.java.simpleName
    }
}