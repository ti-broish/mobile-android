package bg.dabulgaria.tibroish.domain.user

import android.net.Uri
import bg.dabulgaria.tibroish.domain.organisation.Organization

import java.io.Serializable

class User : Serializable {
    var firstName = ""
    var lastName: String = ""
    var pin: String = ""
    var organization: Organization? = null
    var hasAgreedToKeepData: Boolean = false
    var firebaseUid: String? = ""
    var providerId: String? = ""
    var displayName: String? = ""
    var photoUrl: Uri? = null
    var email: String? = ""
    var phone: String? = ""
    var isEmailVerified: Boolean = false

    fun copy(): User {
        val user = User()
        user.firstName = firstName
        user.lastName = lastName
        user.pin = pin
        user.organization = organization
        user.hasAgreedToKeepData = hasAgreedToKeepData
        user.firebaseUid = firebaseUid
        user.providerId = providerId
        user.displayName = displayName
        user.photoUrl = photoUrl
        user.email = email
        user.phone = phone
        user.isEmailVerified = isEmailVerified
        return user
    }
}

class UserS(
    var userDetails: User,
    var firebaseUid: String,
    var firebaseJwt: String
) : Serializable
