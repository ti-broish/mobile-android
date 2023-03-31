package bg.dabulgaria.tibroish.persistence.local

import android.content.Context
import bg.dabulgaria.tibroish.domain.ICountryCodesRepo
import bg.dabulgaria.tibroish.infrastructure.di.annotations.AppContext
import bg.dabulgaria.tibroish.presentation.ui.registration.Countries
import bg.dabulgaria.tibroish.presentation.ui.registration.CountryCode
import bg.dabulgaria.tibroish.utils.AssetReader
import com.google.gson.Gson
import javax.inject.Inject

class CountryCodesRepo @Inject constructor(
    @AppContext private val context: Context
) : ICountryCodesRepo {

    override fun getCountryCodes(): List<CountryCode>? {

        val json: String? = AssetReader().loadJsonFromAsset(
            "phone-codes.json",
            context.applicationContext.assets
        )
        val fromJson = Gson().fromJson(json, Countries::class.java)
        return fromJson.countries
    }
}