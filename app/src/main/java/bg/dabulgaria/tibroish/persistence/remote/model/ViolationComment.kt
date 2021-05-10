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

import bg.dabulgaria.tibroish.persistence.remote.model.User
import bg.dabulgaria.tibroish.persistence.remote.model.Violation

/**
 * 
 * @param id 
 * @param text 
 * @param violation 
 * @param author 
 * @param createdAt 
 */
data class ViolationComment (
    val id: kotlin.String,
    val text: kotlin.String,
    val violation: Violation,
    val author: User,
    val createdAt: java.time.OffsetDateTime
) {

}
