package bg.dabulgaria.tibroish.domain.protocol.image


interface IImageCopier {

    fun copyToFolder(localFileUrlPath: String,
                     folderName:String): String?

    fun copyToUploadsFolder(localFileUrlPath: String): String?
}