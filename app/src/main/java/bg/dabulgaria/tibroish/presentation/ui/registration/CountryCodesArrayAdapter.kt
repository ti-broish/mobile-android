package bg.dabulgaria.tibroish.presentation.ui.registration

import android.content.Context
import bg.dabulgaria.tibroish.presentation.ui.common.FilterableArrayAdapter

class CountryCodesArrayAdapter(context: Context, objects: List<CountryCode>)
    : FilterableArrayAdapter<CountryCode>(context, objects.toMutableList()) {

    companion object {
        private const val DEFAULT_SELECTED_COUNTRY: String = "Bulgaria"
    }

    fun getDefaultSelectedItem(): CountryCode {
        return getInitialObjects().find { countryCode ->
            countryCode.name.equals(DEFAULT_SELECTED_COUNTRY)
        } ?: throw IllegalStateException(
                "Country $DEFAULT_SELECTED_COUNTRY not present in the country list.")
    }

    override fun getFormattedTextForItem(item: CountryCode): String {
        return item.code + " " + item.name
    }

    override fun doesItemMatchConstraint(item: CountryCode, constraint: CharSequence): Boolean {
        return item.name!!.contains(constraint, /* ignoreCase= */ true)
                || item.code!!.contains(constraint, /* ignoreCase= */ true)
    }
}