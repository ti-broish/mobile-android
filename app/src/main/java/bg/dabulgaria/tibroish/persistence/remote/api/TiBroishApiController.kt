package bg.dabulgaria.tibroish.persistence.remote.api

import bg.dabulgaria.tibroish.domain.organisation.Organization
import bg.dabulgaria.tibroish.persistence.remote.ApiHeader
import retrofit2.Call

import retrofit2.http.GET
import retrofit2.http.Header

interface TiBroishApiController {

    @GET("/organizations")
    fun getOrganizations(@Header(ApiHeader.Accept) accept:String ): Call<List<Organization>>
}