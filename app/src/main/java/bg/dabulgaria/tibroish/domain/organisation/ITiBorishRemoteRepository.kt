package bg.dabulgaria.tibroish.domain.organisation

import bg.dabulgaria.tibroish.domain.locations.Country
import bg.dabulgaria.tibroish.domain.user.User
import bg.dabulgaria.tibroish.domain.user.UserS
import bg.dabulgaria.tibroish.persistence.remote.model.UserDto


interface ITiBorishRemoteRepository {

    fun getOrganisations(): List<Organization>

    fun getCountries(): List<Country>

    fun createUser(firebaseJwt: String, userData: User)
}