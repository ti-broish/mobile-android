package bg.dabulgaria.tibroish.domain.organisation


interface IOrganisationRepository {

    fun getOrganisations():List<Organization>
}