package bg.dabulgaria.tibroish.presentation.main

import android.content.Context
import bg.dabulgaria.tibroish.infrastructure.di.annotations.AppContext
import bg.dabulgaria.tibroish.presentation.ui.home.HomeFragment
import bg.dabulgaria.tibroish.presentation.ui.protocol.add.ComicDetailsFragment
import bg.dabulgaria.tibroish.presentation.ui.protocol.add.ComicDetailsViewData
import bg.dabulgaria.tibroish.presentation.ui.protocol.list.ComicListFragment
import javax.inject.Inject

class MainNavigator @Inject constructor(@AppContext private val appContext: Context )
    :IMainNavigator{

    private var view: IMainScreenView? = null

    override fun setView(view: IMainScreenView?) {

        this.view = view
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

    override fun showComicList() {

        var comicListFragment = view?.supportFragmentMngr?.findFragmentByTag(ComicListFragment.TAG )
        if (comicListFragment == null) {

            comicListFragment = ComicListFragment.newInstance()
        }

        view?.showScreen(comicListFragment, ComicListFragment.TAG, false, false)
    }

    override fun showComicDetails(comicDetailsViewData: ComicDetailsViewData) {

        var comicDetailsFragment = view?.supportFragmentMngr?.findFragmentByTag(ComicDetailsFragment.TAG )
        if (comicDetailsFragment == null) {

            comicDetailsFragment = ComicDetailsFragment.newInstance(comicDetailsViewData )
        }

        view?.showScreen(comicDetailsFragment, ComicListFragment.TAG, true, true)
    }
    //endregion IMainNavigator implementation
}