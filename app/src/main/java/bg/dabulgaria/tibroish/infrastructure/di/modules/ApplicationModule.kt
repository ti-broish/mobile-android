package bg.dabulgaria.tibroish.infrastructure.di.modules

import android.content.Context
import androidx.core.view.ViewCompat

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import bg.dabulgaria.tibroish.DaApplication
import bg.dabulgaria.tibroish.infrastructure.di.annotations.AppContext
import bg.dabulgaria.tibroish.domain.calculators.HashCalculator
import bg.dabulgaria.tibroish.domain.calculators.IHashCalculator
import bg.dabulgaria.tibroish.domain.providers.ITimestampProvider
import bg.dabulgaria.tibroish.domain.providers.TimestampProvider
import bg.dabulgaria.tibroish.persistence.local.TiBroishDatabase
import bg.dabulgaria.tibroish.infrastructure.schedulers.ISchedulersProvider
import bg.dabulgaria.tibroish.infrastructure.schedulers.SchedulersProvider

import javax.inject.Singleton

import dagger.Module
import dagger.Provides
import androidx.room.Room
import bg.dabulgaria.tibroish.domain.config.IAppConfigRepository
import bg.dabulgaria.tibroish.domain.providers.ILogger
import bg.dabulgaria.tibroish.domain.providers.Logger
import bg.dabulgaria.tibroish.infrastructure.di.annotations.ActivityScope
import bg.dabulgaria.tibroish.persistence.local.AppConfigRepository
import bg.dabulgaria.tibroish.persistence.remote.ILocationsRemoteRepo
import bg.dabulgaria.tibroish.persistence.remote.LocationsRemoteRepo
import bg.dabulgaria.tibroish.presentation.main.IMainNavigator
import bg.dabulgaria.tibroish.presentation.main.MainNavigator
import dagger.Binds


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
    fun bindsMainNavigator(implementation: MainNavigator): IMainNavigator = implementation

    @Provides
    @Singleton
    internal fun providesMarvelsDatabase( @AppContext context: Context ) :TiBroishDatabase{

        return Room.databaseBuilder( context,
                                     TiBroishDatabase::class.java,
                                     "marvels_db" )
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
    fun providesILocationsRemoteRepo( impementer: LocationsRemoteRepo): ILocationsRemoteRepo = impementer

    @Provides
    @Singleton
    internal fun providesIAppConfigRepository(implementer: AppConfigRepository) : IAppConfigRepository
            =implementer
}
