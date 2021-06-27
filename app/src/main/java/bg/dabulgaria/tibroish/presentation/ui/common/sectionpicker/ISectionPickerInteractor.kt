package bg.dabulgaria.tibroish.presentation.ui.common.sectionpicker

import bg.dabulgaria.tibroish.domain.locations.*
import bg.dabulgaria.tibroish.domain.organisation.ITiBroishRemoteRepository
import bg.dabulgaria.tibroish.domain.providers.ILogger
import bg.dabulgaria.tibroish.persistence.remote.model.SectionsRequestParams
import bg.dabulgaria.tibroish.persistence.remote.model.TownsRequestParams
import bg.dabulgaria.tibroish.presentation.ui.common.item.send.SendItemInteractor
import javax.inject.Inject

interface ISectionPickerInteractor {

    fun loadSectionsData(oldData: SectionsViewData?): SectionsViewData

    fun onCountrySelected(oldData: SectionsViewData, country:CountryRemote): SectionsViewData

    fun onElectionRegionSelected(oldData: SectionsViewData, electionRegion:ElectionRegionRemote): SectionsViewData

    fun onMunicipalitySelected(oldData: SectionsViewData, municipality: MunicipalityRemote): SectionsViewData

    fun onTownSelected(oldData: SectionsViewData, town: TownRemote): SectionsViewData

    fun onCityRegionSelected(oldData: SectionsViewData, cityRegion: CityRegionRemote): SectionsViewData

    fun onSectionSelected(oldData: SectionsViewData, section: SectionRemote): SectionsViewData

    var autoFillSection: Boolean
}

class SectionPickerInteractor
@Inject constructor(private val apiRepo: ITiBroishRemoteRepository,
                    private val selectedSectionLocalRepo: ISelectedSectionLocalRepository,
                    private val logger: ILogger)
    :ISectionPickerInteractor{

    override var autoFillSection:Boolean = true

    override fun loadSectionsData(oldData: SectionsViewData?): SectionsViewData {

        val data = if(oldData!= null)
            SectionsViewData(oldData)
        else selectedSectionLocalRepo.selectedSectionData
                ?: SectionsViewData(SectionViewType.Home)

        if(data.countries.isEmpty() || data.electionRegions.isEmpty()) {

            val allCountries = apiRepo.getCountries()
            data.countries = allCountries.filter {
                if (data.viewType == SectionViewType.Abroad )
                    it.code != BG_CODE
                else
                    it.code == BG_CODE }
            data.electionRegions = apiRepo.getElectionRegions()
        }

        if(data.selectedCountry != null && data.towns.isEmpty()) {

            data.towns = apiRepo.getTowns(TownsRequestParams(
                    data.selectedCountry?.code ?: "", null, null))

            if(data.towns.size == 1)
                data.selectedTown = data.towns.first()
        }

        if(data.selectedElectionRegion != null && data.municipalities.isEmpty()) {

            data.selectedElectionRegion?.municipalities?.let { data.municipalities = it }

            if(data.municipalities.size == 1)
                data.selectedMunicipality = data.municipalities.first()
        }

        if(data.selectedMunicipality != null && data.towns.isEmpty())
            data.towns = apiRepo.getTowns(
                    TownsRequestParams(BG_CODE, data.selectedElectionRegion?.code, data.selectedMunicipality?.code))

        if(data.selectedTown != null){

            data.cityRegions = data.selectedTown?.cityRegions.orEmpty()
            val hasCityRegions = data.viewType != SectionViewType.Abroad && data.cityRegions.isNotEmpty()

            if(hasCityRegions)
                data.viewType = SectionViewType.HomeCityRegion
            else if(data.sections.isEmpty()){
                data.sections = apiRepo.getSections(SectionsRequestParams(data.selectedTown?.code?:-1, null))

                if(autoFillSection && data.sections.size == 1)
                    data.selectedSection = data.sections.first()

                if(data.viewType != SectionViewType.Abroad)
                    data.viewType = SectionViewType.Home
            }
        }

        if(data.selectedCityRegion != null && data.sections.isEmpty()) {

            data.sections = apiRepo.getSections(SectionsRequestParams(
                    data.selectedTown?.code ?: -1, data.selectedCityRegion?.code))

            if(autoFillSection && data.sections.size == 1)
                data.selectedSection = data.sections.first()
        }

        return data
    }

    override fun onCountrySelected(oldData: SectionsViewData, country:CountryRemote): SectionsViewData {

        val data = SectionsViewData(oldData)
        data.selectedElectionRegion = null
        data.selectedMunicipality = null
        data.selectedTown = null
        data.selectedCityRegion = null
        data.selectedSection = null
        data.selectedCountry = country
        data.towns = apiRepo.getTowns( TownsRequestParams( country.code, null, null ) )

        data.sections = emptyList()

        if(data.towns.size == 1) {

            data.selectedTown = data.towns.first()

            data.selectedTown?.id?.let {

                data.sections = apiRepo.getSections(SectionsRequestParams(it, null))
                if(autoFillSection && data.sections.size == 1)
                    data.selectedSection = data.sections.first()
            }
        }

        return data
    }

    override fun onElectionRegionSelected(oldData: SectionsViewData, electionRegion: ElectionRegionRemote): SectionsViewData {

        val data = SectionsViewData(oldData)
        data.selectedElectionRegion = electionRegion
        data.selectedMunicipality = null
        data.selectedTown = null
        data.selectedCityRegion = null
        data.selectedSection = null
        data.selectedCountry = null
        data.municipalities = electionRegion.municipalities

        if(data.municipalities.size == 1) {

            data.selectedMunicipality = data.municipalities.first()

            data.towns = apiRepo.getTowns(
                    TownsRequestParams( BG_CODE,
                            data.selectedElectionRegion?.code,
                            data.selectedMunicipality!!.code ) )

            if(data.towns.size == 1) {

                data.selectedTown = data.towns.first()
                data.cityRegions = data.selectedTown?.cityRegions.orEmpty()

                data.viewType = if(data.cityRegions.isNotEmpty())
                    SectionViewType.HomeCityRegion
                else
                    SectionViewType.Home
            }
        }

        data.sections = emptyList()

        return data
    }

    override fun onMunicipalitySelected(oldData: SectionsViewData, municipality: MunicipalityRemote): SectionsViewData {

        val data = SectionsViewData(oldData)
        data.selectedMunicipality = municipality
        data.towns = apiRepo.getTowns(
                TownsRequestParams( BG_CODE, data.selectedElectionRegion?.code, municipality.code ) )

        data.selectedTown = null
        data.selectedCityRegion = null
        data.selectedSection = null
        data.selectedCountry = null

        return data
    }

    override fun onTownSelected(oldData: SectionsViewData, town: TownRemote): SectionsViewData {

        val data = SectionsViewData(oldData)
        data.cityRegions = town.cityRegions.orEmpty()
        val hasCityRegions = data.viewType != SectionViewType.Abroad && data.cityRegions.isNotEmpty()

        data.selectedSection = null

        if(hasCityRegions)
            data.viewType = SectionViewType.HomeCityRegion
        else {
            data.sections = apiRepo.getSections(SectionsRequestParams(town.id, null))

            if(autoFillSection && data.sections.size == 1)
                data.selectedSection = data.sections.first()

            if(data.viewType != SectionViewType.Abroad)
                data.viewType = SectionViewType.Home
        }

        data.selectedTown = town
        data.selectedCityRegion = null

        return data
    }

    override fun onCityRegionSelected(oldData: SectionsViewData, cityRegion: CityRegionRemote): SectionsViewData {

        val data = SectionsViewData(oldData)

        data.sections = apiRepo.getSections(
                SectionsRequestParams(data.selectedTown?.id ?:-1, cityRegion.code ))

        data.selectedCityRegion = cityRegion
        data.selectedSection = null

        return data
    }

    override fun onSectionSelected(oldData: SectionsViewData, section: SectionRemote): SectionsViewData {

        val data = SectionsViewData(oldData)
        data.selectedSection = section
        return data
    }

    companion object{

        val BG_CODE = "00"
        private val TAG = SectionPickerInteractor::class.simpleName
    }

}