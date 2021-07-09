package bg.dabulgaria.tibroish.persistence.local

import android.util.Log
import bg.dabulgaria.tibroish.domain.providers.ILogger

class Logger : ILogger {

    override fun e(tag: String?, message: String?, throwable: Throwable?) {
        Log.e(tag, message, throwable)
    }

    override fun e( tag:String?, throwable: Throwable? ){
        Log.e(tag, throwable?.message, throwable)
    }

    override fun i(tag: String?, message: String?) {
        Log.i(tag, message ?:"")
    }

    override fun d(tag: String?, message: String?) {
        Log.d(tag, message?:"")
    }
}