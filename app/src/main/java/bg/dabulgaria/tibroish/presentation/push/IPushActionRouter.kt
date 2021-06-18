package bg.dabulgaria.tibroish.presentation.push

import android.content.Intent
import bg.dabulgaria.tibroish.domain.providers.ILogger
import com.google.firebase.messaging.RemoteMessage
import javax.inject.Inject


interface IPushActionRouter {

    fun onMessageReceived(message: RemoteMessage)

    fun onIntent(intent: Intent)
}

class PushActionRouter @Inject constructor(private val logger: ILogger) :IPushActionRouter{


    override fun onMessageReceived(message: RemoteMessage) {

        val title = message.notification?.title
        val messageText = message.notification?.body

        logger.i(TAG,"onMessageReceived title: '$title', message: '$messageText'")

        for(key in message.data?.keys.orEmpty())
            logger.i(TAG,"key: $key, value: ${message.data?.get(key)?:""}")

        //TODO show dialog with action button
    }

    override fun onIntent(intent: Intent) {

        //TODO do action
    }

    companion object{
        
        val TAG = PushActionRouter::class.java.simpleName

        val show_screen_key = "show_screen"
        val entity_id_key = "entity_id"
        val error_message_key = "error_message"
    }
}
