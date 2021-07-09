package bg.dabulgaria.tibroish.persistence.local

import android.content.Context
import android.content.SharedPreferences

open class BasePreferencesRepo(private val context: Context) {

    protected var preferences: SharedPreferences

    init {
        preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    companion object{
        const val PREFERENCES_NAME = "bg.dabulgaria.tibroish.preferences"
    }
}