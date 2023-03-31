package bg.dabulgaria.tibroish.persistence.remote.api

import bg.dabulgaria.tibroish.domain.image.UploadImageRequest
import bg.dabulgaria.tibroish.domain.image.UploadImageResponse
import bg.dabulgaria.tibroish.domain.locations.CountryRemote
import bg.dabulgaria.tibroish.domain.locations.ElectionRegionRemote
import bg.dabulgaria.tibroish.domain.locations.SectionRemote
import bg.dabulgaria.tibroish.domain.locations.TownRemote
import bg.dabulgaria.tibroish.domain.organisation.Organization
import bg.dabulgaria.tibroish.domain.protocol.ProtocolRemote
import bg.dabulgaria.tibroish.domain.protocol.SendProtocolRequest
import bg.dabulgaria.tibroish.domain.push.SendTokenRequest
import bg.dabulgaria.tibroish.domain.push.SendTokenResponse
import bg.dabulgaria.tibroish.domain.stream.StreamRequest
import bg.dabulgaria.tibroish.domain.stream.StreamResponse
import bg.dabulgaria.tibroish.domain.user.SendCheckInRequest
import bg.dabulgaria.tibroish.domain.user.SendCheckInResponse
import bg.dabulgaria.tibroish.domain.user.User
import bg.dabulgaria.tibroish.domain.violation.SendViolationRequest
import bg.dabulgaria.tibroish.domain.violation.VoteViolationRemote
import bg.dabulgaria.tibroish.live.model.UserStreamModel
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface TiBroishApiController {

    @GET("/organizations")
    fun getOrganizations(@Header(ApiHeader.Accept) accept: String): Call<List<Organization>>

    @GET("/countries")
    fun getCountries(@Header(ApiHeader.Authorization) authorization: String): Call<List<CountryRemote>>

    @POST("/users")
    fun createUser(@Header(ApiHeader.Authorization) authorization: String, @Body userData: User)
            : Call<Any>

    @GET("/election_regions")
    fun getElectionRegions(@Header(ApiHeader.Authorization) authorization: String): Call<List<ElectionRegionRemote>>

    @GET("/towns")
    fun getTowns(@Header(ApiHeader.Authorization) authorization: String,
                 @Query("country") country: String,
                 @Query("election_region") electionRegion: String?,
                 @Query("municipality") municipality: String?): Call<List<TownRemote>>

    @GET("/sections")
    fun getSections(@Header(ApiHeader.Authorization) authorization: String,
                    @Query("town") townCode: Long,
                    @Query("city_region") cityRegionCode: String?): Call<List<SectionRemote>>

    @GET("/me")
    fun getUserDetails(@Header(ApiHeader.Authorization) authorization: String): Call<User>

    @PATCH("/me")
    fun updateUserDetails(
            @Header(ApiHeader.Authorization) authorization: String, @Body userData: User): Call<Any>

    @DELETE("/me")
    fun deleteUser(@Header(ApiHeader.Authorization) authorization: String): Call<ResponseBody>

    @POST("/pictures")
    fun uploadImage(@Header(ApiHeader.Authorization) authorization: String,
                    @Body image: UploadImageRequest): Call<UploadImageResponse>

    @POST("/protocols")
    fun sendProtocol(@Header(ApiHeader.Authorization) authorization: String,
                     @Body request: SendProtocolRequest): Call<ProtocolRemote>

    @POST("/violations")
    fun sendViolation(@Header(ApiHeader.Authorization) authorization: String,
                      @Body request: SendViolationRequest): Call<VoteViolationRemote>

    @GET("/me/protocols/")
    fun getUserProtocols(@Header(ApiHeader.Authorization) authorization: String):
            Call<List<ProtocolRemote>>

    @GET("/me/violations")
    fun getViolations(@Header(ApiHeader.Authorization) authorization: String): Call<List<VoteViolationRemote>>

    @POST("/me/checkins ")
    fun sendCheckIn(@Header(ApiHeader.Authorization) authorization: String,
                    @Body request: SendCheckInRequest): Call<Any>

    @POST("/me/clients")
    fun sendFCMToken(@Header(ApiHeader.Authorization) authorization: String,
                     @Body tokenRequest: SendTokenRequest): Call<SendTokenResponse>

    @POST("streams")
    fun postStream(@Header(ApiHeader.Authorization) authorization: String,
                  @Body tokenRequest: StreamRequest): Call<UserStreamModel>


    @GET("me/stream")
    fun getUserStream(@Header("Authorization") firebaseTokenHeader: String): Call<UserStreamModel>


    @GET("/countries")
    fun getCountries(): Call<List<CountryRemote>>

    @POST("/users")
    fun createUser(@Body userData: User) : Call<Any>

    @GET("/election_regions")
    fun getElectionRegions(): Call<List<ElectionRegionRemote>>

    @GET("/towns")
    fun getTowns(@Query("country") country: String,
                 @Query("election_region") electionRegion: String?,
                 @Query("municipality") municipality: String?): Call<List<TownRemote>>

    @GET("/sections")
    fun getSections(@Query("town") townCode: Long,
                    @Query("city_region") cityRegionCode: String?): Call<List<SectionRemote>>

    @POST("/pictures")
    fun uploadImage(@Body image: UploadImageRequest): Call<UploadImageResponse>

    @POST("/protocols")
    fun sendProtocol(@Body request: SendProtocolRequest): Call<ProtocolRemote>

    @POST("/violations")
    fun sendViolation(@Body request: SendViolationRequest): Call<VoteViolationRemote>

    @POST("/me/checkins ")
    fun sendCheckIn(@Body request: SendCheckInRequest): Call<Any>

    @POST("/me/clients")
    fun sendFCMToken(@Body tokenRequest: SendTokenRequest): Call<SendTokenResponse>

    @POST("streams")
    fun postStream(@Body tokenRequest: StreamRequest): Call<UserStreamModel>

}
