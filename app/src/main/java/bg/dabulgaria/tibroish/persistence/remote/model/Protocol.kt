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

import bg.dabulgaria.tibroish.persistence.remote.model.Picture
import bg.dabulgaria.tibroish.persistence.remote.model.ProtocolAction
import bg.dabulgaria.tibroish.persistence.remote.model.ProtocolData
import bg.dabulgaria.tibroish.persistence.remote.model.ProtocolResult
import bg.dabulgaria.tibroish.persistence.remote.model.Section
import bg.dabulgaria.tibroish.persistence.remote.model.User

import com.squareup.moshi.Json
/**
 * 
 * @param id 
 * @param origin 
 * @param status 
 * @param data 
 * @param section 
 * @param pictures 
 * @param assignees 
 * @param actions 
 * @param results 
 * @param parent 
 */
data class Protocol (
    val id: kotlin.String,
    val origin: kotlin.String,
    val status: Protocol.Status,
    val data: ProtocolData,
    val section: Section,
    val pictures: kotlin.collections.List<Picture>,
    val assignees: kotlin.collections.List<User>,
    val actions: kotlin.collections.List<ProtocolAction>,
    val results: kotlin.collections.List<ProtocolResult>,
    val parent: Protocol
) {

    /**
    * 
    * Values: received,rejected,replaced,ready,approved,publishing,published
    */
    enum class Status(val value: kotlin.String){
    
        @Json(name = "received") received("received"),
    
        @Json(name = "rejected") rejected("rejected"),
    
        @Json(name = "replaced") replaced("replaced"),
    
        @Json(name = "ready") ready("ready"),
    
        @Json(name = "approved") approved("approved"),
    
        @Json(name = "publishing") publishing("publishing"),
    
        @Json(name = "published") published("published");
    
    }

}

