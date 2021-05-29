package bg.dabulgaria.tibroish.presentation.ui.common

import android.content.Context
import android.widget.AutoCompleteTextView
import bg.dabulgaria.tibroish.domain.organisation.Organization

interface IOrganizationsDropdownUtil {
    fun populateOrganizationsDropdown(
        context: Context,
        dropdown: AutoCompleteTextView,
        organizations: List<Organization>
    )
}