package bg.dabulgaria.tibroish.persistence.remote.repo

import bg.dabulgaria.tibroish.domain.auth.IRemoteRepoAuthenticator
import bg.dabulgaria.tibroish.domain.locations.CountryRemote
import bg.dabulgaria.tibroish.domain.locations.ElectionRegionRemote
import bg.dabulgaria.tibroish.domain.locations.SectionRemote
import bg.dabulgaria.tibroish.domain.locations.TownRemote
import bg.dabulgaria.tibroish.domain.organisation.ITiBorishRemoteRepository
import bg.dabulgaria.tibroish.domain.organisation.Organization
import bg.dabulgaria.tibroish.domain.user.User
import bg.dabulgaria.tibroish.persistence.remote.api.AcceptValues
import bg.dabulgaria.tibroish.persistence.remote.api.TiBroishApiController
import bg.dabulgaria.tibroish.persistence.remote.model.SectionsRequestParams
import bg.dabulgaria.tibroish.persistence.remote.model.TownsRequestParams
import javax.inject.Inject

class TiBroishRemoteRepository @Inject constructor(private val apiController: TiBroishApiController,
                                                   private val authenticator: IRemoteRepoAuthenticator )
    :ITiBorishRemoteRepository {

    override fun getOrganisations(): List<Organization> {
        return apiController.getOrganizations(AcceptValues.ApplicationJson.value)
                .execute().body()!!
    }


    override fun createUser(firebaseJwt: String, userData: User) {
        // TODO: add error handling
        apiController.createUser(getAuthorization(firebaseJwt), userData).execute()
    }


    override fun getCountries(): List<CountryRemote> {

    val list = authenticator.executeCall { token ->
           apiController.getCountries(getAuthorization(token))}

       return list!!.sortedBy { it.name }
   }

   override fun getElectionRegions(): List<ElectionRegionRemote> {

       val list = authenticator.executeCall { token ->
           apiController.getElectionRegions(getAuthorization(token))}!!

       list.map { it.municipalities= it.municipalities.sortedBy { munic-> munic.name } }

       return list.sortedBy { it.code }
   }

   override fun getTowns(params:TownsRequestParams): List<TownRemote> {

       val towns = authenticator.executeCall( { pParams, token ->
           apiController.getTowns(getAuthorization(token),
                   pParams.countryCode,
                   pParams.electionRegionCode,
                   pParams.municipalityCode)}, params)!!

       towns.map { it.cityRegions = it.cityRegions?.sortedBy { region-> region.code } }
       return towns.sortedBy { it.name }
   }

   override fun getSections(params:SectionsRequestParams): List<SectionRemote> {

       val sections = authenticator.executeCall( { pParams, token ->
           apiController.getSections(getAuthorization(token),
                   pParams.townId,
                   pParams.cityRegionCode)}, params) !!

       return sections.sortedBy { it.code }
   }

   private fun getAuthorization(idToken: String): String = "Bearer $idToken"

}