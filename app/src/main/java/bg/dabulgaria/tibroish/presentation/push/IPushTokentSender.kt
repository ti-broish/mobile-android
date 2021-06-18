package bg.dabulgaria.tibroish.presentation.push

import bg.dabulgaria.tibroish.domain.organisation.ITiBroishRemoteRepository
import bg.dabulgaria.tibroish.domain.providers.ILogger
import bg.dabulgaria.tibroish.domain.push.SendTokenRequest
import bg.dabulgaria.tibroish.infrastructure.schedulers.ISchedulersProvider
import bg.dabulgaria.tibroish.persistence.local.AuthRepository
import com.google.firebase.messaging.FirebaseMessaging
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

interface IPushTokenSender {

    fun initPushToken()

    fun sendPushToken()

    fun sendPushToken(newToken: String)
}

class PushTokenSender @Inject constructor(private val schedulersProvider: ISchedulersProvider,
                                          private val logger: ILogger,
                                          private val authRepository: AuthRepository,
                                          private val tiBroishRemoteRepo: ITiBroishRemoteRepository ): IPushTokenSender {

    override fun initPushToken() {

        FirebaseMessaging.getInstance().token.addOnCompleteListener {

            if(!it.isSuccessful){

                logger.e(TAG,"Fetching FCM registration token failed", it.exception)
                return@addOnCompleteListener
            }

            val fcmToken = it.result?:""

            Single.fromCallable {

                val oldToken = authRepository.fcmPushToken
                if(oldToken == null ) {

                    authRepository.fcmPushToken = fcmToken
                    sendFCMToken()
                }
                return@fromCallable 0
            }
                    .subscribeOn(schedulersProvider.ioScheduler())
                    .subscribe( {
                        logger.i(TAG,"fcmToken saved.")
                    }, { throwable ->
                        logger.e(TAG,"fcmToken save FAILED", throwable)
                    })
        }
    }

    override fun sendPushToken(newToken: String) {

        authRepository.fcmPushToken = newToken
        sendPushToken()
    }

    override fun sendPushToken() {

        Single.fromCallable { sendFCMToken() }
                .subscribeOn(schedulersProvider.ioScheduler())
                .subscribe( {
                    logger.i(TAG,"sendFCMToken() success. ${it}")
                }, { throwable ->
                    logger.e(TAG,"sendFCMToken() FAILED", throwable)
                })
    }

    private fun sendFCMToken():Int{

        val fcmToken = authRepository.fcmPushToken
        val userToken = authRepository.token

        if(fcmToken.isNullOrEmpty() || userToken.isEmpty())
            return 0

        val response = tiBroishRemoteRepo.sendFCMToken(SendTokenRequest(fcmToken))

        logger.i(TAG,"sendFCMToken():I success. ${response.data}")
        authRepository.fcmPushToken = ""

        return 0
    }

    companion object{
        val TAG: String = PushTokenSender::class.java.simpleName
    }
}