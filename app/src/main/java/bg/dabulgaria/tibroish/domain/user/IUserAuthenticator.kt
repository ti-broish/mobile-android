package bg.dabulgaria.tibroish.domain.user

import bg.dabulgaria.tibroish.domain.auth.IAuthRepository
import javax.inject.Inject

interface IUserAuthenticator {

    fun isUserLogged():Boolean

    fun logout()
}

class UserAuthenticator @Inject constructor(private val userRemoteRepo: IUserRemoteRepository,
                                            private val authRepo: IAuthRepository,
                                            private val userLocalRepo: IUserLocalRepository)
    : IUserAuthenticator{

    override fun isUserLogged(): Boolean {

        val remoteUser = userRemoteRepo.getUser()
        val localUser = userLocalRepo.user
        val token = authRepo.token

        if( remoteUser == null || localUser == null || token.isEmpty()) {

            logout()
            return false
        }

        return true
    }

    override fun logout() {

        userRemoteRepo.logout()
        userLocalRepo.user = null
        authRepo.token = ""
    }
}