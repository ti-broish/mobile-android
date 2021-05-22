package bg.dabulgaria.tibroish.persistence.local

import android.content.Context
import android.os.Environment
import bg.dabulgaria.tibroish.domain.io.IFileRepository
import bg.dabulgaria.tibroish.domain.providers.ILogger
import bg.dabulgaria.tibroish.infrastructure.di.annotations.AppContext
import java.io.File
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

    override fun deleteFile(filePath: String): Boolean {

        val file = File(filePath)
        return if (file.exists()) file.delete() else true
    }

    companion object{
        val LOG_TAG = FileRepository::class.java.simpleName
    }
}