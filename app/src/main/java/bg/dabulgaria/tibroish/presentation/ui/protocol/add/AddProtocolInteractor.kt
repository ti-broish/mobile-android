package bg.dabulgaria.tibroish.presentation.ui.protocol.add

import bg.dabulgaria.tibroish.domain.io.IFileRepository
import bg.dabulgaria.tibroish.domain.organisation.ITiBorishRemoteRepository
import bg.dabulgaria.tibroish.domain.protocol.IProtocolsRepository
import bg.dabulgaria.tibroish.domain.protocol.Protocol
import bg.dabulgaria.tibroish.domain.protocol.ProtocolExt
import bg.dabulgaria.tibroish.domain.protocol.ProtocolStatus
import bg.dabulgaria.tibroish.domain.protocol.image.IProtocolImagesRepository
import bg.dabulgaria.tibroish.domain.protocol.image.ProtocolImage
import bg.dabulgaria.tibroish.domain.providers.ILogger
import bg.dabulgaria.tibroish.presentation.providers.IResourceProvider
import java.util.*


import javax.inject.Inject

interface IAddProtocolInteractor {

    fun addNew(protocol: Protocol): ProtocolExt

    fun loadData(viewData: AddProtocolViewData) :AddProtocolViewData

    fun deleteImage(image: ProtocolImage)
}

class AddProtocolInteractor @Inject constructor(private val protocolsRepo: IProtocolsRepository,
                                                private val protocolsImagesRepo: IProtocolImagesRepository,
                                                private val fileRepo: IFileRepository,
                                                private val apiRepo: ITiBorishRemoteRepository,
                                                private val resourceProvider: IResourceProvider,
                                                private val logger:ILogger)
    : IAddProtocolInteractor{

    override fun addNew(protocol: Protocol): ProtocolExt {

        protocol.uuid = UUID.randomUUID().toString()
        protocol.status = ProtocolStatus.New
        protocolsRepo.insert(protocol)
        return ProtocolExt(protocol)
    }

    override fun loadData(viewData: AddProtocolViewData) :AddProtocolViewData{

        val newViewData = AddProtocolViewData()
        newViewData.protocolId = viewData.protocolId

        if(newViewData.protocolId ?:0 > 0){

            val protocol = protocolsRepo.get( viewData.protocolId?:0 )
            val images = protocolsImagesRepo.getByProtocolId(protocol.id)
            newViewData.protocol = ProtocolExt(protocol).apply { this.images.addAll(images) }
        }

        newViewData.items.add(AddProtocolListItemHeader())
        newViewData.items.add(AddProtocolListItemSection())

        for( photo in newViewData.protocol?.images.orEmpty())
            newViewData.items.add(AddProtocolListItemImage(photo))

        newViewData.items.add(AddProtocolListItemButtons())

        return newViewData
    }

    override fun deleteImage(image: ProtocolImage) {

        fileRepo.deleteFile(image.localFilePath)
        protocolsImagesRepo.delete(image)
    }

    companion object {

        private val TAG = AddProtocolInteractor::class.simpleName
    }
}
