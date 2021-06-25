package bg.dabulgaria.tibroish.live.utils

import android.hardware.Camera

object VideoUtil {
    /**
     * @return the Camera V1 index for the first front camera, or null if no such exists.
     */
    @Suppress("deprecation")
    fun getBackCameraIndex(): Int? {
        val cameraInfo = Camera.CameraInfo()
        val cameraCount: Int = Camera.getNumberOfCameras()
        for (camIdx in 0 until cameraCount) {
            Camera.getCameraInfo(camIdx, cameraInfo)
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                return camIdx
            }
        }
        return null
    }
}
