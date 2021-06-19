package bg.dabulgaria.tibroish.domain.image

import android.content.Context
import android.content.Intent
import android.os.Parcelable
import androidx.core.app.JobIntentService
import bg.dabulgaria.tibroish.domain.protocol.IProtocolSenderController
import bg.dabulgaria.tibroish.domain.protocol.ProtocolMetadata
import bg.dabulgaria.tibroish.domain.providers.ILogger
import bg.dabulgaria.tibroish.domain.violation.IViolationSenderController
import bg.dabulgaria.tibroish.domain.violation.ViolationMetadata
import dagger.android.AndroidInjection
import javax.inject.Inject

class ImageUploaderService : JobIntentService() {

    @Inject
    lateinit var protocolSenderController: IProtocolSenderController

    @Inject
    lateinit var violationSenderController: IViolationSenderController

    @Inject
    lateinit var logger: ILogger

    companion object {
        fun enqueueViolation(context: Context, metadata: ViolationMetadata) {
            enqueueImpl(context, UploadType.VIOLATION, metadata)
        }

        fun enqueueProtocol(context: Context, metadata: ProtocolMetadata) {
            enqueueImpl(context, UploadType.PROTOCOL, metadata)
        }

        private fun enqueueImpl(context: Context, uploadType: UploadType, metadata: Parcelable) {
            val intent = Intent(context, ImageUploaderService::class.java)
            intent.putExtra(EXTRA_UPLOAD_INFO, UploadInfo(uploadType, metadata))
            enqueueWork(
                context,
                ImageUploaderService::class.java,
                UPLOAD_IMAGE_JOB_ID,
                intent)
        }

        private const val UPLOAD_IMAGE_JOB_ID = 1;
        private const val EXTRA_UPLOAD_INFO = "extra_upload_info"
        private var TAG = ImageUploaderService::class.java.canonicalName
    }

    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()
    }

    override fun onHandleWork(intent: Intent) {
        val uploadInfo: UploadInfo = intent.getParcelableExtra(EXTRA_UPLOAD_INFO) ?: return
        logger.i(TAG, "Background service started for upload $uploadInfo")

        when (uploadInfo.uploadType) {
            UploadType.VIOLATION -> {
                val metadata: ViolationMetadata = uploadInfo.metadata as ViolationMetadata
                violationSenderController.upload(metadata)
            }
            UploadType.PROTOCOL -> {
                val metadata: ProtocolMetadata = uploadInfo.metadata as ProtocolMetadata
                protocolSenderController.upload(metadata)
            }
        }
    }
}