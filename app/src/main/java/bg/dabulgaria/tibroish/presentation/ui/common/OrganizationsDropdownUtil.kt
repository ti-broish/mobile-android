package bg.dabulgaria.tibroish.presentation.ui.common

import android.content.Context
import android.widget.AdapterView
import android.widget.AutoCompleteTextView
import bg.dabulgaria.tibroish.domain.organisation.Organization
import bg.dabulgaria.tibroish.presentation.ui.registration.OrganizationsAdapter

class OrganizationsDropdownUtil : IOrganizationsDropdownUtil {
    override fun populateOrganizationsDropdown(
        context: Context,
        dropdown: AutoCompleteTextView,
        organizations: List<Organization>
    ) {
        val adapter = OrganizationsAdapter(context, organizations)
        dropdown.setAdapter(adapter)
        dropdown.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            dropdown.setText(adapter.getItem(position)?.name, /* filter= */ false)
            adapter.filter.filter(null)
        }
    }
}