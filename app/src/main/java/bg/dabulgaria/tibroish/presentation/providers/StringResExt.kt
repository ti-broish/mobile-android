package bg.dabulgaria.tibroish.presentation.providers

import android.content.Context
import android.os.Build
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.annotation.StringRes
import bg.dabulgaria.tibroish.R

fun @receiver:StringRes Int.getSpannableStringRedWarnStar(context: Context): SpannableString {

    val sectionInText = context.getString(this)

    val color: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        context.resources.getColor(R.color.textRed, null)
    } else {
        context.resources.getColor(R.color.textRed)
    }

    val spannableString = SpannableString("$sectionInText *")
    spannableString.setSpan(ForegroundColorSpan(color),
            sectionInText.length + 1,
            sectionInText.length + 2,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    return spannableString
}