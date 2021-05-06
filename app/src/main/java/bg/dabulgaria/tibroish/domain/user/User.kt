package bg.dabulgaria.tibroish.domain.user

import android.net.Uri
import java.io.Serializable

class User :Serializable {

    var uid: String?=""
    var providerId: String?=""
    var displayName: String?=""
    var photoUrl: Uri? =null
    var email: String?=""
    var phoneNumber: String?=""
    var isEmailVerified: Boolean=false
}