package bg.dabulgaria.tibroish.persistence.remote.repo

import android.util.Log
import bg.dabulgaria.tibroish.domain.auth.IRemoteRepoAuthenticator
import bg.dabulgaria.tibroish.domain.organisation.ITiBorishRemoteRepository
import bg.dabulgaria.tibroish.domain.organisation.Organization
import bg.dabulgaria.tibroish.persistence.remote.api.AcceptValues
import bg.dabulgaria.tibroish.persistence.remote.api.TiBroishApiController
import bg.dabulgaria.tibroish.domain.locations.Country
import bg.dabulgaria.tibroish.domain.user.User
import bg.dabulgaria.tibroish.domain.user.UserS
import javax.inject.Inject

class TiBroishRemoteRepository @Inject constructor(private val apiController: TiBroishApiController,
                                                   private val authenticator: IRemoteRepoAuthenticator )
    :ITiBorishRemoteRepository {

    override fun getOrganisations(): List<Organization> {
        return apiController.getOrganizations(AcceptValues.ApplicationJson.value)
                .execute().body()!!
    }

    override fun getCountries(): List<Country> {
        return authenticator.executeCall { token ->
            apiController.getCountries(getAuthorization(token))} !!.execute().body()!!
    }

    override fun createUser(firebaseJwt: String, userData: User) {
        // TODO: add error handling
        apiController.createUser(getAuthorization(firebaseJwt), userData).execute()
    }

    private fun getAuthorization(idToken: String): String = "Bearer $idToken"
}