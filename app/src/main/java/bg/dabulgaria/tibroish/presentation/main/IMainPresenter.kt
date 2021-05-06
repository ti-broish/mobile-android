package bg.dabulgaria.tibroish.presentation.main

import androidx.drawerlayout.widget.DrawerLayout
import bg.dabulgaria.tibroish.domain.user.IUserAuthenticator
import bg.dabulgaria.tibroish.presentation.navigation.NavItemAction
import bg.dabulgaria.tibroish.presentation.navigation.OnMenuClickListener
import javax.inject.Inject

interface IMainPresenter : OnMenuClickListener {

    var view: IMainScreenView?

    fun onAuthEvent(coldStart:Boolean)
}

class MainPresenter @Inject constructor(private val mainRouter: IMainRouter,
                                        private val userAuthenticator: IUserAuthenticator)
    : IMainPresenter{

    init {
        mainRouter.setPresenter( this )
    }

    private var _view: IMainScreenView? = null

    override var view: IMainScreenView?
        get() = _view
        set(value) {
            _view = value
            mainRouter.setView( _view )
        }

    override fun onAuthEvent(coldStart:Boolean) {

        if( !userAuthenticator.isUserLogged() ){

            mainRouter.showLoginScreen()
            view?.showNavigation(false)
        }
        else if(coldStart) {

            mainRouter.showHomeScreen()
            view?.showNavigation(true)
        }
    }

    override fun onNavigateToItem(action: NavItemAction) {

        view?.closeDrawer()

        if(action == NavItemAction.Exit){

            userAuthenticator.logout()
            mainRouter.showLoginScreen()
            view?.showNavigation(false)
            return
        }
        mainRouter.onNavigateToItem(action)
    }
}