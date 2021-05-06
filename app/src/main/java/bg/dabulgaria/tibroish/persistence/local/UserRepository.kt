package bg.dabulgaria.tibroish.persistence.local

import android.content.Context
import bg.dabulgaria.tibroish.domain.providers.ILogger
import bg.dabulgaria.tibroish.domain.user.IUserLocalRepository
import bg.dabulgaria.tibroish.domain.user.User
import bg.dabulgaria.tibroish.infrastructure.di.annotations.AppContext
import com.google.gson.Gson
import java.lang.Exception
import javax.inject.Inject

class UserRepository @Inject constructor( @AppContext context: Context,
                                          private val gson: Gson,
                                          private val logger:ILogger)
    : BasePreferencesRepo(context), IUserLocalRepository {

    override var user: User?
        get() {
            val json = preferences.getString(USER_KEY,"")
            try{
                if( json?.isNotEmpty() == true)
                return gson.fromJson(json, User::class.java)
            }
            catch (exception:Exception){
                logger.e(TAG, exception)
            }
            return null
        }
        set(value) {
            try{

                val json = if(value==null) "" else gson.toJson(value)
                val editor = preferences.edit()
                editor.putString(USER_KEY,json)
                editor.apply()
            }
            catch (exception:Exception){
                logger.e(TAG, exception)
            }
        }

    companion object{
        val USER_KEY = "bg.dabulgaria.tibroish.persistence.userrepository.user"
        val TAG = UserRepository::class.java.simpleName
    }
}