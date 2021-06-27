package bg.dabulgaria.tibroish.live

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Camera
import android.media.MediaCodecInfo.CodecProfileLevel.AVCLevel4
import android.net.Uri
import android.os.*
import android.provider.Settings
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.app.ShareCompat
import androidx.core.content.ContextCompat
import bg.dabulgaria.tibroish.R
import bg.dabulgaria.tibroish.live.model.UserStreamModel
import bg.dabulgaria.tibroish.live.utils.*
import bg.dabulgaria.tibroish.presentation.base.BaseActivity
import com.pedro.encoder.input.video.CameraHelper
import com.pedro.encoder.utils.CodecUtil
import com.pedro.rtplibrary.rtmp.RtmpCamera1
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import kotlinx.android.synthetic.main.activity_broadcast.*
import net.ossrs.rtmp.ConnectCheckerRtmp
import java.util.*
import javax.inject.Inject
import kotlin.math.min


const val RECONNECT_ATTEMPT_INTERVAL = 15000L
private const val PERMISSIONS_REQUEST_CODE = 12345

class BroadcastActivity : BaseActivity(), ConnectCheckerRtmp, SurfaceHolder.Callback, HasAndroidInjector {

    @Inject
    lateinit var loginActivityLoader: LoginActivityLoader

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    private lateinit var userStream: UserStreamModel

    private var rtmpCamera1: RtmpCamera1? = null
    private var surfaceViewCreated = false
    private var havePermissions = false
    private var dialog: AlertDialog? = null
    private var refreshTimer: Handler? = null

    private val requiredPermissions = arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        setContentView(R.layout.activity_broadcast)


        resolveIntent()


        action_button.setOnClickListener {
            if (rtmpCamera1?.isStreaming == false) {
                startStream()
            } else {
                stopStream()
            }
        }
        overlay_in_call.setOnTouchListener { _, _ -> true }

        flashlight.setOnCheckedChangeListener { _, _ -> rtmpCamera1?.switchFlashLight() }

        more.setOnClickListener {
            showPopup(it)
        }
    }

    private fun initSurfaceView(): SurfaceView {
        surfaceViewCreated = false
        val videoStreamSurfaceView = SurfaceView(this)
        val cameraPreviewSizes = getScaledPreviewSizeBasedOnCamera()

        val layoutParameters = cameraPreviewSizes?.let { ConstraintLayout.LayoutParams(it.first, it.second) }
                ?: ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT)
        layoutParameters.startToStart = surfaceViewContainer.id
        layoutParameters.topToTop = surfaceViewContainer.id
        layoutParameters.endToEnd = surfaceViewContainer.id
        layoutParameters.bottomToBottom = surfaceViewContainer.id
        videoStreamSurfaceView.layoutParams = layoutParameters
        surfaceViewContainer.removeAllViews()
        videoStreamSurfaceView.holder.addCallback(this@BroadcastActivity)
        videoStreamSurfaceView.id = View.generateViewId()
        surfaceViewContainer.addView(videoStreamSurfaceView)

        return videoStreamSurfaceView
    }

    private fun startStream() {
        val quality = getQuality()
        if (quality != null) {
            if (prepareEncoders(quality) == true) {
                rtmpCamera1?.startStream(userStream.streamUrl)
                progress_bar.visibility = View.VISIBLE
            } else {
                Toast.makeText(this, R.string.stream_unrecoverable_error, Toast.LENGTH_LONG).show()
            }
        } else {
            noConnectivity()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (PERMISSIONS_REQUEST_CODE == requestCode) {
            var grantedCount = 0
            for (index in permissions.indices.reversed()) {
                val permission = permissions[index]
                if (grantResults[index] != PackageManager.PERMISSION_GRANTED) {
                    onPermissionNotGranted(permission)
                } else {
                    grantedCount++
                }
            }
            if (grantedCount == grantResults.size) {
                onAllPermissionsGranted()
            }
        }
    }

    override fun showNavigation(show: Boolean) {
    }

    override fun closeDrawer() {
    }

    override fun showProcessing(processing: Boolean) {
    }

    override fun showDismissableDialog(message: String, dismissCallback: () -> Unit) {
    }

    override fun onStart() {
        super.onStart()

        if (isCallActive()) {
            overlay_in_call.visibility = View.VISIBLE
        } else {
            overlay_in_call.visibility = View.GONE
            Handler(Looper.getMainLooper()).postDelayed({
                checkPermissions()
            }, 500L)

        }
    }

    private fun checkPermissions() {
        if (requiredPermissions.map { item -> ContextCompat.checkSelfPermission(this@BroadcastActivity, item) == PackageManager.PERMISSION_DENIED }.contains(true)) {
            // Requesting the permission
            ActivityCompat.requestPermissions(this@BroadcastActivity, requiredPermissions, PERMISSIONS_REQUEST_CODE)
        } else {
            onAllPermissionsGranted()
        }
    }

    override fun onStop() {
        super.onStop()
        refreshTimer?.removeCallbacksAndMessages(null)
        rtmpCamera1?.stopPreview()
    }

    private fun getQuality(): Quality? {
        return when (ConnectionUtil(this).getNetworkClass()) {
            NetworkClass.WIFI -> Quality.HIGH
            NetworkClass.NONE -> null
            NetworkClass.MOBILE_2G -> Quality.LOW
            NetworkClass.MOBILE_3G -> Quality.MEDIUM
            NetworkClass.MOBILE_4G -> Quality.HIGH
            NetworkClass.MOBILE_5G -> Quality.HIGH
        }
    }

    private fun stopStream() {
        action_button.setImageResource(R.drawable.ic_start_button)
        rtmpCamera1?.stopStream()

        live_marker.visibility = View.GONE
        counter.stop()
    }

    override fun onAuthSuccessRtmp() {
    }

    override fun onNewBitrateRtmp(bitrate: Long) {
    }

    override fun onConnectionSuccessRtmp() {
        runOnUiThread {
            action_button.setImageResource(R.drawable.ic_stop_button)
            refreshTimer?.removeCallbacksAndMessages(null)
            dialog?.dismiss()
            live_marker.visibility = View.VISIBLE
            counter.base = SystemClock.elapsedRealtime()
            counter.start()
            progress_bar.visibility = View.GONE
        }
    }

    override fun onConnectionFailedRtmp(reason: String) {
        runOnUiThread {
            live_marker.visibility = View.GONE
            action_button.setImageResource(R.drawable.ic_start_button)
            counter.stop()
            progress_bar.visibility = View.GONE
            openDisconnectedDialog()
        }

    }

    override fun onAuthErrorRtmp() {
    }

    override fun onDisconnectRtmp() {
        runOnUiThread {
            live_marker.visibility = View.GONE
            action_button.setImageResource(R.drawable.ic_start_button)
            counter.stop()
            progress_bar.visibility = View.GONE
        }

    }

    override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {

    }

    override fun surfaceDestroyed(p0: SurfaceHolder) {
        if (rtmpCamera1?.isStreaming == true) {
            rtmpCamera1?.stopStream()
            action_button.setImageResource(R.drawable.ic_start_button)
            counter.stop()
        }
        rtmpCamera1?.stopPreview()
    }


    override fun surfaceCreated(p0: SurfaceHolder) {
        surfaceViewCreated = true
        synchronized(this@BroadcastActivity) {
            if (havePermissions && surfaceViewCreated) {
                rtmpCamera1?.startStreamPreview()
            }
        }
    }

    private fun prepareEncoders(quality: Quality): Boolean? {
        return rtmpCamera1?.let { localCamera ->

            val avcLevel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                AVCLevel4
            } else {
                -1
            }

            // there used to be a defect related to Pixel 3a, that seems to have reemerged with latest Android version
            // https://github.com/pedroSG94/rtmp-rtsp-stream-client-java/issues/381
            if (Build.MODEL == "Pixel 3a") {
                localCamera.setForce(CodecUtil.Force.SOFTWARE, CodecUtil.Force.FIRST_COMPATIBLE_FOUND)
            }
            val (width, height) = localCamera.getResolutionForQuality(quality)
            val result = localCamera.prepareVideo(
                    width, height, quality.fps,
                    quality.videoBitrate * 1024,
                    1,
                    CameraHelper.getCameraOrientation(this),
                    -1, avcLevel
            ) && localCamera.prepareAudio(
                    quality.audioBitrate * 1024,
                    quality.sampleBitrate,
                    true,
                    false,
                    false
            )
            if (userStream.audioDisabled != false) {
                localCamera.disableAudio()
            }
            return result
        } ?: false
    }


    private fun RtmpCamera1.startStreamPreview() {
        val (width, height) = getStreamingSizes()
        startPreview(CameraHelper.Facing.BACK, width, height)
    }

    private fun RtmpCamera1.getStreamingSizes(): Pair<Int, Int> {
        val quality = getQuality() ?: Quality.HIGH
        return getResolutionForQuality(quality)
    }

    private fun RtmpCamera1.getResolutionForQuality(quality: Quality): Pair<Int, Int> {
        return getResolutionForQuality(resolutionsBack ?: listOf(), quality)
    }

    private fun RtmpCamera1.switchFlashLight() {
        if (isLanternEnabled) {
            disableLantern()
        } else {
            enableLantern()
        }
    }

    private fun getScaledPreviewSizeBasedOnCamera(): Pair<Int, Int>? {
        val originalPreviewSize = getPreviewSizeBasedOnCamera()
        return originalPreviewSize?.let { (originalWidth, originalHeight) ->
            val targetWidth = surfaceViewContainer.width
            val targetHeight = surfaceViewContainer.height
            val scaleFactor = min(targetWidth.toDouble() / originalWidth.toDouble(), targetHeight.toDouble() / originalHeight.toDouble())
            return Pair((originalWidth.toDouble() * scaleFactor).toInt(), (originalHeight.toDouble() * scaleFactor).toInt())
        }
    }

    @Suppress("DEPRECATION")
    private fun getPreviewSizeBasedOnCamera(): Pair<Int, Int>? {
        return try {
            VideoUtil.getBackCameraIndex()?.let {
                Camera.open(it).let { camera ->
                    try {
                        val quality = getQuality() ?: Quality.HIGH
                        getResolutionForQuality(camera.parameters.supportedPreviewSizes
                                ?: listOf(), quality)
                    } finally {
                        camera.release()
                    }
                }
            }
        } catch (e: RuntimeException) {
            null
        }
    }

    @Suppress("DEPRECATION")
    private fun getResolutionForQuality(supportedResolutions: List<Camera.Size>, quality: Quality): Pair<Int, Int> {

        val chosenResolution = ResolutionUtil(supportedResolutions, quality.width, quality.height).getCameraSize()

        return Pair(chosenResolution.width, chosenResolution.height)
    }


    private fun showPopup(v: View) {
        PopupMenu(this, v).apply {

            setOnMenuItemClickListener { item ->
                when (item?.itemId) {

                    R.id.main_toolbar_logout -> {
                        logout()
                        true
                    }
                    R.id.main_toolbar_share -> {
                        share()
                        true
                    }
                    else -> false
                }
            }
            inflate(R.menu.more_menu)
            show()
            if (userStream.viewUrl == null) {
                this.menu.findItem(R.id.main_toolbar_share).isVisible = false
            }
        }
    }

    private fun share() {
        ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setChooserTitle(getString(R.string.share_url))
                .setText(userStream.viewUrl)
                .startChooser()
    }

    private fun logout() {
        var dialog: AlertDialog? = null
        val alertDialogBuilder = AlertDialog.Builder(ContextThemeWrapper(this, R.style.YouCountDialog))
        alertDialogBuilder.setMessage(R.string.exit_message)
        alertDialogBuilder.setPositiveButton(R.string.yes) { _, _ -> loginActivityLoader.logout() }
        alertDialogBuilder.setNegativeButton(R.string.no) { _, _ -> dialog?.dismiss() }
        dialog = alertDialogBuilder.show()

    }

    private fun noConnectivity() {
        openDisconnectedDialog()
    }

    private fun openDisconnectedDialog() {
        stopStream()
        val view = layoutInflater.inflate(R.layout.dialog_interrupted, null)

        view.findViewById<Button>(R.id.exit_button).setOnClickListener {
            logout()
        }

        view.findViewById<Button>(R.id.retry_button).setOnClickListener {
            startStream()
        }

        dialog = dialog ?: AlertDialog.Builder(this).create().apply {
            setCancelable(false)
            setCanceledOnTouchOutside(false)
        }

        dialog?.apply {
            setView(view)
            show()
        }

        refreshTimer?.removeCallbacksAndMessages(null)
        refreshTimer = null

        refreshTimer = object : Handler(Looper.getMainLooper()) {}.apply {
            val attemptStartStreamRunnable = object : Runnable {
                override fun run() {
                    if (this@BroadcastActivity.isActive()) {
                        if (NetworkObserver(this@BroadcastActivity).hasNetworkConnectivity()) {
                            startStream()
                        } else {
                            postDelayed(this, RECONNECT_ATTEMPT_INTERVAL)
                        }
                    }
                }
            }
            postDelayed(attemptStartStreamRunnable, RECONNECT_ATTEMPT_INTERVAL)
        }
    }


    private fun resolveIntent() {
        this.userStream = intent.extras?.getParcelable(USER_STREAM_MODEL_EXTRA)
                ?: throw IllegalArgumentException("Use start with parameters to start activity")
    }

    companion object {
        private const val USER_STREAM_MODEL_EXTRA =
                "bg.dabulgaria.tibroish.stream.BroadcastActivity.USER_STREAM_MODEL_EXTRA"


        fun startWithParameters(context: Context, userStream: UserStreamModel) {
            val intent = Intent(context, BroadcastActivity::class.java).apply {
                putExtra(USER_STREAM_MODEL_EXTRA, userStream)
            }

            context.startActivity(intent)
        }
    }

    override fun androidInjector(): AndroidInjector<Any> {
        return dispatchingAndroidInjector
    }


    fun onAllPermissionsGranted() {
        val videoStreamSurfaceView = initSurfaceView()
        videoStreamSurfaceView.visibility = View.VISIBLE
        permission_rationale_container.visibility = View.GONE
        havePermissions = true

        rtmpCamera1 = RtmpCamera1(videoStreamSurfaceView, this@BroadcastActivity).apply {
            setReTries(10)
            setLogs(false)
            synchronized(this@BroadcastActivity) {
                if (havePermissions && surfaceViewCreated && !isOnPreview) {
                    startStreamPreview()
                }
            }
        }
    }

    fun onPermissionNotGranted(permission: String) {
        havePermissions = false
        permission_rationale_container.visibility = View.VISIBLE
        surfaceViewContainer.removeAllViews()
        allow_permissions_button.setOnClickListener {
            startActivity(Intent().apply {
                action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                data = Uri.fromParts("package", packageName, null)
            })
        }
    }


}

private enum class Quality(
        val videoBitrate: Int,
        val audioBitrate: Int,
        val sampleBitrate: Int,
        val fps: Int,
        val width: Int,
        val height: Int
) {
    HIGH(1024, 128, 44100, 30, 540, 960),
    MEDIUM(512, 128, 44100, 25, 360, 640),
    LOW(512, 128, 44100, 15, 270, 420);
}
