package bg.dabulgaria.tibroish.infrastructure.di.modules

import android.content.Context

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import bg.dabulgaria.tibroish.DaApplication
import bg.dabulgaria.tibroish.infrastructure.di.annotations.AppContext
import bg.dabulgaria.tibroish.domain.calculators.HashCalculator
import bg.dabulgaria.tibroish.domain.calculators.IHashCalculator
import bg.dabulgaria.tibroish.domain.interactors.ComicInteractor
import bg.dabulgaria.tibroish.domain.interactors.IComicInteractor
import bg.dabulgaria.tibroish.domain.providers.ITimestampProvider
import bg.dabulgaria.tibroish.domain.providers.TimestampProvider
import bg.dabulgaria.tibroish.persistence.remote.MarvelsRemoteRepository
import bg.dabulgaria.tibroish.domain.repositories.remote.IMarvelsRemoteRepository
import bg.dabulgaria.tibroish.domain.repositories.local.IComicsLocalRepository
import bg.dabulgaria.tibroish.persistence.local.ComicsLocalRepository
import bg.dabulgaria.tibroish.persistence.local.MarvelsDatabase
import bg.dabulgaria.tibroish.infrastructure.schedulers.ISchedulersProvider
import bg.dabulgaria.tibroish.infrastructure.schedulers.SchedulersProvider

import javax.inject.Singleton

import dagger.Module
import dagger.Provides
import androidx.room.Room
import bg.dabulgaria.tibroish.domain.providers.ILogger
import bg.dabulgaria.tibroish.domain.providers.Logger
import bg.dabulgaria.tibroish.persistence.remote.ILocationsRemoteRepo
import bg.dabulgaria.tibroish.persistence.remote.LocationsRemoteRepo


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
    internal fun providesMarvelsDatabase( @AppContext context: Context ) :MarvelsDatabase{

        return Room.databaseBuilder( context,
                                     MarvelsDatabase::class.java,
                                     "marvels_db" )
                .build()
    }

    @Provides
    internal fun providesLocalStorageRepository(database :MarvelsDatabase): IComicsLocalRepository {

        return ComicsLocalRepository( database)
    }

    @Provides
    internal fun providesSchedulersProvider(schedulersProvider: SchedulersProvider): ISchedulersProvider {

        return schedulersProvider
    }

    @Provides
    internal fun providesMarvelsApiRepository( comicApiRepository: MarvelsRemoteRepository): IMarvelsRemoteRepository {

        return comicApiRepository
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
    fun providesComicInteractor(comicInteractor: ComicInteractor): IComicInteractor {

        return comicInteractor
    }

    @Provides
    @Singleton
    fun providesLogger():ILogger{

        return Logger()
    }

    @Provides
    @Singleton
    fun providesILocationsRemoteRepo( impementer: LocationsRemoteRepo): ILocationsRemoteRepo = impementer

}
