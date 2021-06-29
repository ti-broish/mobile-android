package bg.dabulgaria.tibroish.persistence.remote

import okhttp3.Response
import java.io.IOException
import java.lang.Exception

class ApiException(val response: Response, val responseData:String, override val message: String?)
    :IOException()

class AuthException(val response: Response, val responseData:String, override val message: String?)
    : IOException()

class ApiConflictException(val response: Response, val responseData:String, override val message: String?)
    : IOException()
