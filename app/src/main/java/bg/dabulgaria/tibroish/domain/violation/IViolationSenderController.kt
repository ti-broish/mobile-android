package bg.dabulgaria.tibroish.domain.violation

import android.content.Context
import android.content.Intent

interface IViolationSenderController {
    fun upload(metadata: ViolationMetadata): String

    fun getIntent(context: Context, violationServerId: String): Intent
}