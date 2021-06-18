package bg.dabulgaria.tibroish.infrastructure.services

import bg.dabulgaria.tibroish.presentation.push.IPushActionRouter
import bg.dabulgaria.tibroish.presentation.push.IPushTokenSender
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.android.AndroidInjection
import javax.inject.Inject


class TiBroishMessagingService : FirebaseMessagingService() {

    @Inject
    protected lateinit var pushActionRouter: IPushActionRouter
    @Inject
    protected lateinit var pushTokenSender: IPushTokenSender

    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        pushActionRouter.onMessageReceived(message)
    }

    override fun onNewToken(newToken: String) {
        super.onNewToken(newToken)

        pushTokenSender.sendPushToken(newToken)
    }
}