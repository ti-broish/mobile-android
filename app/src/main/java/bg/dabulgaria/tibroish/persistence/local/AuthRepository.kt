package bg.dabulgaria.tibroish.persistence.local

import android.content.Context
import bg.dabulgaria.tibroish.domain.providers.ILogger
import bg.dabulgaria.tibroish.domain.user.IAuthRepository
import bg.dabulgaria.tibroish.infrastructure.di.annotations.AppContext
import java.lang.Exception
import javax.inject.Inject

class AuthRepository @Inject constructor(@AppContext context: Context, private val logger: ILogger)
    :BasePreferencesRepo(context), IAuthRepository {

    override var token: String
        get() = preferences.getString(TOKEN_KEY, "") ?:""
        set(value) {
            try{

                val editor = preferences.edit()
                editor.putString(TOKEN_KEY,value)
                editor.apply()
            }
            catch (exception: Exception){
                logger.e(UserRepository.TAG, exception)
            }
        }

    companion object{
        val TOKEN_KEY = "bg.dabulgaria.tibroish.persistence.authrepository.token"
        val TAG = AuthRepository::class.java.simpleName
    }
}