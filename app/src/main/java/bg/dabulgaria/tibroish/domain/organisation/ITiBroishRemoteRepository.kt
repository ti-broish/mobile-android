package bg.dabulgaria.tibroish.domain.organisation

import bg.dabulgaria.tibroish.domain.image.UploadImageRequest
import bg.dabulgaria.tibroish.domain.image.UploadImageResponse
import bg.dabulgaria.tibroish.domain.locations.*
import bg.dabulgaria.tibroish.domain.protocol.ProtocolRemote
import bg.dabulgaria.tibroish.domain.protocol.SendProtocolRequest
import bg.dabulgaria.tibroish.domain.user.User
import bg.dabulgaria.tibroish.domain.violation.SendViolationRequest
import bg.dabulgaria.tibroish.domain.violation.VoteViolationRemote
import bg.dabulgaria.tibroish.persistence.remote.api.ApiHeader
import bg.dabulgaria.tibroish.persistence.remote.model.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header


interface ITiBroishRemoteRepository {

    fun getOrganisations(): List<Organization>

    fun getCountries(): List<CountryRemote>

    fun createUser(firebaseJwt: String, userData: User)

    fun getElectionRegions(): List<ElectionRegionRemote>

    fun getTowns(params: TownsRequestParams): List<TownRemote>

    fun getSections(params: SectionsRequestParams): List<SectionRemote>

    fun getUserDetails(): User

    fun updateUserDetails(user: User)

    fun deleteUser()

    fun uploadImage(image: UploadImageRequest): UploadImageResponse

    fun sendProtocol(request: SendProtocolRequest): ProtocolRemote

    fun sendViolation(request: SendViolationRequest): VoteViolationRemote

    fun getUserProtocols(): List<ProtocolRemote>
}