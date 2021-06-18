package bg.dabulgaria.tibroish.presentation.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import bg.dabulgaria.tibroish.R
import bg.dabulgaria.tibroish.infrastructure.di.annotations.AppContext
import bg.dabulgaria.tibroish.presentation.providers.IResourceProvider
import javax.inject.Inject


interface IChannelInitializer{

    fun initChannels()
}

class ChannelInitializer @Inject constructor(@AppContext private val context: Context,
                                             private val resourceProvider: IResourceProvider) :IChannelInitializer {

    override fun initChannels() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
            return

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
                ?: return

        initPrimaryChannel(manager)

        initImportantChannel(manager)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initPrimaryChannel(manager: NotificationManager){

        val defaultSoundUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val attributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build()

        val channelId = context.getString(R.string.primary_notification_channel)
        val channelName = context.getString(R.string.primary_notification_channel_name)
        val chan1 = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
        chan1.lightColor = resourceProvider.getColor(R.color.colorPrimary)
        chan1.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        chan1.enableVibration(true)
        chan1.enableLights(true)
        chan1.setShowBadge(true)
        chan1.setSound(defaultSoundUri, attributes)
        manager.createNotificationChannel(chan1)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initImportantChannel(manager: NotificationManager){

        val defaultSoundUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val attributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build()

        val channelId = context.getString(R.string.important_notification_channel)
        val channelName = context.getString(R.string.important_notification_channel_name)
        val chan1 = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
        chan1.lightColor = resourceProvider.getColor(R.color.colorPrimary)
        chan1.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        chan1.enableVibration(true)
        chan1.enableLights(true)
        chan1.setShowBadge(true)
        chan1.setSound(defaultSoundUri, attributes)
        manager.createNotificationChannel(chan1)
    }

}