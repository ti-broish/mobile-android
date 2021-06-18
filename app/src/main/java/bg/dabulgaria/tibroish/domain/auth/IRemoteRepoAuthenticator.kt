package bg.dabulgaria.tibroish.domain.auth

import bg.dabulgaria.tibroish.domain.providers.ILogger
import bg.dabulgaria.tibroish.persistence.remote.AuthException
import bg.dabulgaria.tibroish.presentation.main.IMainRouter
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject
import kotlin.jvm.Throws

interface IRemoteRepoAuthenticator {

    @Throws(Exception::class)
    fun <P, V> executeCall(authenticatedRepoCall: (param: P, token: String) -> Call<V>, parameter: P): V?

    @Throws(Exception::class)
    fun <V> executeCall(authenticatedRepoCall: (token: String) -> Call<V>): V?
}

class RemoteRepoAuthenticator @Inject constructor(private val authRepo: IAuthRepository,
                                                  private val logger: ILogger,
                                                  private val mainRouter :IMainRouter)
    :IRemoteRepoAuthenticator{

    private val syncObject = Object()
    private var refreshTokenStarted = AtomicBoolean(false)


    @Throws(Exception::class)
    override fun <P, V> executeCall(authenticatedRepoCall: (param: P, authorization: String) ->Call<V>,
                                    parameter: P): V? {

        var result :V? = null
        for( i in 0 until NUM_RETRIES) {

            result = executeCallInt( authenticatedRepoCall, parameter )
            if( result != null )
                break
        }

        return result
    }

    @Throws(Exception::class)
    override fun <V> executeCall(authenticatedRepoCall: (token: String) -> Call<V>): V? {

        var result :V? = null
        for( i in 0 until NUM_RETRIES) {

            result = executeCallInt( authenticatedRepoCall )
            if( result != null )
                break
        }

        return result
    }

    //region private methods
    private fun <P, V> executeCallInt(authenticatedRepoCall: (param: P, authorization: String) ->Call<V>,
                           parameter: P): V?{

        try {

            val token = authRepo.token

            if (token.isEmpty())
                return null

            return authenticatedRepoCall(parameter, token).execute().body()
        }
        catch (authEx: AuthException){

            val token = refreshToken()
            if( token.isEmpty())
                throw LogoutException()
        }
        catch (ex: Exception) {

            logger.e(TAG, ex)
            throw ex
        }
        return null
    }

    private fun <V> executeCallInt(authenticatedRepoCall: (token: String) -> Call<V>): V? {

        try {

            val token = authRepo.token

            if (token.isEmpty())
                return null

            return authenticatedRepoCall(token).execute().body()
        }
        catch (authEx: AuthException){

            val token = refreshToken()
            if( token.isEmpty())
                throw LogoutException()
        }
        catch (ex: Exception) {

            logger.e(TAG, ex)
            throw ex
        }

        return null
    }

    /**
     * Retrieves a fresh token for api calls
     * Has internal synchronization, only one thread starts the token refresh process
     * @return The refreshed token on success and empty string on failure
     */
    private fun refreshToken() :String{

        if (refreshTokenStarted.get()) {

            try {

                synchronized(syncObject) {

                    syncObject.wait()
                }
            } catch (ex: Exception) { }

            return authRepo.token
        }

        refreshTokenStarted.set(true)

        val task = FirebaseAuth.getInstance().currentUser.getIdToken(true)

        try{

            val tokenResult = Tasks.await(task)
            val token = tokenResult.token?:""
            authRepo.token = token

            return token
        }
        catch (th: Throwable){

            authRepo.token = ""
            logger.e(TAG, th)
            mainRouter.onAuthEvent()
        }
        finally {
            refreshTokenStarted.set(false)
            synchronized(syncObject) { syncObject.notifyAll() }
        }

        return ""
    }
    //endregion private methods

    companion object{

        val TAG :String = RemoteRepoAuthenticator::class.java.simpleName
        const val NUM_RETRIES =2
    }
}