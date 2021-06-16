package bg.dabulgaria.tibroish.persistence.local

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
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
                                      private val stremCopier:IStreamCopier) :IImageCopier {

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

            val uri = Uri.parse(localFileUrlPath)
            val bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.contentResolver, uri)
            ) { imageDecoder, imageInfo, source ->
                imageDecoder.isMutableRequired = true
                imageDecoder.onPartialImageListener = ImageDecoder.OnPartialImageListener { e ->
                    logger.e(LOG_TAG, e)
                    false
                }
            }

            saveBitmapToLocalFolderFile(bitmap, folderName)
        }
        else {

            val localFile = File(localFileUrlPath)
            var fileName = newFileName
            if (fileName.isEmpty())
                fileName = localFile.name

            val newFile = fileRepository.createNewFile(folderPath, fileName)
                    ?:return null

            stremCopier.copy(FileInputStream(localFile), FileOutputStream(newFile))
            newFile.absolutePath
        }
    }

    override fun saveBitmapToLocalUploadsFolderFile(bitmap: Bitmap ):String?
            = saveBitmapToLocalFolderFile(bitmap, Folders.LocalUploadsFolder)

    override fun saveBitmapToLocalFolderFile(bitmap: Bitmap, folderName: String):String?{

        val folderPath = fileRepository.getFolder(folderName)!!.absolutePath

        val mutableBitmap =  if(!bitmap.isMutable)
            bitmap.copy(Bitmap.Config.ARGB_8888, true)
        else
            bitmap

        val newFileName = UUID.randomUUID().toString()+".jpg"
        if ( mutableBitmap.width <= 0 || mutableBitmap.height <= 0)
            throw Exception("Unable to load the bitmap for image from bitmap")

        val newFile = fileRepository.createNewFile(folderPath, newFileName)
                ?:return null

        val fileStreamPhoto: OutputStream = FileOutputStream(newFile.path)
        mutableBitmap.compress(Bitmap.CompressFormat.JPEG, 99, fileStreamPhoto)
        fileStreamPhoto.flush()
        fileStreamPhoto.close()
        return newFile.absolutePath
    }

    companion object{

        val LOG_TAG = ImageCopier::class.java.simpleName
    }
}