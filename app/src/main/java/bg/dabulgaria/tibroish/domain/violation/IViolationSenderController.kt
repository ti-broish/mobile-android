package bg.dabulgaria.tibroish.domain.violation

interface IViolationSenderController {
    fun upload(metadata: ViolationMetadata)
}