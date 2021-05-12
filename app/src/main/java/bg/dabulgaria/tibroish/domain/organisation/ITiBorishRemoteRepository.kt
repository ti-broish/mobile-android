package bg.dabulgaria.tibroish.domain.organisation

import bg.dabulgaria.tibroish.domain.locations.Country


interface ITiBorishRemoteRepository {

    fun getOrganisations(): List<Organization>

    fun getCountries(): List<Country>
}