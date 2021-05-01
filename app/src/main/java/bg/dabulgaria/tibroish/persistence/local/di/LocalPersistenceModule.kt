package bg.dabulgaria.tibroish.persistence.local.di

import bg.dabulgaria.tibroish.domain.image.IGalleryImagesRepository
import bg.dabulgaria.tibroish.domain.protocol.IProtocolsRepository
import bg.dabulgaria.tibroish.domain.protocol.image.IProtocolImagesRepository
import bg.dabulgaria.tibroish.persistence.local.GalleryImagesRepository
import bg.dabulgaria.tibroish.persistence.local.ProtocolImagesRepository
import bg.dabulgaria.tibroish.persistence.local.ProtocolsRepository
import dagger.Binds
import dagger.Module

@Module
interface LocalPersistenceModule {

    @Binds
    fun bindsIProtocolsRepository(implementation: ProtocolsRepository): IProtocolsRepository

    @Binds
    fun bindsIProtocolImagesRepository(implementation: ProtocolImagesRepository): IProtocolImagesRepository

    @Binds
    fun bindsIGalleryImagesRepository(implementation: GalleryImagesRepository): IGalleryImagesRepository
}