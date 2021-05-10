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

/**
 * 
 * @param id 
 * @param path 
 * @param sortPosition 
 * @param rotation 
 * @param createdAt 
 * @param author 
 */
data class Picture (
    val id: kotlin.String,
    val path: kotlin.String,
    val sortPosition: java.math.BigDecimal,
    val rotation: java.math.BigDecimal,
    val createdAt: java.time.OffsetDateTime,
    val author: User
) {

}
