package bg.dabulgaria.tibroish.presentation.ui.common

import bg.dabulgaria.tibroish.domain.organisation.Organization

data class UserDataWrapper (
    var firstName: String,
    var lastName: String,
    var email: String,
    var phone: String,
    var pin: String,
    var organization: Organization,
    var hasAgreedToKeepData: Boolean = false,
    var password: String)