package bg.dabulgaria.tibroish.live.utils

import android.hardware.Camera
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class ResolutionUtil(private val resolutions: List<Camera.Size>, private val width: Int, private val height: Int) {
    fun getCameraSize(): Camera.Size {
        val(selectedWidth, selectedHeight) = resolutions.map {
            Pair(min(it.width, it.height), max(it.width, it.height))
        }.minBy { size -> getDifference(size.first, size.second) }
                ?: Pair(width, height)

        return resolutions.firstOrNull { size ->
            matchPreviewSize(size, selectedWidth, selectedHeight)
        } ?: resolutions.first()
    }

    private fun getDifference(selectedWidth: Int, selectedHeight: Int): Double {
        return abs(width.toDouble() - selectedWidth.toDouble()) *
                abs(height.toDouble() - selectedHeight.toDouble())
    }

    private fun matchPreviewSize(previewSize: Camera.Size, selectedWidth: Int, selectedHeight: Int): Boolean {
        return (previewSize.width == selectedWidth && previewSize.height == selectedHeight) ||
                (previewSize.height == selectedWidth && previewSize.width == selectedHeight)
    }
}
