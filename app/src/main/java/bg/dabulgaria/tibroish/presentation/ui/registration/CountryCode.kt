package bg.dabulgaria.tibroish.presentation.ui.registration

data class Countries (
        var countries: List<CountryCode>? = null)

data class CountryCode (
        var name: String? = null,
        var code: String? = null)