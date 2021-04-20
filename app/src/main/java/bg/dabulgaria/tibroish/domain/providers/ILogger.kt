package bg.dabulgaria.tibroish.domain.providers

interface ILogger {

    fun e( tag:String?, message: String?, throwable: Throwable? )
}