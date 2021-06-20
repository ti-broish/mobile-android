package bg.dabulgaria.tibroish.domain.image

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Parcelable
import androidx.core.app.JobIntentService
import bg.dabulgaria.tibroish.domain.protocol.IProtocolSenderController
import bg.dabulgaria.tibroish.domain.protocol.ProtocolMetadata
import bg.dabulgaria.tibroish.domain.providers.ILogger
import bg.dabulgaria.tibroish.domain.violation.IViolationSenderController
import bg.dabulgaria.tibroish.domain.violation.ViolationMetadata
import dagger.android.AndroidInjection
import java.util.concurrent.CountDownLatch
import javax.inject.Inject

class UploaderService : JobIntentService() {

    @Inject
    lateinit var protocolSenderController: IProtocolSenderController

    @Inject
    lateinit var violationSenderController: IViolationSenderController

    @Inject
    lateinit var logger: ILogger

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
                UploaderService::class.java,
                UPLOAD_IMAGE_JOB_ID,
                startJobIntent)
            try {
                latch.await()
            } catch (e: InterruptedException) {

            }
        }

        private const val UPLOAD_IMAGE_JOB_ID = 1;
        private const val EXTRA_UPLOAD_INFO = "extra_upload_info"
        private var TAG = UploaderService::class.java.canonicalName
        private val ACTION_UPLOAD_COMPLETE = "action_upload_complete"
        private val EXTRA_UPLOAD_DATA_ID = "extra_upload_data_id"
    }

    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()
    }

    override fun onHandleWork(intent: Intent) {
        val uploadInfo: UploadInfo = intent.getParcelableExtra(EXTRA_UPLOAD_INFO) ?: return
        logger.i(TAG, "Background service started for upload $uploadInfo")
        val uploadId: Long;
        when (uploadInfo.uploadType) {
            UploadType.VIOLATION -> {
                val metadata: ViolationMetadata = uploadInfo.metadata as ViolationMetadata
                uploadId = metadata.violationId
                violationSenderController.upload(metadata)
            }
            UploadType.PROTOCOL -> {
                val metadata: ProtocolMetadata = uploadInfo.metadata as ProtocolMetadata
                uploadId = metadata.protocolId
                protocolSenderController.upload(metadata)
            }
        }
        val resultIntent = Intent(ACTION_UPLOAD_COMPLETE)
        resultIntent.putExtra(EXTRA_UPLOAD_DATA_ID, uploadId)
        applicationContext.sendBroadcast(resultIntent)
        logger.i(TAG, "Upload job completed $uploadInfo")
    }
}