package bg.dabulgaria.tibroish.di.modules

import com.google.gson.Gson
import bg.dabulgaria.tibroish.BuildConfig
import bg.dabulgaria.tibroish.persistence.remote.MarvelsApiController
import bg.dabulgaria.tibroish.persistence.remote.VDApiController
import bg.dabulgaria.tibroish.persistence.remote.VDApiInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class NetworkModule() {

    companion object {
        val TAG = NetworkModule::class.java.simpleName
    }

    @Provides
    @Singleton
    fun providesOkHttpClient(): OkHttpClient{

        return OkHttpClient.Builder()
                .connectTimeout(2L, TimeUnit.MINUTES)
                .readTimeout(2L, TimeUnit.MINUTES)
                .writeTimeout(2L, TimeUnit.MINUTES)
                .addInterceptor( VDApiInterceptor( "VillageDirect Android" ))
                .build()
    }


    @Provides
    @Singleton
    fun providesMarvelsApiController( okHttpClient : OkHttpClient, gson : Gson ): MarvelsApiController{

        val retrofit = Retrofit.Builder()
                .baseUrl( BuildConfig.API_ENDPOINT)
                .client( okHttpClient )
                .addConverterFactory( GsonConverterFactory.create( gson ) )
                .addCallAdapterFactory( RxJava3CallAdapterFactory.create() )
                .build()

        return retrofit.create(MarvelsApiController::class.java )
    }

    @Provides
    @Singleton
    fun providesVDApiController( okHttpClient : OkHttpClient, gson : Gson ): VDApiController{

        val retrofit = Retrofit.Builder()
                .baseUrl( BuildConfig.VD_API_ENDPOINT)
                .client( okHttpClient )
                .addConverterFactory( GsonConverterFactory.create( gson ) )
                .addCallAdapterFactory( RxJava3CallAdapterFactory.create() )
                .build()

        return retrofit.create(VDApiController::class.java )
    }

}