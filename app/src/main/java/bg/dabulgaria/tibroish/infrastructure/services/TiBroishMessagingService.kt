package bg.dabulgaria.tibroish.infrastructure.services

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import androidx.core.app.NotificationCompat
import bg.dabulgaria.tibroish.R
import bg.dabulgaria.tibroish.presentation.main.MainActivity
import bg.dabulgaria.tibroish.presentation.providers.IResourceProvider
import bg.dabulgaria.tibroish.presentation.push.IPushTokenSender
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.android.AndroidInjection
import javax.inject.Inject


class TiBroishMessagingService : FirebaseMessagingService() {

    @Inject
    protected lateinit var pushTokenSender: IPushTokenSender

    @Inject
    protected lateinit var resourceProvider: IResourceProvider

    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        showNotification(message)
    }

    override fun onNewToken(newToken: String) {
        super.onNewToken(newToken)

        pushTokenSender.sendPushToken(newToken)
    }

    private fun showNotification(message: RemoteMessage) {

        if(message.notification?.title.isNullOrEmpty()
                && message.notification?.body.isNullOrEmpty())
            return

        val channel = this.getString(R.string.primary_notification_channel)
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        for(key in message.data.keys.orEmpty())
            intent.putExtra(key, message.data[key] )

        val pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT)

        val defaultSoundUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notificationBuilder = NotificationCompat.Builder(this, channel)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(message.notification?.title ?: "")
                .setColor(resourceProvider.getColor(R.color.colorPrimary))
                .setContentText(message.notification?.body ?: "")
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

        notificationManager?.notify(0 /* ID of notification */, notificationBuilder.build())
    }
}