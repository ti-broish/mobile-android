package bg.dabulgaria.tibroish.persistence.local.di

import bg.dabulgaria.tibroish.domain.config.IAppConfigRepository
import bg.dabulgaria.tibroish.domain.image.IGalleryImagesRepository
import bg.dabulgaria.tibroish.domain.protocol.IProtocolsRepository
import bg.dabulgaria.tibroish.domain.protocol.image.IProtocolImagesRepository
import bg.dabulgaria.tibroish.persistence.local.AppConfigRepository
import bg.dabulgaria.tibroish.persistence.local.GalleryImagesRepository
import bg.dabulgaria.tibroish.persistence.local.ProtocolImagesRepository
import bg.dabulgaria.tibroish.persistence.local.ProtocolsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
interface LocalPersistenceModule {

    @Binds
    fun bindsIAppConfigRepository(implementer: AppConfigRepository) : IAppConfigRepository

    @Binds
    fun bindsIProtocolsRepository(implementation: ProtocolsRepository): IProtocolsRepository

    @Binds
    fun bindsIProtocolImagesRepository(implementation: ProtocolImagesRepository): IProtocolImagesRepository

    @Binds
    fun bindsIGalleryImagesRepository(implementation: GalleryImagesRepository): IGalleryImagesRepository

}