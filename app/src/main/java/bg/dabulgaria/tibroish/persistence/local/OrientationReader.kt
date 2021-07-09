package bg.dabulgaria.tibroish.persistence.local

import android.graphics.Matrix
import androidx.exifinterface.media.ExifInterface
import bg.dabulgaria.tibroish.domain.providers.ILogger
import java.io.IOException
import javax.inject.Inject


interface IOrientationReader{

    fun getOrientation(imageFilePath: String): Int

    fun getRotationMatrix(orientation: Int): Matrix
}

class OrientationReader @Inject constructor(private val logger: ILogger): IOrientationReader{

    override fun getOrientation(imageFilePath: String): Int{

        var exif: ExifInterface? = null
        try {
            exif = ExifInterface(imageFilePath)
        } catch (e: IOException) {
           logger.e(TAG, e)
        }

        val orientation: Int = exif?.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL) ?: ExifInterface.ORIENTATION_NORMAL

        return orientation
    }

    override fun getRotationMatrix(orientation: Int): Matrix {

        val matrix = Matrix()

        when (orientation) {
            ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> matrix.setScale(-1f, 1f)
            ExifInterface.ORIENTATION_ROTATE_180 -> matrix.setRotate(180f)
            ExifInterface.ORIENTATION_FLIP_VERTICAL -> {
                matrix.setRotate(180f)
                matrix.postScale(-1f, 1f)
            }
            ExifInterface.ORIENTATION_TRANSPOSE -> {
                matrix.setRotate(90f)
                matrix.postScale(-1f, 1f)
            }
            ExifInterface.ORIENTATION_ROTATE_90 -> matrix.setRotate(90f)
            ExifInterface.ORIENTATION_TRANSVERSE -> {
                matrix.setRotate(-90f)
                matrix.postScale(-1f, 1f)
            }
            ExifInterface.ORIENTATION_ROTATE_270 -> matrix.setRotate(-90f)
        }

        return matrix
    }

    companion object{
        val TAG = OrientationReader::class.java.simpleName
    }
}