package bg.dabulgaria.tibroish.presentation.ui.common.sectionpicker

import bg.dabulgaria.tibroish.domain.locations.*
import bg.dabulgaria.tibroish.domain.organisation.ITiBroishRemoteRepository
import bg.dabulgaria.tibroish.persistence.remote.model.SectionsRequestParams
import bg.dabulgaria.tibroish.persistence.remote.model.TownsRequestParams
import javax.inject.Inject

interface ISectionPickerInteractor {

    fun loadSectionsData(oldData:SectionsViewData?):SectionsViewData

    fun onCountrySelected(oldData:SectionsViewData, country:CountryRemote):SectionsViewData

    fun onElectionRegionSelected(oldData:SectionsViewData,electionRegion:ElectionRegionRemote): SectionsViewData

    fun onMunicipalitySelected(oldData:SectionsViewData,municipality: MunicipalityRemote): SectionsViewData

    fun onTownSelected(oldData:SectionsViewData,town: TownRemote): SectionsViewData

    fun onCityRegionSelected(oldData:SectionsViewData,cityRegion: CityRegionRemote): SectionsViewData
}

class SectionPickerInteractor @Inject constructor(private val apiRepo: ITiBroishRemoteRepository) :ISectionPickerInteractor{

    override fun loadSectionsData(oldData:SectionsViewData?):SectionsViewData{

        val data = if( oldData!= null )
            SectionsViewData(oldData)
        else
            SectionsViewData( SectionViewType.Home)

        if(data.countries.isEmpty() || data.electionRegions.isEmpty()) {

            val allCountries = apiRepo.getCountries()
            data.countries = allCountries.filter {
                if (data.viewType == SectionViewType.Abroad )
                    it.code != BG_CODE
                else
                    it.code == BG_CODE }
            data.electionRegions = apiRepo.getElectionRegions()
        }

        if(data.mSelectedCountry != null && data.towns.isEmpty())
            data.towns = apiRepo.getTowns( TownsRequestParams(
                    data.mSelectedCountry?.code?:"", null, null ) )

        if(data.mSelectedElectionRegion != null && data.municipalities.isEmpty())
            data.mSelectedElectionRegion?.municipalities?.let{ data.municipalities }

        if(data.mSelectedMunicipality != null && data.municipalities.isEmpty())
            data.towns = apiRepo.getTowns(
                    TownsRequestParams( BG_CODE, data.mSelectedElectionRegion?.code, data.mSelectedMunicipality?.code ) )

        if(data.mSelectedTown != null){

            data.cityRegions = data.mSelectedTown?.cityRegions.orEmpty()
            val hasCityRegions = data.viewType != SectionViewType.Abroad && data.cityRegions.isNotEmpty()

            if(hasCityRegions)
                data.viewType = SectionViewType.HomeCityRegion
            else {
                data.sections = apiRepo.getSections(SectionsRequestParams(data.mSelectedTown?.code?:-1, null))

                if(data.viewType != SectionViewType.Abroad)
                    data.viewType = SectionViewType.Home
            }
        }

        if(data.mSelectedCityRegion != null && data.sections.isEmpty())
            data.sections = apiRepo.getSections(SectionsRequestParams(
                    data.mSelectedTown?.code ?:-1, data.mSelectedCityRegion?.code ))

        return data
    }

    override fun onCountrySelected(oldData:SectionsViewData, country:CountryRemote):SectionsViewData{

        val data = SectionsViewData(oldData)
        data.mSelectedElectionRegion = null
        data.mSelectedMunicipality = null
        data.mSelectedTown = null
        data.mSelectedCityRegion = null
        data.mSelectedSection = null
        data.mSelectedCountry = country
        data.towns = apiRepo.getTowns( TownsRequestParams( country.code, null, null ) )
        data.sections = emptyList()

        return data
    }

    override fun onElectionRegionSelected(oldData: SectionsViewData, electionRegion: ElectionRegionRemote): SectionsViewData {

        val data = SectionsViewData(oldData)
        data.mSelectedElectionRegion = electionRegion
        data.mSelectedMunicipality = null
        data.mSelectedTown = null
        data.mSelectedCityRegion = null
        data.mSelectedSection = null
        data.mSelectedCountry = null
        data.municipalities = electionRegion.municipalities
        data.sections = emptyList()

        return data
    }

    override fun onMunicipalitySelected(oldData: SectionsViewData, municipality: MunicipalityRemote): SectionsViewData {

        val data = SectionsViewData(oldData)
        data.mSelectedMunicipality = municipality
        data.towns = apiRepo.getTowns(
                TownsRequestParams( BG_CODE, data.mSelectedElectionRegion?.code, municipality.code ) )

        data.mSelectedTown = null
        data.mSelectedCityRegion = null
        data.mSelectedSection = null
        data.mSelectedCountry = null

        return data
    }

    override fun onTownSelected(oldData: SectionsViewData, town: TownRemote): SectionsViewData {

        val data = SectionsViewData(oldData)
        data.cityRegions = town.cityRegions.orEmpty()
        val hasCityRegions = data.viewType != SectionViewType.Abroad && data.cityRegions.isNotEmpty()

        if(hasCityRegions)
            data.viewType = SectionViewType.HomeCityRegion
        else {
            data.sections = apiRepo.getSections(SectionsRequestParams(town.id, null))

            if(data.viewType != SectionViewType.Abroad)
                data.viewType = SectionViewType.Home
        }

        data.mSelectedTown = town
        data.mSelectedCityRegion = null
        data.mSelectedSection = null

        return data
    }

    override fun onCityRegionSelected(oldData: SectionsViewData, cityRegion: CityRegionRemote): SectionsViewData {

        val data = SectionsViewData(oldData)

        data.sections = apiRepo.getSections(
                SectionsRequestParams(data.mSelectedTown?.id ?:-1, cityRegion.code ))

        data.mSelectedCityRegion = cityRegion
        data.mSelectedSection = null

        return data
    }

    companion object{

        val BG_CODE = "00"
    }

}