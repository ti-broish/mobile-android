package bg.dabulgaria.tibroish.live

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import bg.dabulgaria.tibroish.R
import bg.dabulgaria.tibroish.live.model.UserStreamModel
import bg.dabulgaria.tibroish.live.utils.makeFullScreen
import bg.dabulgaria.tibroish.databinding.ActivityPrepareToStreamBinding

class PrepareToStreamActivity : AppCompatActivity() {


    private lateinit var userStream: UserStreamModel
    private lateinit var binding: ActivityPrepareToStreamBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        makeFullScreen()
        setContentView(R.layout.activity_prepare_to_stream)

        resolveIntent()

        binding.continueButton.setOnClickListener() {
            BroadcastActivity.startWithParameters(this@PrepareToStreamActivity, userStream)
            finish()
        }
    }

    private fun resolveIntent() {
        this.userStream = intent.extras?.getParcelable(USER_STREAM_MODEL_EXTRA)
                ?: throw IllegalArgumentException("Use start with parameters to start activity")
    }

    companion object {
        private const val USER_STREAM_MODEL_EXTRA =
                "bg.dabulgaria.tibroish.stream.PrepareToStreamActivity.USER_STREAM_MODEL_EXTRA"

        fun startWithParameters(context: Context, userStream: UserStreamModel) {
            val intent = Intent(context, PrepareToStreamActivity::class.java).apply {
                putExtra(USER_STREAM_MODEL_EXTRA, userStream)
            }

            context.startActivity(intent)
        }
    }
}
