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


/**
 * 
 * @param id 
 * @param name 
 * @param displayName 
 * @param isFeatured 
 * @param color 
 */
data class PartyDto (
    val id: java.math.BigDecimal,
    val name: kotlin.String,
    val displayName: kotlin.String,
    val isFeatured: kotlin.Boolean,
    val color: kotlin.String
) {

}
