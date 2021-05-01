package bg.dabulgaria.tibroish.domain.providers

import android.util.Log

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