package bg.dabulgaria.tibroish.domain

import bg.dabulgaria.tibroish.presentation.ui.registration.CountryCode

interface ICountryCodesRepo {

    fun getCountryCodes(): List<CountryCode>?
}