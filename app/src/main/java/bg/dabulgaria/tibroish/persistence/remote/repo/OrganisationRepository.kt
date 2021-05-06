package bg.dabulgaria.tibroish.persistence.remote.repo

import bg.dabulgaria.tibroish.domain.organisation.IOrganisationRepository
import bg.dabulgaria.tibroish.domain.organisation.Organization
import bg.dabulgaria.tibroish.persistence.remote.AcceptValues
import bg.dabulgaria.tibroish.persistence.remote.api.TiBroishApiController
import javax.inject.Inject

class OrganisationRepository @Inject constructor(private val tiBroishApiController: TiBroishApiController )
    :IOrganisationRepository {

    override fun getOrganisations(): List<Organization> {
        return tiBroishApiController.getOrganizations(AcceptValues.ApplicationJson.value)
                .execute().body()!!
    }
}