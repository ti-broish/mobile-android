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
                                      private val fileReposotiry: IFileRepository,
                                      private val stremCopier:IStreamCopier) :IImageCopier {

    override fun copyToUploadsFolder(localFileUrlPath: String): String? {

        return copyToFolder(localFileUrlPath, Folders.LocalUploadsFolder)
    }

    override fun copyToFolder(localFileUrlPath: String, folderName: String): String? {

        if (localFileUrlPath.isEmpty())
            throw Exception("Empty file path.")

        val folderPath = fileReposotiry.getFolder(folderName)!!.absolutePath
        val newFileName = UUID.randomUUID().toString()

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

            if ( bitmap.width <= 0 || bitmap.height <= 0 || !bitmap.isMutable )
                throw Exception("Unable to load the bitmap for image $localFileUrlPath")

            val newFile = fileReposotiry.createNewFile(folderPath, newFileName)
                    ?:return null

            val fileStreamPhoto: OutputStream = FileOutputStream(newFile.path)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 99, fileStreamPhoto)
            fileStreamPhoto.flush()
            fileStreamPhoto.close()
            newFile.absolutePath
        }
        else {

            val localFile = File(localFileUrlPath)
            var fileName = newFileName
            if (fileName.isEmpty())
                fileName = localFile.name

            val newFile = fileReposotiry.createNewFile(folderPath, fileName)
                    ?:return null

            stremCopier.copy(FileInputStream(localFile), FileOutputStream(newFile))
            newFile.absolutePath
        }
    }

    companion object{
        val LOG_TAG = ImageCopier::class.java.simpleName
    }
}