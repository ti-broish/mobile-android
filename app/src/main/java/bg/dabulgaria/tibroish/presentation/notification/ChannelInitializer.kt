package bg.dabulgaria.tibroish.presentation.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import bg.dabulgaria.tibroish.R
import bg.dabulgaria.tibroish.infrastructure.di.annotations.AppContext
import javax.inject.Inject

interface IChannelInitializer{

    fun initChannels()
}

class ChannelInitializer @Inject constructor(@AppContext private val context: Context) :IChannelInitializer {

    override fun initChannels() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
            return

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
                ?: return

        initPrimaryChannel(manager)

        initImportantChannel(manager)
    }

    private fun initPrimaryChannel(manager: NotificationManager){

        val channelId = context.getString(R.string.primary_notification_channel)
        val channelName = context.getString(R.string.primary_notification_channel_name)
        val chan1 = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW)
        chan1.lightColor = Color.GREEN
        chan1.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        chan1.enableVibration(false)
        chan1.enableLights(true)
        chan1.setShowBadge(false)
        manager.createNotificationChannel(chan1)
    }

    private fun initImportantChannel(manager: NotificationManager){

        val channelId = context.getString(R.string.important_notification_channel)
        val channelName = context.getString(R.string.important_notification_channel_name)
        val chan1 = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
        chan1.lightColor = Color.GREEN
        chan1.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        chan1.enableVibration(true)
        chan1.enableLights(true)
        chan1.setShowBadge(false)
        manager.createNotificationChannel(chan1)
    }

}