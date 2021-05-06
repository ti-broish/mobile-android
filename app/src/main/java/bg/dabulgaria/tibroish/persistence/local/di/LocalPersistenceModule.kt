package bg.dabulgaria.tibroish.persistence.local.di

import bg.dabulgaria.tibroish.domain.config.IAppConfigRepository
import bg.dabulgaria.tibroish.domain.image.IGalleryImagesRepository
import bg.dabulgaria.tibroish.domain.protocol.IProtocolsRepository
import bg.dabulgaria.tibroish.domain.protocol.image.IProtocolImagesRepository
import bg.dabulgaria.tibroish.domain.user.IAuthRepository
import bg.dabulgaria.tibroish.domain.user.IUserLocalRepository
import bg.dabulgaria.tibroish.persistence.local.*
import dagger.Binds
import dagger.Module

@Module
interface LocalPersistenceModule {

    @Binds
    fun bindsIAppConfigRepository(implementation: AppConfigRepository) : IAppConfigRepository

    @Binds
    fun bindsIProtocolsRepository(implementation: ProtocolsRepository): IProtocolsRepository

    @Binds
    fun bindsIProtocolImagesRepository(implementation: ProtocolImagesRepository): IProtocolImagesRepository

    @Binds
    fun bindsIGalleryImagesRepository(implementation: GalleryImagesRepository): IGalleryImagesRepository

    @Binds
    fun bindsIUserRepository(implementation: UserRepository): IUserLocalRepository

    @Binds
    fun bindsIAuthRepository(implementation: AuthRepository): IAuthRepository
}