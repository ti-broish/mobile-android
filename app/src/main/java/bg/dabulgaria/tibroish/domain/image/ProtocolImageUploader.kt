package bg.dabulgaria.tibroish.domain.image

import bg.dabulgaria.tibroish.domain.io.IFileRepository
import bg.dabulgaria.tibroish.domain.organisation.ITiBroishRemoteRepository
import bg.dabulgaria.tibroish.domain.protocol.IProtocolsRepository
import bg.dabulgaria.tibroish.domain.protocol.ProtocolStatus
import bg.dabulgaria.tibroish.domain.protocol.image.IProtocolImagesRepository
import bg.dabulgaria.tibroish.domain.protocol.image.ProtocolImage
import bg.dabulgaria.tibroish.domain.protocol.image.UploadStatus
import javax.inject.Inject

interface IProtocolImageUploader{

    fun uploadImages(protocolId:Long)
}

class ProtocolImageUploader @Inject constructor(private val tiBroishRemoteRepo: ITiBroishRemoteRepository,
                                                private val protocolImagesLocalRepo: IProtocolImagesRepository,
                                                private val protocolLocalRepo: IProtocolsRepository,
                                                private val fileRepo: IFileRepository)
    :IProtocolImageUploader {

    override fun uploadImages(protocolId:Long){

        val protocol = protocolLocalRepo.get(protocolId)
                ?:return

        if( protocol.status == ProtocolStatus.Uploaded )
            return

        val images = protocolImagesLocalRepo.getByProtocolId(protocolId)

        images.mapIndexed { index, protocolImage ->  uploadImage(index,protocolImage) }
    }

    private fun uploadImage(index:Int, protocolImage:ProtocolImage){

        if( protocolImage.uploadStatus == UploadStatus.Uploaded)
            return

        val fileName = fileRepo.getFileName(protocolImage.localFilePath)

        if( fileName.isNullOrEmpty() ){

            markWithStatus(protocolImage,UploadStatus.Replace)
            return
        }

        val fileContentB64 = fileRepo.getFileContentBase64Encoded(protocolImage.localFilePath)

        if( fileContentB64.isNullOrEmpty() ){

            markWithStatus(protocolImage,UploadStatus.Replace)
            return
        }

        val uploadImage = UploadImage(fileName,
                protocolImage.localFilePath,
                null,
                fileContentB64,
                true,
                protocolImage.id.toInt())

        val uploadImageRequest = UploadImageRequest(uploadImage)

        val response = tiBroishRemoteRepo.uploadImage(uploadImageRequest)

        updateUploaded(protocolImage, response)
    }

    private fun updateUploaded(protocolImage: ProtocolImage, response: UploadImageResponse ){

        if(response.id.isNullOrEmpty()
                || response.url.isNullOrEmpty()
                || response.index != protocolImage.id.toInt())
            return

        protocolImage.serverId = response.id
        protocolImage.serverUrl = response.url
        markWithStatus(protocolImage, UploadStatus.Uploaded)
    }

    private fun markWithStatus(protocolImage:ProtocolImage, uploadStatus:UploadStatus){

        protocolImage.uploadStatus = uploadStatus
        protocolImagesLocalRepo.update(protocolImage)
    }
}