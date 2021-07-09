package bg.dabulgaria.tibroish.domain.auth

interface IAuthRepository {

    var token:String

    var fcmPushToken:String?
}