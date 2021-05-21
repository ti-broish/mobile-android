package bg.dabulgaria.tibroish.presentation.ui.common.sectionpicker

import android.content.Context
import bg.dabulgaria.tibroish.domain.locations.*
import bg.dabulgaria.tibroish.presentation.ui.common.FilterableArrayAdapter


class CountriesAdapter(context: Context, objects: List<CountryRemote>)
    : FilterableArrayAdapter<CountryRemote>(context, objects.toMutableList()) {

    override fun getFormattedTextForItem(item: CountryRemote): String {
        return item.name
    }

    override fun doesItemMatchConstraint(item: CountryRemote, constraint: CharSequence): Boolean {
        return item.name.contains(constraint, /* ignoreCase= */ true)
    }
}

class ElectionRegionsAdapter(context: Context, objects: List<ElectionRegionRemote>)
    : FilterableArrayAdapter<ElectionRegionRemote>(context, objects.toMutableList()) {

    override fun getFormattedTextForItem(item: ElectionRegionRemote): String {
        return "${item.code} ${item.name}"
    }

    override fun doesItemMatchConstraint(item: ElectionRegionRemote, constraint: CharSequence): Boolean {
        return getFormattedTextForItem(item).contains(constraint, /* ignoreCase= */ true)
    }
}

class MunicipalitiesAdapter(context: Context, objects: List<MunicipalityRemote>)
    : FilterableArrayAdapter<MunicipalityRemote>(context, objects.toMutableList()) {

    override fun getFormattedTextForItem(item: MunicipalityRemote): String {
        return item.name
    }

    override fun doesItemMatchConstraint(item: MunicipalityRemote, constraint: CharSequence): Boolean {
        return item.name.contains(constraint, /* ignoreCase= */ true)
    }
}

class TownsAdapter(context: Context, objects: List<TownRemote>)
    : FilterableArrayAdapter<TownRemote>(context, objects.toMutableList()) {

    override fun getFormattedTextForItem(item: TownRemote): String {
        return item.name
    }

    override fun doesItemMatchConstraint(item: TownRemote, constraint: CharSequence): Boolean {
        return item.name.contains(constraint, /* ignoreCase= */ true)
    }
}

class CityRegionsAdapter(context: Context, objects: List<CityRegionRemote>)
    : FilterableArrayAdapter<CityRegionRemote>(context, objects.toMutableList()) {

    override fun getFormattedTextForItem(item: CityRegionRemote): String {
        return item.name
    }

    override fun doesItemMatchConstraint(item: CityRegionRemote, constraint: CharSequence): Boolean {
        return item.name.contains(constraint, /* ignoreCase= */ true)
    }
}

class SectionsAdapter(context: Context, objects: List<SectionRemote>)
    : FilterableArrayAdapter<SectionRemote>(context, objects.toMutableList()) {

    override fun getFormattedTextForItem(item: SectionRemote): String {
        return "${item.code} ${item.place}"
    }

    override fun doesItemMatchConstraint(item: SectionRemote, constraint: CharSequence): Boolean {
        return getFormattedTextForItem(item).contains(constraint, /* ignoreCase= */ true)
    }
}