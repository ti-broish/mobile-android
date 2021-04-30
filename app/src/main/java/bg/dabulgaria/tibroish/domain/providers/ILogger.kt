package bg.dabulgaria.tibroish.domain.providers

import android.util.Log

interface ILogger {

    fun e( tag:String?, message: String?, throwable: Throwable? )

    fun e( tag:String?, throwable: Throwable? )

     fun i(tag: String?, message: String?)

    fun d(tag: String?, message: String?)
}