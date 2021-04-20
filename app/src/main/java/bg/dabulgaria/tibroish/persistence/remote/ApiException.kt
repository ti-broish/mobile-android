package bg.dabulgaria.tibroish.persistence.remote

import okhttp3.Response
import java.lang.Exception

class ApiException(val response: Response, val responseData:String) :Exception() {
}