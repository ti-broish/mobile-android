package bg.dabulgaria.tibroish.domain.protocol

import android.content.Context
import android.content.Intent

interface IProtocolSenderController {
    fun upload(metadata: ProtocolMetadata): Pair<String,Long>

    fun getIntent(context: Context, serverAndDbIds: Pair<String,Long>): Intent
}