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
package bg.dabulgaria.tibroish.persistence.remote.model


import bg.dabulgaria.tibroish.domain.locations.SectionRemote
import bg.dabulgaria.tibroish.domain.locations.TownRemote
import com.squareup.moshi.Json
/**
 * 
 * @param id 
 * @param description 
 * @param status 
 * @param isPublished 
 * @param town 
 * @param pictures 
 * @param assignees 
 * @param updates 
 * @param comments 
 * @param section 
 */
data class Violation (
        val id: kotlin.String,
        val description: kotlin.String,
        val status: Violation.Status,
        val isPublished: kotlin.Boolean,
        val town: TownRemote,
        val pictures: kotlin.collections.List<Picture>,
        val assignees: kotlin.collections.List<User>,
        val updates: kotlin.collections.List<ViolationUpdate>,
        val comments: kotlin.collections.List<ViolationComment>,
        val section: SectionRemote? = null
) {

    /**
    * 
    * Values: received,processing,processed,rejected
    */
    enum class Status(val value: kotlin.String){
    
        @Json(name = "received") received("received"),
    
        @Json(name = "processing") processing("processing"),
    
        @Json(name = "processed") processed("processed"),
    
        @Json(name = "rejected") rejected("rejected");
    
    }

}

