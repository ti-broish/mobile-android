package bg.dabulgaria.tibroish.presentation.ui.registration

import android.content.Context
import bg.dabulgaria.tibroish.presentation.ui.common.FilterableArrayAdapter

class OrganizationsAdapter(context: Context, objects: List<Organization>)
    : FilterableArrayAdapter<Organization>(context, objects) {

    override fun getFormattedTextForItem(item: Organization): String {
        return item.name!!
    }

    override fun doesItemMatchConstraint(item: Organization, constraint: CharSequence): Boolean {
        return item.name!!.contains(constraint, /* ignoreCase= */ true)
    }
}