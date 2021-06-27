package bg.dabulgaria.tibroish.persistence.local

import android.content.Context
import bg.dabulgaria.tibroish.domain.locations.ISelectedSectionLocalRepository
import bg.dabulgaria.tibroish.domain.locations.SectionsViewData
import bg.dabulgaria.tibroish.domain.providers.ILogger
import bg.dabulgaria.tibroish.infrastructure.di.annotations.AppContext
import com.google.gson.Gson
import java.lang.Exception
import javax.inject.Inject

class SelectedSectionLocalRepository
@Inject constructor(@AppContext context: Context,
                    private val logger: ILogger,
                    private val gson: Gson)
    : BasePreferencesRepo(context), ISelectedSectionLocalRepository {


    override var selectedSectionData: SectionsViewData?
        get() {
            try {
                val jsonString = preferences.getString(SELECTED_SECTION_DATA_KEY, "")
                if (jsonString.isNullOrEmpty())
                    return null

                return gson.fromJson(jsonString, SectionsViewData::class.java)
            } catch (th: Throwable) {
                logger.e(TAG, th)
            }
            return null
        }
        set(value) {
            try {
                var jsonString = ""
                value?.let { jsonString = gson.toJson(it) }

                val editor = preferences.edit()
                editor.putString(SELECTED_SECTION_DATA_KEY, jsonString)
                editor.apply()
            } catch (exception: Exception) {
                logger.e(UserRepository.TAG, exception)
            }

        }

    companion object {
        val TAG = SelectedSectionLocalRepository::class.java.simpleName
        val SELECTED_SECTION_DATA_KEY = "SelectedSectionLocalRepository.SELECTED_SECTION_DATA_KEY"
    }
}