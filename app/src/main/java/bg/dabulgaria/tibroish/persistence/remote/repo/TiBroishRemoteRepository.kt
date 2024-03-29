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
import bg.dabulgaria.tibroish.domain.protocol.SendProtocolRequest
import bg.dabulgaria.tibroish.domain.push.SendTokenRequest
import bg.dabulgaria.tibroish.domain.push.SendTokenResponse
import bg.dabulgaria.tibroish.domain.stream.StreamRequest
import bg.dabulgaria.tibroish.domain.user.IUserAuthenticator
import bg.dabulgaria.tibroish.domain.user.SendCheckInRequest
import bg.dabulgaria.tibroish.domain.user.SendCheckInResponse
import bg.dabulgaria.tibroish.domain.user.User
import bg.dabulgaria.tibroish.domain.violation.SendViolationRequest
import bg.dabulgaria.tibroish.domain.violation.VoteViolationRemote
import bg.dabulgaria.tibroish.live.model.UserStreamModel
import bg.dabulgaria.tibroish.persistence.remote.api.AcceptValues
import bg.dabulgaria.tibroish.persistence.remote.api.TiBroishApiController
import bg.dabulgaria.tibroish.persistence.remote.model.SectionsRequestParams
import bg.dabulgaria.tibroish.persistence.remote.model.TownsRequestParams
import javax.inject.Inject

class TiBroishRemoteRepository @Inject constructor(
    private val apiController: TiBroishApiController,
    private val authenticator: IRemoteRepoAuthenticator,
    private val userAuthenticator: IUserAuthenticator
)
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

        val list = if(userAuthenticator.isUserLogged()) {
            authenticator.executeCall { token ->
                apiController.getCountries(getAuthorization(token))
            }
        }
        else
            apiController.getCountries().execute().body()!!

        return list!!.sortedBy { it.name }
    }

    override fun getElectionRegions(): List<ElectionRegionRemote> {


        val list = if(userAuthenticator.isUserLogged()) {
            authenticator.executeCall { token ->
                apiController.getElectionRegions(getAuthorization(token))
            }!!
        }
        else
            apiController.getElectionRegions().execute().body()!!

        list.map { it.municipalities = it.municipalities.sortedBy { munic -> munic.name } }

        return list.sortedBy { it.code }
    }

    override fun getTowns(params: TownsRequestParams): List<TownRemote> {

        val towns = if(userAuthenticator.isUserLogged()) {
            authenticator.executeCall({ pParams, token ->
                apiController.getTowns(
                    getAuthorization(token),
                    pParams.countryCode,
                    pParams.electionRegionCode,
                    pParams.municipalityCode
                )
            }, params)!!
        }
        else
            apiController.getTowns(
                params.countryCode,
                params.electionRegionCode,
                params.municipalityCode
            ).execute().body()!!

        towns.map { it.cityRegions = it.cityRegions?.sortedBy { region -> region.code } }
        return towns.sortedBy { it.name }
    }

    override fun getSections(params: SectionsRequestParams): List<SectionRemote> {

        val sections =
            if(userAuthenticator.isUserLogged()) {
                authenticator.executeCall({ pParams, token ->
                    apiController.getSections(
                        getAuthorization(token),
                        pParams.townId,
                        pParams.cityRegionCode
                    )
                }, params)!!
            }
            else{
                apiController.getSections(
                    params.townId,
                    params.cityRegionCode
                ).execute().body()!!
            }

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

        return if(userAuthenticator.isUserLogged()) {
            authenticator.executeCall({ pParams, token ->
                apiController.uploadImage(getAuthorization(token), pParams)
            }, imageRequest)!!
        }
        else{
            apiController.uploadImage(imageRequest).execute().body()!!
        }
    }

    override fun sendProtocol(request: SendProtocolRequest): ProtocolRemote {

        return if(userAuthenticator.isUserLogged()) {
            authenticator.executeCall({ pParams, token ->
                apiController.sendProtocol(getAuthorization(token), pParams)
            }, request)!!
        }
        else{
            apiController.sendProtocol(request).execute().body()!!
        }
    }

    override fun sendViolation(request: SendViolationRequest): VoteViolationRemote {

        return if(userAuthenticator.isUserLogged()){
            authenticator.executeCall({ pParams, token ->
                apiController.sendViolation(getAuthorization(token), pParams)
                                      }, request)!!
        }
        else
            apiController.sendViolation(request).execute().body()!!
    }

    override fun getUserProtocols(): List<ProtocolRemote> {
        return authenticator.executeCall { token ->
            apiController.getUserProtocols(getAuthorization(token))
        }!!.sortedBy { protocolRemote -> protocolRemote.id }
    }

    override fun getViolations(): List<VoteViolationRemote> {
        return authenticator.executeCall { token ->
            apiController.getViolations(getAuthorization(token))
        }!!.sortedBy { violationRemote -> violationRemote.id }
    }

    override fun sendCheckIn(request: SendCheckInRequest) {

        return authenticator.executeVoidCall( { pParams, token ->
            apiController.sendCheckIn(getAuthorization(token), pParams)}, request)
    }

    override fun sendFCMToken(request: SendTokenRequest): SendTokenResponse {

        return if(userAuthenticator.isUserLogged()) {

            authenticator.executeCall({ param, token ->
                apiController.sendFCMToken(getAuthorization(token), param)
            }, request)!!
        }
        else{
            apiController.sendFCMToken(request).execute().body()!!
        }
    }

    override fun getUserStream(): UserStreamModel {
        return authenticator.executeCall { token ->
            apiController.getUserStream(getAuthorization(token))
        } ?: throw  Exception()
    }

    override fun getStream(request: StreamRequest): UserStreamModel {

        return authenticator.executeCall({ param, token ->
            apiController.postStream(getAuthorization(token), param)
        }, request)!!
    }

    private fun getAuthorization(idToken: String): String = "Bearer $idToken"

}
