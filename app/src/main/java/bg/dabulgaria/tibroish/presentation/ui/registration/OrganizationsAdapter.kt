package bg.dabulgaria.tibroish.presentation.ui.registration

import android.content.Context
import bg.dabulgaria.tibroish.presentation.ui.common.FilterableArrayAdapter

class OrganizationsAdapter(context: Context, objects: List<RegistrationOrganization>) :
    FilterableArrayAdapter<RegistrationOrganization>(context, objects) {

    override fun getFormattedTextForItem(item: RegistrationOrganization): String {
        return item.name
    }

    override fun doesItemMatchConstraint(
        item: RegistrationOrganization,
        constraint: CharSequence
    ): Boolean {
        return item.name.contains(constraint, /* ignoreCase= */ true)
    }
}