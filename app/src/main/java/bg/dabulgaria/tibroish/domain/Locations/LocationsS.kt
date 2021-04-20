package bg.dabulgaria.tibroish.domain.Locations

import java.io.Serializable


//
//MunicipalityDto{
//    code*	string
//    name*	string
//}
//
//ElectionRegionDto{
//    code*	string
//    name*	string
//    isAbroad*	boolean
//    municipalities*	[...]
//}
//
//class CityRegionDto{
//    code*	string
//    name*	string
//}
//
//Country{
//    cityRegions*	{...}
//    id*	number
//    code*	string
//    name*	string
//    isAbroad*	boolean
//    towns*	[...]
//    sectionsCount*	number
//}

open class CountryS constructor():Serializable{

    var uuid :String?=null
    var name :String?=null
    var isocode :String?=null

    constructor( country:CountryS ):this(){

        this.uuid = country.uuid
        this.name = country.name
        this.isocode = country.isocode
    }
}

open class RegionS constructor():Serializable{

    var uuid :String?=null
    var name :String?=null
    var country:CountryS?=null
    var countryUuid:String?= null

    constructor(region: RegionS):this(){

        this.uuid = region.uuid
        this.name = region.name
        this.countryUuid = region.countryUuid
    }
}

open class MunicipalityS constructor():Serializable{

    var uuid :String?=null
    var name :String?=null
    var region:RegionS?=null
    var regionUuid:String?= null

    constructor(municipality: MunicipalityS):this(){

        this.uuid = municipality.uuid
        this.name = municipality.name
        this.regionUuid = municipality.regionUuid
    }
}

open class CityS constructor():Serializable{

    var id: Long? = null
    var uuid :String?=null
    var name :String?=null
    var postcode :String?=null
    var municipality:MunicipalityS?=null
    var municipalityUuid:String?= null

    constructor(city: CityS):this(){

        this.id= city.id
        this.uuid = city.uuid
        this.name = city.name
        this.postcode = city.postcode
        this.municipalityUuid = city.municipalityUuid
    }
}

enum class SODeliveryType (val typeValue:Int){
    None(0),
    ToCustomerAddress(1),
    AtCommonPlace(2)
}

open class DeliveryPlaceS constructor():Serializable{

    var uuid :String?=null
    var name :String?=null
    var description:String?=null

    var city:CityS?=null
    var cityUuid:String?= null

    var type : SODeliveryType? = null
    var streetAddress:String?=null
    var longtitude:Double?=null
    var latitude:Double?=null

    constructor(deliveryPlace: DeliveryPlaceS?):this(){

        deliveryPlace?:return
        this.uuid = deliveryPlace.uuid
        this.name = deliveryPlace.name
        this.cityUuid = deliveryPlace.cityUuid
        this.description = deliveryPlace.description
        this.type = deliveryPlace.type
        this.streetAddress = deliveryPlace.streetAddress
        this.longtitude = deliveryPlace.longtitude
        this.latitude = deliveryPlace.latitude
    }
}

class DeliveryPlaceEx constructor(deliveryPlace: DeliveryPlaceS?)
    :DeliveryPlaceS(deliveryPlace), Serializable{

}

class CityEx constructor(city: CityS):CityS(city), Serializable{

    val deliveryPlaces = mutableListOf<DeliveryPlaceEx>()
}

class MunicipalityEx constructor(municipality: MunicipalityS)
    :MunicipalityS(municipality), Serializable{

    val cities = mutableListOf<CityEx>()
}

class RegionEx constructor(region: RegionS):RegionS(region), Serializable{

    val municipalities = mutableListOf<MunicipalityEx>()
}

class CountryEx constructor(country: CountryS):CountryS(country), Serializable{

    val regions = mutableListOf<RegionEx>()
}

class LocationsS constructor():Serializable{

    val countries = mutableListOf<CountryS>()
    val regions = mutableListOf<RegionS>()
    val municipalities = mutableListOf<MunicipalityS>()
    val cities = mutableListOf<CityS>()
    var timeTaken:Long =0
}