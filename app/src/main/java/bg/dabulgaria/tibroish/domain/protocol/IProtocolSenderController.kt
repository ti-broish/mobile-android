package bg.dabulgaria.tibroish.domain.protocol

import android.content.Context
import android.content.Intent

interface IProtocolSenderController {
    fun upload(metadata: ProtocolMetadata): String

    fun getIntent(context: Context, protocolServerId: String): Intent
}