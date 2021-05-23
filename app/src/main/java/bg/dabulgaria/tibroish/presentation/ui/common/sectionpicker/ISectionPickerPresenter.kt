package bg.dabulgaria.tibroish.presentation.ui.common.sectionpicker

import bg.dabulgaria.tibroish.domain.locations.*


interface ISectionPickerPresenter {

    fun onAbroadChecked(abroad:Boolean)

    fun onCountrySelected(country:CountryRemote)

    fun onElectionRegionSelected(electionRegion:ElectionRegionRemote)

    fun onMunicipalitySelected(municipality:MunicipalityRemote)

    fun onTownSelected(town: TownRemote)

    fun onCityRegionSelected(cityRegion:CityRegionRemote)
}