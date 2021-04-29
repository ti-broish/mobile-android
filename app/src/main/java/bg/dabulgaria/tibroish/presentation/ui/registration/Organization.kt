package bg.dabulgaria.tibroish.presentation.ui.registration

data class Organizations(var organizations: List<Organization>? = null)

data class Organization(var name: String? = null, var id: String? = null);