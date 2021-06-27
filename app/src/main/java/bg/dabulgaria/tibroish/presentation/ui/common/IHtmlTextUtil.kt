package bg.dabulgaria.tibroish.presentation.ui.common

import android.text.Spanned

interface IHtmlTextUtil {
    fun convertTextToHtml(text: String): Spanned
}
