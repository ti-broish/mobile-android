package bg.dabulgaria.tibroish.persistence.remote.api

import bg.dabulgaria.tibroish.domain.image.UploadImageRequest
import bg.dabulgaria.tibroish.domain.image.UploadImageResponse
import bg.dabulgaria.tibroish.domain.locations.*
import bg.dabulgaria.tibroish.domain.organisation.Organization
import bg.dabulgaria.tibroish.domain.protocol.ProtocolRemote
import bg.dabulgaria.tibroish.domain.protocol.SendProtocolRequest
<<<<<<< HEAD
import bg.dabulgaria.tibroish.domain.user.SendCheckInRequest
import bg.dabulgaria.tibroish.domain.user.SendCheckInResponse
=======
import bg.dabulgaria.tibroish.domain.push.SendTokenRequest
import bg.dabulgaria.tibroish.domain.push.SendTokenResponse
>>>>>>> Firebase FCM Token #28 (+2 squashed commit)
import bg.dabulgaria.tibroish.domain.user.User
import bg.dabulgaria.tibroish.domain.violation.SendViolationRequest
import bg.dabulgaria.tibroish.domain.violation.VoteViolationRemote
import retrofit2.Call
import retrofit2.http.Body

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.*

interface TiBroishApiController {

    @GET("/organizations")
    fun getOrganizations(@Header(ApiHeader.Accept) accept:String ): Call<List<Organization>>

    @GET("/countries")
    fun getCountries(@Header(ApiHeader.Authorization) authorization:String): Call<List<CountryRemote>>

    @POST("/users")
    fun createUser(@Header(ApiHeader.Authorization) authorization: String, @Body userData: User)
            :Call<Any>

    @GET("/election_regions")
    fun getElectionRegions(@Header(ApiHeader.Authorization) authorization:String): Call<List<ElectionRegionRemote>>

    @GET("/towns")
    fun getTowns(@Header(ApiHeader.Authorization) authorization:String,
                 @Query("country" ) country: String,
                 @Query("election_region" ) electionRegion: String?,
                 @Query("municipality" ) municipality: String?): Call<List<TownRemote>>

    @GET("/sections")
    fun getSections(@Header(ApiHeader.Authorization) authorization:String,
                    @Query("town" ) townCode: Long,
                    @Query("city_region" ) cityRegionCode: String?): Call<List<SectionRemote>>

    @GET("/me")
    fun getUserDetails(@Header(ApiHeader.Authorization) authorization:String): Call<User>

    @PATCH("/me")
    fun updateUserDetails(
        @Header(ApiHeader.Authorization) authorization:String, @Body userData: User): Call<Any>

    @DELETE("/me")
    fun deleteUser(@Header(ApiHeader.Authorization) authorization:String): Call<Any>

    @POST("/pictures")
    fun uploadImage(@Header(ApiHeader.Authorization) authorization:String,
                    @Body image: UploadImageRequest):Call<UploadImageResponse>

    @POST("/protocols")
    fun sendProtocol(@Header(ApiHeader.Authorization) authorization:String,
                     @Body request: SendProtocolRequest):Call<ProtocolRemote>

    @POST("/violations")
    fun sendViolation(@Header(ApiHeader.Authorization) authorization:String,
                     @Body request: SendViolationRequest):Call<VoteViolationRemote>

    @GET("/me/protocols/")
    fun getUserProtocols(@Header(ApiHeader.Authorization) authorization:String):
            Call<List<ProtocolRemote>>

    @GET("/me/violations")
    fun getViolations(@Header(ApiHeader.Authorization) authorization:String):Call<List<VoteViolationRemote>>

    @POST("/checkin")
    fun sendCheckIn(@Header(ApiHeader.Authorization) authorization:String,
                      @Body request: SendCheckInRequest):Call<SendCheckInResponse>

    @POST("/me/clients")
    fun sendFCMToken(@Header(ApiHeader.Authorization) authorization: String,
                     @Body tokenRequest: SendTokenRequest): Call<SendTokenResponse>
}
