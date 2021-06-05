package bg.dabulgaria.tibroish.presentation.providers

import androidx.annotation.StringRes

interface IResourceProvider {

    fun getString(@StringRes stringResId: Int): String

    fun getString(@StringRes resId: Int, vararg formatArgs: Any): String
}
