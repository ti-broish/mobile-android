package bg.dabulgaria.tibroish.domain.violation

import android.content.Context
import android.content.Intent

interface IViolationSenderController {
    fun upload(metadata: ViolationMetadata): Pair<String,Long>

    fun getIntent(context: Context, serverAndDbIds: Pair<String,Long>): Intent
}