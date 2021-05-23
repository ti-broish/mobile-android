/**
* Ti Broish API
* Ti Broish API is built for clients sending in election results data in Bulgaria
*
* OpenAPI spec version: 0.1
* Contact: team@dabulgaria.bg
*
* NOTE: This class is auto generated by the swagger code generator program.
* https://github.com/swagger-api/swagger-codegen.git
* Do not edit the class manually.
*/
package bg.dabulgaria.tibroish.domain.locations

import java.io.Serializable

data class CountryRemote (
    val code: String,
    val name: String,
    val isAbroad: Boolean,
):Serializable

data class ElectionRegionRemote (
        val code: String,
        val name: String,
        val isAbroad: Boolean,
        var municipalities: List<MunicipalityRemote>
) :Serializable

data class MunicipalityRemote (
        val code: String,
        val name: String
) :Serializable

data class TownRemote (
        val id: Long,
        val code: Long,
        val name: String,
        val countryCode:String?,
        val electionRegionCode:String?,
        val municipalityCode:String?,
        var cityRegions: List<CityRegionRemote>?
): Serializable

data class CityRegionRemote (
        val code: String,
        val name: String
) :Serializable

data class SectionRemote (
        val id: String,
        val code: String,
        val place: String,
        val name: String
) :Serializable