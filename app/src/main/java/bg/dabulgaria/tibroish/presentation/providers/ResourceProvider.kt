package bg.dabulgaria.tibroish.presentation.providers

import android.content.Context
import androidx.annotation.StringRes

class ResourceProvider(private val context: Context?) : IResourceProvider {


    override fun getString(@StringRes stringResId: Int): String {

        return context?.getString(stringResId) ?: ""
    }

    override fun getString(@StringRes resId: Int, vararg formatArgs: Any): String{


        return context?.getString(resId, *formatArgs) ?:""
    }
}
