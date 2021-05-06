package bg.dabulgaria.tibroish.domain.user

interface IUserRemoteRepository {

    fun getUser(): User?

    fun logout()
}