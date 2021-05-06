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

import bg.dabulgaria.tibroish.persistence.remote.model.PictureDto
import bg.dabulgaria.tibroish.persistence.remote.model.SectionDto
import bg.dabulgaria.tibroish.persistence.remote.model.TownDto
import bg.dabulgaria.tibroish.persistence.remote.model.UserDto
import bg.dabulgaria.tibroish.persistence.remote.model.ViolationUpdateDto

import com.squareup.moshi.Json
/**
 * 
 * @param id 
 * @param town 
 * @param pictures 
 * @param description 
 * @param status 
 * @param isPublished 
 * @param assignees 
 * @param updates 
 * @param author 
 * @param section 
 */
data class ViolationDto (
    val id: kotlin.String,
    val town: TownDto,
    val pictures: kotlin.collections.List<PictureDto>,
    val description: kotlin.String,
    val status: ViolationDto.Status,
    val isPublished: kotlin.Boolean,
    val assignees: kotlin.collections.List<UserDto>,
    val updates: kotlin.collections.List<ViolationUpdateDto>,
    val author: UserDto,
    val section: SectionDto? = null
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

