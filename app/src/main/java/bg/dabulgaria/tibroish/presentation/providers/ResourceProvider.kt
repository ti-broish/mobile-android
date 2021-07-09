package bg.dabulgaria.tibroish.presentation.providers

import android.content.Context
import android.os.Build
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import bg.dabulgaria.tibroish.R

class ResourceProvider(private val context: Context) : IResourceProvider {


    override fun getString(@StringRes stringResId: Int): String {

        return context.getString(stringResId) ?: ""
    }

    override fun getString(@StringRes resId: Int, vararg formatArgs: Any): String{


        return context.getString(resId, *formatArgs) ?:""
    }

    override fun getColor(@ColorRes resId: Int): Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        context.resources.getColor(resId, null)
    } else {
        context.resources.getColor(resId)
    }
}
