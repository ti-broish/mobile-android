package bg.dabulgaria.tibroish.domain.image

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.IBinder
import android.os.Parcelable
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import bg.dabulgaria.tibroish.R
import bg.dabulgaria.tibroish.domain.protocol.IProtocolSenderController
import bg.dabulgaria.tibroish.domain.protocol.ProtocolMetadata
import bg.dabulgaria.tibroish.domain.providers.ILogger
import bg.dabulgaria.tibroish.domain.violation.IViolationSenderController
import bg.dabulgaria.tibroish.domain.violation.ViolationMetadata
import bg.dabulgaria.tibroish.presentation.providers.IResourceProvider
import dagger.android.AndroidInjection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.CountDownLatch
import javax.inject.Inject

class UploaderService : Service() {

    @Inject
    lateinit var protocolSenderController: IProtocolSenderController

    @Inject
    lateinit var violationSenderController: IViolationSenderController

    @Inject
    lateinit var logger: ILogger

    @Inject
    lateinit var resourceProvider : IResourceProvider

    companion object {
        fun uploadViolation(context: Context, metadata: ViolationMetadata) {
            uploadImpl(context, UploadType.VIOLATION, metadata, metadata.violationId)
        }

        fun uploadProtocol(context: Context, metadata: ProtocolMetadata) {
            uploadImpl(context, UploadType.PROTOCOL, metadata, metadata.protocolId)
        }

        private fun uploadImpl(
            context: Context,
            uploadType: UploadType,
            metadata: Parcelable,
            dataId: Long) {

            val startJobIntent = Intent(context, UploaderService::class.java)
            startJobIntent.putExtra(EXTRA_UPLOAD_INFO, UploadInfo(uploadType, metadata))

            val latch = CountDownLatch(1)
            context.registerReceiver(object : BroadcastReceiver() {
                    override fun onReceive(context: Context?, intent: Intent?) {
                        if (intent == null) {
                            return;
                        }
                        if (intent.getLongExtra(EXTRA_UPLOAD_DATA_ID, -1) != dataId) {
                            return
                        }
                        latch.countDown()
                        context?.unregisterReceiver(this)
                    }
                },
                IntentFilter(ACTION_UPLOAD_COMPLETE)
            )
            enqueueWork(
                context,
                startJobIntent)
            try {
                latch.await()
            } catch (e: InterruptedException) {

            }
        }

        private fun enqueueWork(
            context: Context,
            startServiceIntent: Intent
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(startServiceIntent)
            } else {
                context.startService(startServiceIntent)
            }
        }

        private const val EXTRA_UPLOAD_INFO = "extra_upload_info"
        private var TAG = UploaderService::class.java.canonicalName
        private val ACTION_UPLOAD_COMPLETE = "action_upload_complete"
        private val EXTRA_UPLOAD_DATA_ID = "extra_upload_data_id"
        private val PROGRESS_NOTIFICATION_ID = 1
        private val DONE_NOTIFICATION_ID = 2
        private val NOTIFICATION_CHANNEL_ID = "uploader_channel"
    }

    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val uploadInfo: UploadInfo? = intent!!.getParcelableExtra(EXTRA_UPLOAD_INFO)
        if (uploadInfo == null) {
            stopSelf()
            return START_REDELIVER_INTENT
        }
        startForeground(PROGRESS_NOTIFICATION_ID, createProgressNotification(uploadInfo.uploadType))
        CoroutineScope(Dispatchers.IO).launch {
            onHandleWork(uploadInfo)
            stopSelf()
        }
        return START_REDELIVER_INTENT
    }

    private fun createProgressNotification(uploadType: UploadType): Notification {
        val contentTitle = when (uploadType) {
            UploadType.PROTOCOL -> {
                resourceProvider.getString(R.string.protocol_upload_publishing)
            }
            UploadType.VIOLATION -> {
                resourceProvider.getString(R.string.violation_upload_publishing)
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                resourceProvider.getString(R.string.upload_notification_channel_name))
        }
        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setColor(resourceProvider.getColor(R.color.colorPrimary))
            .setContentTitle(contentTitle)
            .setProgress(
                /* max= */ 0,
                /* progress= */ 0,
                /* indeterminate= */true)
            .build()
    }

    private fun createDoneNotification(uploadType: UploadType, intent: Intent): Notification {
        val contentTitle = when (uploadType) {
            UploadType.PROTOCOL -> {
                resourceProvider.getString(R.string.protocol_upload_published)
            }
            UploadType.VIOLATION -> {
                resourceProvider.getString(R.string.violation_upload_published)
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                resourceProvider.getString(R.string.upload_notification_channel_name))
        }
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setColor(resourceProvider.getColor(R.color.colorPrimary))
            .setContentTitle(contentTitle)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
    }

    private fun onHandleWork(uploadInfo: UploadInfo) {
        logger.i(TAG, "Background service started for upload $uploadInfo")
        val uploadId: Long;
        val intent: Intent;
        when (uploadInfo.uploadType) {
            UploadType.VIOLATION -> {
                val metadata: ViolationMetadata = uploadInfo.metadata as ViolationMetadata
                uploadId = metadata.violationId
                val violationServerId = violationSenderController.upload(metadata)
                intent = violationSenderController.getIntent(this, violationServerId)
            }
            UploadType.PROTOCOL -> {
                val metadata: ProtocolMetadata = uploadInfo.metadata as ProtocolMetadata
                uploadId = metadata.protocolId
                val protocolServerId = protocolSenderController.upload(metadata)
                intent = protocolSenderController.getIntent(this, protocolServerId)
            }
        }
        val doneNotification = createDoneNotification(uploadInfo.uploadType, intent)
        with(NotificationManagerCompat.from(this)) {
            notify(DONE_NOTIFICATION_ID, doneNotification)
        }
        val resultIntent = Intent(ACTION_UPLOAD_COMPLETE)
        resultIntent.putExtra(EXTRA_UPLOAD_DATA_ID, uploadId)
        applicationContext.sendBroadcast(resultIntent)
        logger.i(TAG, "Upload job completed $uploadInfo")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelId: String, channelName: String) {
        val channel = NotificationChannel(channelId,
            channelName, NotificationManager.IMPORTANCE_LOW)
        val service = getSystemService(NotificationManager::class.java)
        service.createNotificationChannel(channel)
    }
}