package bg.dabulgaria.tibroish.presentation.ui.common

import bg.dabulgaria.tibroish.domain.organisation.Organization

interface IOrganizationsManager {
    fun loadOrganizationsAsync(
        callback: (List<Organization>?) -> Unit)

    fun getOrganizationWithName(
        organizationName: String,
        organizations: MutableList<Organization>?): Organization?
}