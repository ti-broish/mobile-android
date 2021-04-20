package bg.dabulgaria.tibroish.persistence.remote

import bg.dabulgaria.tibroish.domain.Locations.LocationsS
import javax.inject.Inject

interface ILocationsRemoteRepo{

    fun getLocations() :LocationsS
}

class LocationsRemoteRepo @Inject constructor( val vdApiController:VDApiController ) :ILocationsRemoteRepo{

    override fun getLocations(): LocationsS {

        return vdApiController.getLocations().execute().body()!!
    }
}