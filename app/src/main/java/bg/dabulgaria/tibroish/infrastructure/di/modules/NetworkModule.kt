package bg.dabulgaria.tibroish.infrastructure.di.modules

import android.content.Context
import com.google.gson.Gson
import bg.dabulgaria.tibroish.BuildConfig
import bg.dabulgaria.tibroish.R
import bg.dabulgaria.tibroish.domain.config.IAppConfigRepository
import bg.dabulgaria.tibroish.infrastructure.BuildConstants
import bg.dabulgaria.tibroish.persistence.remote.api.ApiInterceptor
import bg.dabulgaria.tibroish.persistence.remote.api.TiBroishApiController
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
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

        val agent = "Ti Broish ${BuildConfig.FLAVOR} ${BuildConfig.VERSION_NAME} ${BuildConfig.VERSION_CODE}"

        val okHttpClientBuilder= OkHttpClient.Builder()
                .connectTimeout(2L, TimeUnit.MINUTES)
                .readTimeout(2L, TimeUnit.MINUTES)
                .writeTimeout(2L, TimeUnit.MINUTES)
                .addInterceptor(ApiInterceptor(agent))

        if (BuildConfig.DEBUG)
            okHttpClientBuilder.addInterceptor(HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY))

        return okHttpClientBuilder.build()
    }

    @Provides
    @Singleton
    fun providesTiBroishApiController( okHttpClient : OkHttpClient,
                                      gson : Gson,
                                      appConfigRepo: IAppConfigRepository): TiBroishApiController{

        val retrofit = Retrofit.Builder()
                .baseUrl( getTiBroishEndpoint(appConfigRepo) )
                .client( okHttpClient )
                .addConverterFactory( GsonConverterFactory.create( gson ) )
                .addCallAdapterFactory( RxJava3CallAdapterFactory.create() )
                .build()


        return retrofit.create(TiBroishApiController::class.java )
    }

    private fun getTiBroishEndpoint(appConfigRepo: IAppConfigRepository):String{

        val config = appConfigRepo.appConfig
        if(BuildConfig.FLAVOR == BuildConstants.PRODUCTION_FLAVOR)
            return config.apiBaseUrl
        else
            return config.apiBaseUrlStage
    }

}