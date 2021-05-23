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
}

class UserS(
    var userDetails: User,
    var firebaseUid: String,
    var firebaseJwt: String
) : Serializable
