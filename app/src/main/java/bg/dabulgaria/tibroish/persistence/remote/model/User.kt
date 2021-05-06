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

import bg.dabulgaria.tibroish.domain.organisation.Organization

import com.squareup.moshi.Json
/**
 * 
 * @param id 
 * @param firstName 
 * @param lastName 
 * @param email 
 * @param phone 
 * @param pin 
 * @param organization 
 * @param pictures 
 * @param firebaseUid 
 * @param hasAgreedToKeepData 
 * @param isEmailVerified 
 * @param roles 
 * @param clients 
 * @param assignedProtocols 
 * @param assignedViolations 
 * @param stream 
 * @param section 
 * @param registeredAt 
 */
data class User (
        val id: kotlin.String,
        val firstName: kotlin.String,
        val lastName: kotlin.String,
        val email: kotlin.String,
        val phone: kotlin.String,
        val pin: kotlin.String,
        val organization: Organization,
        val pictures: kotlin.collections.List<Picture>,
        val firebaseUid: kotlin.String,
        val hasAgreedToKeepData: kotlin.Boolean,
        val isEmailVerified: kotlin.Boolean,
        val roles: User.Roles,
        val clients: kotlin.collections.List<Client>,
        val assignedProtocols: kotlin.collections.List<Protocol>,
        val assignedViolations: kotlin.collections.List<Violation>,
        val stream: Stream,
        val section: Section,
        val registeredAt: java.time.OffsetDateTime
) {

    /**
    * 
    * Values: user,validator,externalMinusValidator,lawyer,streamer,admin
    */
    enum class Roles(val value: kotlin.collections.List<kotlin.String>){
    
        @Json(name = "user") user(listOf("user")),
    
        @Json(name = "validator") validator(listOf("validator")),
    
        @Json(name = "external-validator") externalMinusValidator(listOf("external-validator")),
    
        @Json(name = "lawyer") lawyer(listOf("lawyer")),

        @Json(name = "streamer") streamer(listOf("streamer")),

        @Json(name = "admin") admin(listOf("admin"))
    
    }

}

