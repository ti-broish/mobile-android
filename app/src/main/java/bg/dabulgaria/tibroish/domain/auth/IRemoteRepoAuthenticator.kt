package bg.dabulgaria.tibroish.domain.auth

import bg.dabulgaria.tibroish.domain.providers.ILogger
import javax.inject.Inject
import kotlin.jvm.Throws

interface IRemoteRepoAuthenticator {

    @Throws(Exception::class)
    fun <P, V> executeCall(authenticatedRepoCall: (param: P, token: String) -> V, parameter: P): V?

    @Throws(Exception::class)
    fun <V> executeCall(authenticatedRepoCall: (token: String) -> V): V?
}

class RemoteRepoAuthenticator @Inject constructor( private val authRepository: IAuthRepository,
                                                   private val logger: ILogger)
    :IRemoteRepoAuthenticator{

    @Throws(Exception::class)
    override fun <P, V> executeCall(authenticatedRepoCall: (param: P, authorization: String) -> V, parameter: P): V? {

        try {

            val token = authRepository.token

            if (token.isEmpty())
                return null

            return authenticatedRepoCall(parameter, token)
        }
        catch (ex: Exception) {

            logger.e(TAG, ex)
            throw ex
        }
    }

    @Throws(Exception::class)
    override fun <V> executeCall(authenticatedRepoCall: (token: String) -> V): V? {

        try {

            val token = authRepository.token

            if (token.isEmpty())
                return null

            return authenticatedRepoCall( token)
        }
        catch (ex: Exception) {

            logger.e(TAG, ex)
            throw ex
        }
    }

    companion object{
        val TAG = RemoteRepoAuthenticator::class.java.simpleName
    }
}