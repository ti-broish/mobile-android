package bg.dabulgaria.tibroish.infrastructure.di.modules

import android.content.Context
import androidx.room.Room
import bg.dabulgaria.tibroish.DaApplication
import bg.dabulgaria.tibroish.domain.calculators.HashCalculator
import bg.dabulgaria.tibroish.domain.calculators.IHashCalculator
import bg.dabulgaria.tibroish.domain.organisation.ITiBroishRemoteRepository
import bg.dabulgaria.tibroish.domain.protocol.IProtocolSenderController
import bg.dabulgaria.tibroish.domain.protocol.IProtocolsRepository
import bg.dabulgaria.tibroish.domain.protocol.ProtocolSenderController
import bg.dabulgaria.tibroish.domain.protocol.ProtocolStatusRemote
import bg.dabulgaria.tibroish.domain.protocol.image.IProtocolImageUploader
import bg.dabulgaria.tibroish.domain.protocol.image.IProtocolImagesRepository
import bg.dabulgaria.tibroish.domain.providers.ILogger
import bg.dabulgaria.tibroish.domain.providers.ITimestampProvider
import bg.dabulgaria.tibroish.domain.providers.TimestampProvider
import bg.dabulgaria.tibroish.domain.user.IUserAuthenticator
import bg.dabulgaria.tibroish.domain.violation.IViolationRepository
import bg.dabulgaria.tibroish.domain.violation.IViolationSenderController
import bg.dabulgaria.tibroish.domain.violation.ViolationSenderController
import bg.dabulgaria.tibroish.domain.violation.image.IViolationImageUploader
import bg.dabulgaria.tibroish.domain.violation.image.IViolationImagesRepository
import bg.dabulgaria.tibroish.infrastructure.di.annotations.AppContext
import bg.dabulgaria.tibroish.infrastructure.schedulers.ISchedulersProvider
import bg.dabulgaria.tibroish.infrastructure.schedulers.SchedulersProvider
import bg.dabulgaria.tibroish.live.utils.LoginActivityLoader
import bg.dabulgaria.tibroish.persistence.local.Logger
import bg.dabulgaria.tibroish.persistence.local.db.TiBroishDatabase
import bg.dabulgaria.tibroish.presentation.main.IMainPresenter
import bg.dabulgaria.tibroish.presentation.main.IMainRouter
import bg.dabulgaria.tibroish.presentation.main.MainPresenter
import bg.dabulgaria.tibroish.presentation.main.MainRouter
import bg.dabulgaria.tibroish.presentation.notification.ChannelInitializer
import bg.dabulgaria.tibroish.presentation.notification.IChannelInitializer
import bg.dabulgaria.tibroish.presentation.providers.CameraTakenImageProvider
import bg.dabulgaria.tibroish.presentation.providers.GallerySelectedImagesProvider
import bg.dabulgaria.tibroish.presentation.providers.ICameraTakenImageProvider
import bg.dabulgaria.tibroish.presentation.providers.IGallerySelectedImagesProvider
import bg.dabulgaria.tibroish.presentation.push.IPushActionRouter
import bg.dabulgaria.tibroish.presentation.push.IPushTokenSender
import bg.dabulgaria.tibroish.presentation.push.PushActionRouter
import bg.dabulgaria.tibroish.presentation.push.PushTokenSender
import bg.dabulgaria.tibroish.presentation.ui.common.*
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class ApplicationModule {

    @Provides
    @Singleton
    @AppContext
    internal fun providesAppContext(DaApplication: DaApplication): Context {

        return DaApplication
    }

    @Provides
    @Singleton
    internal fun providesGson(): Gson {
        return GsonBuilder()
                .registerTypeAdapter(ProtocolStatusRemote::class.java, ProtocolStatusRemote.deserializer)
                .setDateFormat("dd/mm/yyyy HH:mm")
                .create()
    }

    @Provides
    @Singleton
    fun bindsMainRouter(implementation: MainRouter): IMainRouter = implementation

    @Provides
    @Singleton
    fun bindsMainPresenter(implementation: MainPresenter): IMainPresenter = implementation

    @Provides
    @Singleton
    internal fun providesTiBroishDatabase(@AppContext context: Context): TiBroishDatabase {

        return Room.databaseBuilder(context, TiBroishDatabase::class.java, "ti_broish_db")
                .build()
    }

    @Provides
    internal fun providesSchedulersProvider(schedulersProvider: SchedulersProvider): ISchedulersProvider {

        return schedulersProvider
    }

    @Provides
    @Singleton
    internal fun providesIHashCalculator(): IHashCalculator {

        return HashCalculator()
    }

    @Provides
    @Singleton
    internal fun providesITimestampProvider(): ITimestampProvider {

        return TimestampProvider()
    }

    @Provides
    @Singleton
    fun providesLogger(): ILogger {

        return Logger()
    }

    @Provides
    @Singleton
    fun providesFormValidator(
            logger: ILogger,
            organizationsManager: OrganizationsManager): FormValidator {
        return FormValidator(logger, organizationsManager)
    }

    @Provides
    @Singleton
    fun providesDialogUtil(): IDialogUtil {
        return DialogUtil()
    }

    @Provides
    @Singleton
    fun providesOrganizationsManager(tiBroishRemoteRepository: ITiBroishRemoteRepository):
            IOrganizationsManager {
        return OrganizationsManager(tiBroishRemoteRepository)
    }

    @Provides
    @Singleton
    fun providesOrganizationsDropdownUtil(): IOrganizationsDropdownUtil {
        return OrganizationsDropdownUtil()
    }

    @Provides
    @Singleton
    internal fun providesIGallerySelectedImagesProvider(implementation: GallerySelectedImagesProvider): IGallerySelectedImagesProvider {
        return implementation
    }

    @Provides
    @Singleton
    internal fun providesStatusColorUtil(): IStatusColorUtil {
        return StatusColorUtil()
    }

    @Provides
    @Singleton
    internal fun providesICameraTakenImageProvider(implementation: CameraTakenImageProvider): ICameraTakenImageProvider = implementation

    @Provides
    @Singleton
    internal fun providesIPushTokenSender(implemenation: PushTokenSender): IPushTokenSender = implemenation

    @Provides
    internal fun providesIPushActionRouter(implemenation: PushActionRouter): IPushActionRouter = implemenation

    @Provides
    internal fun providesIChannelInitializer(iml: ChannelInitializer): IChannelInitializer = iml

    @Provides
    @Singleton
    fun provideLoginScreenLoader(context: Context, userAuthenticator: IUserAuthenticator): LoginActivityLoader {
        return LoginActivityLoader(context, userAuthenticator)
    }

    @Provides
    @Singleton
    fun providesProtocolSenderController(
        protocolImageUploader: IProtocolImageUploader,
        protocolsRepository: IProtocolsRepository,
        protocolImagesRepository: IProtocolImagesRepository,
        tiBroishRemoteRepository: ITiBroishRemoteRepository
    ): IProtocolSenderController {
        return ProtocolSenderController(
            protocolImageUploader,
            protocolImagesRepository,
            protocolsRepository,
            tiBroishRemoteRepository
        )
    }

    @Provides
    @Singleton
    fun providesViolationSenderController(
        violationImageUploader: IViolationImageUploader,
        violationsRepository: IViolationRepository,
        violationImagesRepository: IViolationImagesRepository,
        tiBroishRemoteRepository: ITiBroishRemoteRepository
    ): IViolationSenderController {
        return ViolationSenderController(
            violationsRepository,
            violationImagesRepository,
            violationImageUploader,
            tiBroishRemoteRepository
        )
    }
}
