package bg.dabulgaria.tibroish.persistence.remote.repo

import bg.dabulgaria.tibroish.domain.auth.IRemoteRepoAuthenticator
import bg.dabulgaria.tibroish.domain.image.UploadImageRequest
import bg.dabulgaria.tibroish.domain.image.UploadImageResponse
import bg.dabulgaria.tibroish.domain.locations.CountryRemote
import bg.dabulgaria.tibroish.domain.locations.ElectionRegionRemote
import bg.dabulgaria.tibroish.domain.locations.SectionRemote
import bg.dabulgaria.tibroish.domain.locations.TownRemote
import bg.dabulgaria.tibroish.domain.organisation.ITiBroishRemoteRepository
import bg.dabulgaria.tibroish.domain.organisation.Organization
import bg.dabulgaria.tibroish.domain.protocol.ProtocolRemote
import bg.dabulgaria.tibroish.domain.protocol.ProtocolStatusRemote
import bg.dabulgaria.tibroish.domain.protocol.SendProtocolRequest
import bg.dabulgaria.tibroish.domain.user.User
import bg.dabulgaria.tibroish.domain.violation.SendViolationRequest
import bg.dabulgaria.tibroish.domain.violation.ViolationRemoteStatus
import bg.dabulgaria.tibroish.domain.violation.VoteViolationRemote
import bg.dabulgaria.tibroish.persistence.remote.api.AcceptValues
import bg.dabulgaria.tibroish.persistence.remote.api.TiBroishApiController
import bg.dabulgaria.tibroish.persistence.remote.model.SectionsRequestParams
import bg.dabulgaria.tibroish.persistence.remote.model.TownsRequestParams
import retrofit2.Call
import java.util.*
import javax.inject.Inject
import kotlin.random.Random

class TiBroishRemoteRepository @Inject constructor(private val apiController: TiBroishApiController,
                                                   private val authenticator: IRemoteRepoAuthenticator)
    : ITiBroishRemoteRepository {

    override fun getOrganisations(): List<Organization> {
        return apiController.getOrganizations(AcceptValues.ApplicationJson.value)
                .execute().body()!!
    }


    override fun createUser(firebaseJwt: String, userData: User) {
        // TODO: add error handling
        apiController.createUser(getAuthorization(firebaseJwt), userData).execute()
    }


    override fun getCountries(): List<CountryRemote> {

        val list = authenticator.executeCall { token ->
            apiController.getCountries(getAuthorization(token))
        }

        return list!!.sortedBy { it.name }
    }

    override fun getElectionRegions(): List<ElectionRegionRemote> {

        val list = authenticator.executeCall { token ->
            apiController.getElectionRegions(getAuthorization(token))
        }!!

        list.map { it.municipalities = it.municipalities.sortedBy { munic -> munic.name } }

        return list.sortedBy { it.code }
    }

    override fun getTowns(params: TownsRequestParams): List<TownRemote> {

        val towns = authenticator.executeCall({ pParams, token ->
            apiController.getTowns(getAuthorization(token),
                    pParams.countryCode,
                    pParams.electionRegionCode,
                    pParams.municipalityCode)
        }, params)!!

        towns.map { it.cityRegions = it.cityRegions?.sortedBy { region -> region.code } }
        return towns.sortedBy { it.name }
    }

    override fun getSections(params: SectionsRequestParams): List<SectionRemote> {

        val sections = authenticator.executeCall({ pParams, token ->
            apiController.getSections(getAuthorization(token),
                    pParams.townId,
                    pParams.cityRegionCode)
        }, params)!!

        return sections.sortedBy { it.code }
    }

    override fun getUserDetails(): User {
        return authenticator.executeCall { token ->
            apiController.getUserDetails(getAuthorization(token))
        }!!
    }

    override fun updateUserDetails(user: User) {
        authenticator.executeCall { token ->
            apiController.updateUserDetails(getAuthorization(token), user)
        }
    }

    override fun deleteUser() {
        authenticator.executeCall { token ->
            apiController.deleteUser(getAuthorization(token))
        }
    }

    override fun uploadImage(imageRequest: UploadImageRequest): UploadImageResponse {

        return authenticator.executeCall( { pParams, token ->
            apiController.uploadImage(getAuthorization(token), pParams )}, imageRequest)!!
    }

    override fun sendProtocol(request: SendProtocolRequest): ProtocolRemote {

        return authenticator.executeCall( { pParams, token ->
            apiController.sendProtocol(getAuthorization(token), pParams )}, request)!!
    }

    override fun sendViolation(request: SendViolationRequest): VoteViolationRemote {
        return authenticator.executeCall( { pParams, token ->
            apiController.sendViolation(getAuthorization(token), pParams)}, request)!!
    }

    override fun getUserProtocols(): List<ProtocolRemote> {
        return authenticator.executeCall { token ->
            apiController.getUserProtocols(getAuthorization(token))
        }!!.sortedByDescending { protocolRemote -> protocolRemote.id }
    }

    override fun getViolations(): List<VoteViolationRemote> {

        return authenticator.executeCall { token ->
            apiController.getViolations(getAuthorization(token))} !!
    }

   private fun getAuthorization(idToken: String): String = "Bearer $idToken"
}