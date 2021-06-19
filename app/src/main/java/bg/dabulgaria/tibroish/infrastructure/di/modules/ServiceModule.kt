package bg.dabulgaria.tibroish.infrastructure.di.modules

import bg.dabulgaria.tibroish.domain.image.ImageUploaderService
import bg.dabulgaria.tibroish.infrastructure.services.TiBroishMessagingService
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
internal abstract class ServiceModule {

    @ContributesAndroidInjector
    abstract fun bindTiBroishMessagingService(): TiBroishMessagingService

    @ContributesAndroidInjector
    abstract fun bindImageUploaderService(): ImageUploaderService
}