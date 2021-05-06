package bg.dabulgaria.tibroish.persistence.local

import android.content.Context
import bg.dabulgaria.tibroish.domain.config.AppConfig
import bg.dabulgaria.tibroish.domain.config.IAppConfigRepository
import bg.dabulgaria.tibroish.infrastructure.di.annotations.AppContext
import java.util.*
import javax.inject.Inject

class AppConfigRepository @Inject constructor(@AppContext private val context: Context) :IAppConfigRepository {

    override val appConfig: AppConfig
        get() {
            val properties = Properties()
            val inputStream = context.getAssets().open("appconfig.properties")
            properties.load(inputStream)
            return AppConfig(properties.getProperty(API_BASE_URL_KEY),
                    properties.getProperty(API_BASE_URL_STAGE_KEY))
        }

    companion object{

        const val API_BASE_URL_KEY = "ApiBaseUrl"
        const val API_BASE_URL_STAGE_KEY = "ApiBaseUrlStage"
    }
}