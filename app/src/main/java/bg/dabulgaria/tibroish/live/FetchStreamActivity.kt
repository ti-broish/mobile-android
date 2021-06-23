package bg.dabulgaria.tibroish.live

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import bg.dabulgaria.tibroish.R
import bg.dabulgaria.tibroish.live.model.UserStreamModel
import bg.dabulgaria.tibroish.live.utils.LoginActivityLoader
import bg.dabulgaria.tibroish.live.utils.makeFullScreen
import bg.dabulgaria.tibroish.presentation.base.BaseActivity
import com.google.android.material.button.MaterialButton
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import kotlinx.android.synthetic.main.activity_fetch_stream.*
import javax.inject.Inject
import kotlin.concurrent.thread

private const val BUTTON_COOLDOWN = 1000L

class FetchStreamActivity : BaseActivity(), HasAndroidInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    @Inject
    lateinit var loginActivityLoader: LoginActivityLoader

//    @Inject
//    lateinit var streamService: StreamService

    private lateinit var progressBarView: ProgressBar
    private lateinit var disabledExplanationContainer: ConstraintLayout
    private lateinit var disabledExplanationScrollView: ScrollView
    private lateinit var logoutButtonView: MaterialButton
    private lateinit var streamingDisabledJustificationView: TextView
    private lateinit var tryAgainButtonView: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        makeFullScreen()
        setContentView(R.layout.activity_fetch_stream)

        progressBarView = streaming_disabled_progress_bar
        disabledExplanationContainer = disabled_explanation_container
        disabledExplanationScrollView = disabled_explanation_scroll_view
        logoutButtonView = logout_button
        streamingDisabledJustificationView = streaming_disabled_message_id
        tryAgainButtonView = try_again_button

        logoutButtonView.setOnClickListener {
            loginActivityLoader.logout()
        }

        tryAgainButtonView.setOnClickListener {
            tryFetchingStream()
        }
    }

    override fun onResume() {
        super.onResume()

        tryFetchingStream()

    }

    override fun showNavigation(show: Boolean) {
    }

    override fun closeDrawer() {
    }

    override fun showProcessing(processing: Boolean) {
    }

    override fun showDismissableDialog(message: String, dismissCallback: () -> Unit) {
    }

    private fun tryFetchingStream() {
        showProgressBar()

        // TODO implement get user stream
        thread {
            Thread.sleep(5000)
            val userStream = UserStreamModel("rtmp://strm.ludost.net/st/streamtest1", "https://strm.ludost.net/hls/streamtest1.m3u8", true)
            PrepareToStreamActivity.startWithParameters(this@FetchStreamActivity, userStream)
            finish()
        }

//        streamService.getUserStream(this, { result: UserStreamModel ->
//            if (result.streamUrl != null) {
//                val userStream = UserStream(result.streamUrl, result.audioDisabled
//                        ?: true, result.viewUrl)
//                PrepareToStreamActivity.startWithParameters(this@FetchStreamActivity, userStream)
//                finish()
//            } else {
//                hideProgressBar()
//                streamingDisabledJustificationView.text = getString(R.string.streaming_disabled_other_error_message)
//                disabledExplanationScrollView.setBackgroundResource(R.drawable.background_warn)
//            }
//        }, onError = object : GetStreamErrorHandler {
//            override fun onNoConnectivity() {
//                hideProgressBar()
//                streamingDisabledJustificationView.text = getString(R.string.streaming_disabled_no_internet_message)
//                disabledExplanationScrollView.setBackgroundResource(R.drawable.background_warn)
//            }
//
//            override fun onStreamingNotAllowed() {
//                hideProgressBar()
//                streamingDisabledJustificationView.text = getString(R.string.streaming_not_yet_possible_message)
//                disabledExplanationScrollView.setBackgroundResource(R.drawable.background_clock)
//            }
//
//            override fun onOtherError() {
//                hideProgressBar()
//                streamingDisabledJustificationView.text = getString(R.string.streaming_disabled_other_error_message)
//                disabledExplanationScrollView.setBackgroundResource(R.drawable.background_warn)
//            }
//
//        })
    }

    private fun showProgressBar() {
        disabledExplanationContainer.visibility = View.GONE
        progressBarView.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        progressBarView.visibility = View.GONE
        disabledExplanationContainer.visibility = View.VISIBLE
    }

    override fun androidInjector(): AndroidInjector<Any> {
        return dispatchingAndroidInjector
    }
}
