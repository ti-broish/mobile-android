package bg.dabulgaria.tibroish.persistence.local

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Base64
import android.webkit.MimeTypeMap
import bg.dabulgaria.tibroish.domain.io.IFileRepository
import bg.dabulgaria.tibroish.domain.providers.ILogger
import bg.dabulgaria.tibroish.infrastructure.di.annotations.AppContext
import java.io.File
import java.io.FileInputStream
import java.util.*
import javax.inject.Inject
import kotlin.jvm.Throws


class FileRepository @Inject constructor(@AppContext private val context: Context,
                                         val logger: ILogger): IFileRepository {

    @Throws(Exception::class)
    override fun getFolder(folder:String): File? {

        // check storage state
        val storageState = Environment.getExternalStorageState()

        if (storageState != Environment.MEDIA_MOUNTED) {

            throw Exception("Unable to access device storage. Probably not mounted!")
        } else if (storageState == Environment.MEDIA_MOUNTED_READ_ONLY) {

            throw Exception("Storage is mounted read only. Please check permissions!")
        }

        val externalFilesDirs = context.getExternalFilesDir(folder)
        if (externalFilesDirs == null || !externalFilesDirs.exists()) {

            throw Exception("Failed to load available folders!")
        }

        val mediaFolder: File = externalFilesDirs
        createNewFile(mediaFolder.absolutePath, Folders.NoMediaFileName)
        return mediaFolder
    }

    override fun createNewFile(folderName: String, fileName: String): File? {

        var file: File? = null
        try {

            file = File(folderName, fileName)

            var result = file.exists()

            if (!result) {

                result = file.createNewFile()
                if (!result)
                    logger.e(LOG_TAG, "ERROR creating file ${fileName}.", null )
            }
        } catch (e: Exception) {
            logger.e(LOG_TAG, "ERROR creating file.: ", e)
            logger.e(LOG_TAG, e)
        }
        return file
    }

    override fun createNewJpgFile(folder: String): File?{

        val folderPath = getFolder(folder)!!.absolutePath

        val newFileName = UUID.randomUUID().toString()+".jpg"

        return createNewFile(folderPath, newFileName)
    }

    override fun deleteFile(filePath: String): Boolean {

        val file = File(filePath)
        return if (file.exists()) file.delete() else true
    }

    override fun getFileName(filePath: String): String? {

        val file = File(filePath)

        if( !file.exists())
            return null

        return file.name
    }

    override fun getFileContentBase64Encoded(filePath: String): String? {

        val file = File(filePath)

        if(!file.exists())
            return null

        val mimeType = getMimeType(file) ?:return null

        val fileInputStreamReader = FileInputStream(file)
        val bytes = ByteArray(file.length().toInt())
        fileInputStreamReader.read(bytes)
        fileInputStreamReader.close()
        return "data:$mimeType;base64,${String(Base64.encode(bytes, Base64.DEFAULT), Charsets.UTF_8 )}"
    }

    override fun getFileSizeKb(filePath: String): Int {

        val file = File(filePath)
        return (file.length() / KB).toInt()
    }

    private fun getMimeType(file: File):String? {

        val uri = Uri.fromFile(file)
        var mimeType: String? = null

        mimeType = if (ContentResolver.SCHEME_CONTENT == uri.scheme) {

            context.contentResolver.getType(uri)
        }
        else {

            val fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.toString())
            MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension.toLowerCase(Locale.ENGLISH))
        }
        return mimeType
    }

    companion object{
        val LOG_TAG = FileRepository::class.java.simpleName
        val KB = 1024
    }
}