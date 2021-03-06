package bg.dabulgaria.tibroish.domain.io

import java.io.File
import kotlin.jvm.Throws

interface IFileRepository {

    @Throws(Exception::class)
    fun getFolder(folder:String): File?

    fun createNewFile(folderName: String, fileName: String): File?

    fun createNewJpgFile(folder: String): File?

    fun deleteFile(filePath:String) :Boolean

    fun getFileName(filePath:String) :String?

    fun getFileContentBase64Encoded(filePath: String):String?

    fun getFileSizeKb(filePath:String) :Int
}