package bg.dabulgaria.tibroish.persistence.remote.repo

import bg.dabulgaria.tibroish.domain.user.IUserRemoteRepository
import bg.dabulgaria.tibroish.domain.user.IUserTypeAdapter
import bg.dabulgaria.tibroish.domain.user.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class UserRemoteRepository @Inject constructor(private val userTypeAdapter: IUserTypeAdapter)
    :IUserRemoteRepository {

    private var auth = FirebaseAuth.getInstance()

    override fun getUser(): User? {

        val currentUser:FirebaseUser = auth.currentUser?:
        return null

        return userTypeAdapter.toUser(currentUser)
    }



    override fun logout(){

        auth.signOut()
    }
}