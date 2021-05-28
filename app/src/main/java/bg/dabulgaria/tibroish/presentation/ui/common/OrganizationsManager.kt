package bg.dabulgaria.tibroish.presentation.ui.common

import bg.dabulgaria.tibroish.domain.organisation.ITiBorishRemoteRepository
import bg.dabulgaria.tibroish.domain.organisation.Organization
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class OrganizationsManager @Inject constructor(
    private val tiBroishRemoteRepository: ITiBorishRemoteRepository): IOrganizationsManager {

    override fun loadOrganizationsAsync(
        callback: (List<Organization>?) -> Unit
    ) {
        CoroutineScope(Dispatchers.Main).launch {
            val organizations: List<Organization> = fetchOrganizations(tiBroishRemoteRepository)
            callback(organizations)
        }
    }

    override fun getOrganizationWithName(
        organizationName: String,
        organizations: MutableList<Organization>?
    ): Organization? {
        return organizations?.firstOrNull {
            it.name == organizationName
        }
    }

    private suspend fun fetchOrganizations(
        tiBroishRemoteRepository: ITiBorishRemoteRepository
    ): List<Organization> {
        return withContext(Dispatchers.IO) {
            tiBroishRemoteRepository.getOrganisations()
        }
    }
}