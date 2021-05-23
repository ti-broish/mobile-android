package bg.dabulgaria.tibroish.domain.protocol.image

import android.graphics.Bitmap


interface IImageCopier {

    fun copyToLocalFolder(localFileUrlPath: String, folderName:String): String?

    fun copyToLocalUploadsFolder(localFileUrlPath: String): String?

    fun saveBitmapToLocalFolderFile(bitmap: Bitmap, folderName: String):String?

    fun saveBitmapToLocalUploadsFolderFile(bitmap: Bitmap):String?
}