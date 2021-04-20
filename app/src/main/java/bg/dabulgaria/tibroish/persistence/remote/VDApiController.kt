package bg.dabulgaria.tibroish.persistence.remote

import bg.dabulgaria.tibroish.domain.Locations.LocationsS
import retrofit2.Call
import retrofit2.http.GET

interface VDApiController {

    @GET("showCities")
    fun getLocations() : Call<LocationsS>
}