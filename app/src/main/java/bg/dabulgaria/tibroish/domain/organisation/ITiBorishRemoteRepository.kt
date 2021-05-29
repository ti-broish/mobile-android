package bg.dabulgaria.tibroish.domain.organisation

import bg.dabulgaria.tibroish.domain.locations.*
import bg.dabulgaria.tibroish.domain.user.User
import bg.dabulgaria.tibroish.persistence.remote.model.*


interface ITiBorishRemoteRepository {

    fun getOrganisations(): List<Organization>

    fun getCountries(): List<CountryRemote>

    fun createUser(firebaseJwt: String, userData: User)

    fun getElectionRegions(): List<ElectionRegionRemote>

    fun getTowns(params: TownsRequestParams): List<TownRemote>

    fun getSections(params: SectionsRequestParams): List<SectionRemote>

    fun getUserDetails(): User

    fun updateUserDetails(user: User)

    fun deleteUser()
}