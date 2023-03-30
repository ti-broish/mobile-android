package bg.dabulgaria.tibroish.persistence.local

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.util.Size
import androidx.annotation.RequiresApi
import bg.dabulgaria.tibroish.domain.io.IFileRepository
import bg.dabulgaria.tibroish.domain.protocol.image.IImageCopier
import bg.dabulgaria.tibroish.domain.protocol.image.Uris
import bg.dabulgaria.tibroish.domain.providers.ILogger
import bg.dabulgaria.tibroish.infrastructure.di.annotations.AppContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.*
import javax.inject.Inject

class ImageCopier @Inject constructor(@AppContext private val context: Context,
                                      private val logger: ILogger,
                                      private val fileRepository: IFileRepository,
                                      private val streamCopier:IStreamCopier,
                                      private val orientationReader: IOrientationReader) :IImageCopier {

    override fun copyToLocalUploadsFolder(localFileUrlPath: String): String? {

        return copyToLocalFolder(localFileUrlPath, Folders.LocalUploadsFolder)
    }

    override fun copyToLocalFolder(localFileUrlPath: String, folderName: String): String? {

        if (localFileUrlPath.isEmpty())
            throw Exception("Empty file path.")

        val folderPath = fileRepository.getFolder(folderName)!!.absolutePath
        val newFileName = UUID.randomUUID().toString()+".jpg"

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
                && localFileUrlPath.startsWith(Uris.ContentScheme)) {

            scaleAndCopyImageUri(localFileUrlPath, folderName)
        }
        else {

            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeFile(localFileUrlPath, options)
            val orientation = orientationReader.getOrientation(localFileUrlPath)

            val actualSize = Size(options.outWidth, options.outHeight)
            val targetSize = getTargetImageSize(actualSize)

            if(actualSize == targetSize){

                copyFileImage(localFileUrlPath, newFileName, folderPath)
            }
            else{

                scaleAndCopyFileImage(localFileUrlPath, targetSize, folderName, orientation)
            }
        }
    }

    override fun saveBitmapToLocalUploadsFolderFile(bitmap: Bitmap ):String?
            = saveBitmapToLocalFolderFile(bitmap, Folders.LocalUploadsFolder)

    override fun saveBitmapToLocalFolderFile(bitmap: Bitmap, folderName: String):String?{

        val folderPath = fileRepository.getFolder(folderName)!!.absolutePath

        val mutableBitmap = if(!bitmap.isMutable)
            bitmap.copy(Bitmap.Config.ARGB_8888, true)
        else
            bitmap

        val newFileName = UUID.randomUUID().toString()+".jpg"
        if ( mutableBitmap.width <= 0 || mutableBitmap.height <= 0)
            throw Exception("Unable to load the bitmap for image from bitmap")

        val newFile = fileRepository.createNewFile(folderPath, newFileName)
                ?:return null

        val fileStreamPhoto: OutputStream = FileOutputStream(newFile.path)
        mutableBitmap.compress(Bitmap.CompressFormat.JPEG, IMAGE_QUALITY, fileStreamPhoto)
        fileStreamPhoto.flush()
        fileStreamPhoto.close()
        return newFile.absolutePath
    }

    private fun scaleAndCopyFileImage(localFileUrlPath: String,
                                      targetSize: Size,
                                      folderName: String,
                                      orientation: Int): String?{

        val sourceBitmap = BitmapFactory.decodeFile(localFileUrlPath)

        val scaledBitmap = Bitmap.createScaledBitmap(sourceBitmap, targetSize.width, targetSize.height, false)

        val bitmap = if(orientation != ExifInterface.ORIENTATION_NORMAL) {
            val matrix = orientationReader.getRotationMatrix(orientation)
            Bitmap.createBitmap(scaledBitmap,
                    0,
                    0,
                    scaledBitmap.getWidth(),
                    scaledBitmap.getHeight(),
                    matrix,
                    true)
        }
        else
            scaledBitmap

        val fileName = saveBitmapToLocalFolderFile(bitmap, folderName)

        if(!bitmap.isRecycled)
            bitmap.recycle()

        if(!scaledBitmap.isRecycled)
            scaledBitmap.recycle()

        if(!sourceBitmap.isRecycled)
            sourceBitmap.recycle()

        return fileName
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun scaleAndCopyImageUri(localFileUrlPath:String, folderName: String): String?{

        val uri = Uri.parse(localFileUrlPath)
        val bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.contentResolver, uri))
        { imageDecoder, info, source ->

            imageDecoder.isMutableRequired = true
            val size= getTargetImageSize(info.size)
            imageDecoder.setTargetSize(size.width, size.height)

            imageDecoder.onPartialImageListener = ImageDecoder.OnPartialImageListener { e ->
                logger.e(LOG_TAG, e)
                false
            }
        }

        val fileName = saveBitmapToLocalFolderFile(bitmap, folderName)

        if(!bitmap.isRecycled)
            bitmap.recycle()

        return fileName
    }

    private fun copyFileImage(localFileUrlPath:String,
                              newFileName:String,
                              folderPath: String): String?{

        val localFile = File(localFileUrlPath)
        var fileName = newFileName
        if (fileName.isEmpty())
            fileName = localFile.name

        val newFile = fileRepository.createNewFile(folderPath, fileName)
                ?:return null

        streamCopier.copy(FileInputStream(localFile), FileOutputStream(newFile))
        return newFile.absolutePath
    }

    private fun getTargetImageSize(actualSize: Size): Size{

        if(actualSize.width <= MAX_IMAGE_SIZE_PX && actualSize.height <= MAX_IMAGE_SIZE_PX)
            return actualSize

        val ratio = actualSize.width.toFloat() / actualSize.height.toFloat()

        val width = if(ratio > 1.0)
            MAX_IMAGE_SIZE_PX.toFloat()
        else
            MAX_IMAGE_SIZE_PX * ratio

        val height = width / ratio

        return Size(width.toInt(), height.toInt())
    }

    companion object{

        val LOG_TAG = ImageCopier::class.java.simpleName
        val MAX_IMAGE_SIZE_PX = 3096
        val IMAGE_QUALITY = 80
    }
}