package bg.dabulgaria.tibroish.domain.locations

import java.io.Serializable

enum class SectionViewType: Serializable {
    Home, HomeCityRegion, Abroad
}

class SectionsViewData(var viewType: SectionViewType,
                       var countries: List<CountryRemote> = emptyList(),
                       var electionRegions: List<ElectionRegionRemote> = emptyList(),
                       var municipalities: List<MunicipalityRemote> = emptyList(),
                       var towns: List<TownRemote> = emptyList(),
                       var cityRegions: List<CityRegionRemote> = emptyList(),
                       var sections: List<SectionRemote> = emptyList()) : Serializable {

    var selectedCountry: CountryRemote? = null
    var selectedElectionRegion: ElectionRegionRemote? = null
    var selectedMunicipality: MunicipalityRemote? = null
    var selectedTown: TownRemote? = null
    var selectedCityRegion: CityRegionRemote? = null
    var selectedSection: SectionRemote? = null
    var hideUniqueUntilSectionIsSelected = false
    var isSectionRequired = true

    constructor(data: SectionsViewData) : this(
            data.viewType,
            data.countries.toList(),
            data.electionRegions.toList(),
            data.municipalities.toList(),
            data.towns.toList(),
            data.cityRegions.toList(),
            data.sections.toList(),
    ) {
        this.selectedCountry = data.selectedCountry
        this.selectedElectionRegion = data.selectedElectionRegion
        this.selectedMunicipality = data.selectedMunicipality
        this.selectedTown = data.selectedTown
        this.selectedCityRegion = data.selectedCityRegion
        this.selectedSection = data.selectedSection
        this.hideUniqueUntilSectionIsSelected = data.hideUniqueUntilSectionIsSelected
        this.isSectionRequired = data.isSectionRequired
    }
}