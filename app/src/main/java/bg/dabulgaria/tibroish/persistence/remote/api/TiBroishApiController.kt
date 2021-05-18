package bg.dabulgaria.tibroish.persistence.remote.api

import bg.dabulgaria.tibroish.domain.organisation.Organization
import bg.dabulgaria.tibroish.domain.locations.Country
import bg.dabulgaria.tibroish.domain.user.User
import bg.dabulgaria.tibroish.domain.user.UserS
import retrofit2.Call
import retrofit2.http.Body

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface TiBroishApiController {

    @GET("/organizations")
    fun getOrganizations(@Header(ApiHeader.Accept) accept:String ): Call<List<Organization>>

    @GET("/countries")
    fun getCountries(@Header(ApiHeader.Authorization) authorization:String): Call<List<Country>>

    @POST("/users")
    fun createUser(@Header(ApiHeader.Authorization) authorization: String, @Body userData: User)
            :Call<Any>
}
