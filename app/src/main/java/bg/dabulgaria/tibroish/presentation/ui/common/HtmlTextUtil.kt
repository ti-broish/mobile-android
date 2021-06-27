package bg.dabulgaria.tibroish.presentation.ui.common

import android.os.Build
import android.text.Html
import android.text.Spanned

class HtmlTextUtil : IHtmlTextUtil {
    override fun convertTextToHtml(text: String): Spanned {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT)
        } else {
            return Html.fromHtml(text)
        }
    }
}