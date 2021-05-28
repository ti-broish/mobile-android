package bg.dabulgaria.tibroish.infrastructure.di.modules

import android.content.Context
import androidx.room.Room
import bg.dabulgaria.tibroish.DaApplication
import bg.dabulgaria.tibroish.domain.calculators.HashCalculator
import bg.dabulgaria.tibroish.domain.calculators.IHashCalculator
import bg.dabulgaria.tibroish.domain.providers.ILogger
import bg.dabulgaria.tibroish.domain.providers.ITimestampProvider
import bg.dabulgaria.tibroish.domain.providers.TimestampProvider
import bg.dabulgaria.tibroish.infrastructure.di.annotations.AppContext
import bg.dabulgaria.tibroish.infrastructure.schedulers.ISchedulersProvider
import bg.dabulgaria.tibroish.infrastructure.schedulers.SchedulersProvider
import bg.dabulgaria.tibroish.persistence.local.Logger
import bg.dabulgaria.tibroish.persistence.local.TiBroishDatabase
import bg.dabulgaria.tibroish.presentation.main.IMainPresenter
import bg.dabulgaria.tibroish.presentation.main.IMainRouter
import bg.dabulgaria.tibroish.presentation.main.MainPresenter
import bg.dabulgaria.tibroish.presentation.main.MainRouter
import bg.dabulgaria.tibroish.presentation.ui.common.DialogUtil
import bg.dabulgaria.tibroish.presentation.ui.common.FormValidator
import bg.dabulgaria.tibroish.presentation.ui.common.IDialogUtil
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
    internal fun providesAppContext(DaApplication : DaApplication) : Context {

        return DaApplication
    }

    @Provides
    @Singleton
    internal fun providesGson() : Gson {
        return GsonBuilder()
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
    internal fun providesTiBroishDatabase( @AppContext context: Context ): TiBroishDatabase{

        return Room.databaseBuilder( context, TiBroishDatabase::class.java, "ti_broish_db" )
                .build()
    }

    @Provides
    internal fun providesSchedulersProvider(schedulersProvider: SchedulersProvider): ISchedulersProvider {

        return schedulersProvider
    }

    @Provides
    @Singleton
    internal fun providesIHashCalculator() :IHashCalculator{

        return HashCalculator()
    }

    @Provides
    @Singleton
    internal fun providesITimestampProvider() :ITimestampProvider{

        return TimestampProvider()
    }

    @Provides
    @Singleton
    fun providesLogger():ILogger{

        return Logger()
    }

    @Provides
    @Singleton
    fun providesFormValidator(): FormValidator {
        return FormValidator()
    }

    @Provides
    @Singleton
    fun providesDialogUtil(): IDialogUtil {
        return DialogUtil()
    }
}
