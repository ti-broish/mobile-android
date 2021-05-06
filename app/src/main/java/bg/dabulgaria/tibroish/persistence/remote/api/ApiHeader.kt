package bg.dabulgaria.tibroish.persistence.remote.api

object ApiHeader {

    const val Authorization = "Authorization"

    const val AcceptLanguage = "Accept-Language"

    const val AcceptEncoding = "AcceptEncoding"

    const val ContentType = "Content-Type"

    const val UserAgent = "User-Agent"

    const val Accept = "accept"
}

enum class AcceptValues (val value:String) {
    ApplicationJson("application/json")
}