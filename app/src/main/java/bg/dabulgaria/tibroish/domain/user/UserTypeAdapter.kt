package bg.dabulgaria.tibroish.domain.user

import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

interface IUserTypeAdapter{

    fun toUser(firebaseUser: FirebaseUser):User
}

class UserTypeAdapter @Inject constructor() :IUserTypeAdapter {

    override fun toUser(firebaseUser: FirebaseUser):User {

        return User().apply {
            this.uid = firebaseUser.uid
            this.providerId = firebaseUser.providerId
            this.displayName = firebaseUser.displayName
            this.photoUrl = firebaseUser.photoUrl
            this.email = firebaseUser.email
            this.phoneNumber = firebaseUser.phoneNumber
            this.isEmailVerified = firebaseUser.isEmailVerified
        }
    }

}