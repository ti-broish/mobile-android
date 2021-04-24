package bg.dabulgaria.tibroish.presentation.main

import android.content.Context
import android.util.Log
import androidx.fragment.app.FragmentManager
import bg.dabulgaria.tibroish.infrastructure.di.annotations.AppContext
import bg.dabulgaria.tibroish.presentation.navigation.NavItemAction
import bg.dabulgaria.tibroish.presentation.ui.home.HomeFragment
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

        var homeFragment = view?.supportFragmentMngr?.findFragmentByTag(HomeFragment.TAG )
        if (homeFragment == null) {

            homeFragment = HomeFragment.newInstance()
        }

        view?.showScreen(homeFragment, HomeFragment.TAG, false, false)
    }

//    override fun showComicList() {
//
//        var comicListFragment = view?.supportFragmentMngr?.findFragmentByTag(ComicListFragment.TAG )
//        if (comicListFragment == null) {
//
//            comicListFragment = ComicListFragment.newInstance()
//        }
//
//        view?.showScreen(comicListFragment, ComicListFragment.TAG, false, false)
//    }

//    override fun showComicDetails(comicDetailsViewData: ComicDetailsViewData) {
//
////        var comicDetailsFragment = view?.supportFragmentMngr?.findFragmentByTag(ComicDetailsFragment.TAG )
////        if (comicDetailsFragment == null) {
////
////            comicDetailsFragment = ComicDetailsFragment.newInstance(comicDetailsViewData )
////        }
////
////        view?.showScreen(comicDetailsFragment, ComicListFragment.TAG, true, true)
//    }

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