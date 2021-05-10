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

import bg.dabulgaria.tibroish.persistence.remote.model.Party
import bg.dabulgaria.tibroish.persistence.remote.model.Protocol

/**
 * 
 * @param id 
 * @param protocol 
 * @param party 
 * @param validVotesCount 
 * @param machineVotesCount 
 * @param nonMachineVotesCount 
 * @param invalidVotesCount 
 * @param createdAt 
 */
data class ProtocolResult (
    val id: kotlin.String,
    val protocol: Protocol,
    val party: Party,
    val validVotesCount: java.math.BigDecimal,
    val machineVotesCount: java.math.BigDecimal,
    val nonMachineVotesCount: java.math.BigDecimal,
    val invalidVotesCount: java.math.BigDecimal,
    val createdAt: java.time.OffsetDateTime
) {

}
