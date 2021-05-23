package bg.dabulgaria.tibroish.presentation.ui.common.sectionpicker

import bg.dabulgaria.tibroish.domain.locations.*
import java.io.Serializable

enum class SectionViewType{
    Home, HomeCityRegion, Abroad
}

class SectionsViewData(var viewType:SectionViewType,
                       var countries:List<CountryRemote> = emptyList(),
                       var electionRegions:List<ElectionRegionRemote> = emptyList(),
                       var municipalities:List<MunicipalityRemote> = emptyList(),
                       var towns:List<TownRemote> = emptyList(),
                       var cityRegions:List<CityRegionRemote> = emptyList(),
                       var sections: List<SectionRemote> = emptyList()):Serializable {

    var mSelectedCountry:CountryRemote?=null
    var mSelectedElectionRegion:ElectionRegionRemote?=null
    var mSelectedMunicipality: MunicipalityRemote?=null
    var mSelectedTown:TownRemote?=null
    var mSelectedCityRegion:CityRegionRemote?=null
    var mSelectedSection:SectionRemote?=null

    constructor( data:SectionsViewData):this(
             data.viewType,
             data.countries.toList(),
             data.electionRegions.toList(),
             data.municipalities.toList(),
             data.towns.toList(),
             data.cityRegions.toList(),
             data.sections.toList(),
    ){
        this.mSelectedCountry = data.mSelectedCountry
        this.mSelectedElectionRegion = data.mSelectedElectionRegion
        this.mSelectedMunicipality = data.mSelectedMunicipality
        this.mSelectedTown = data.mSelectedTown
        this.mSelectedCityRegion = data.mSelectedCityRegion
        this.mSelectedSection = data.mSelectedSection
    }
}