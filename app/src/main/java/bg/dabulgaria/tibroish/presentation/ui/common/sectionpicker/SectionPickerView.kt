package bg.dabulgaria.tibroish.presentation.ui.common.sectionpicker

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.AdapterView
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import bg.dabulgaria.tibroish.R
import bg.dabulgaria.tibroish.domain.locations.SectionViewType
import bg.dabulgaria.tibroish.domain.locations.SectionsViewData
import bg.dabulgaria.tibroish.presentation.providers.getSpannableStringRedWarnStar
import kotlinx.android.synthetic.main.layout_sections_view.view.*

class SectionPickerView : ConstraintLayout {

    //region construction
    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {

        View.inflate(context, R.layout.layout_sections_view, this)
    }
    //endregion construction

    fun bindView(sectionsViewData: SectionsViewData?, presenter: ISectionPickerPresenter) {

        val data = sectionsViewData ?: return

        initHomeAbroad(data, presenter)

        initCountry(data, presenter)

        initElectionRegion(data, presenter)

        initMunicipalities(data, presenter)

        initTowns(data, presenter)

        initCityRegions(data, presenter)

        initSections(data, presenter)

        initUniqueSection(data)
    }
    //region private methods

    private fun initHomeAbroad(data: SectionsViewData, presenter: ISectionPickerPresenter) {

        sectionInText.text = R.string.section_in.getSpannableStringRedWarnStar(context)

        localSectionsRadioButton.isChecked = data.viewType != SectionViewType.Abroad
        abroadSectionsRadioButton.isChecked = data.viewType == SectionViewType.Abroad

        localSectionsRadioButton.setOnClickListener {

            if (data.viewType == SectionViewType.Abroad)
                presenter.onAbroadChecked(false)
        }

        abroadSectionsRadioButton.setOnClickListener {

            if (data.viewType != SectionViewType.Abroad)
                presenter.onAbroadChecked(true)
        }
    }

    private fun initCountry(data: SectionsViewData, presenter: ISectionPickerPresenter) {

        inputCountry.hint = R.string.country.getSpannableStringRedWarnStar(context)

                inputCountry.visibility = if (data.viewType == SectionViewType.Abroad)
            View.VISIBLE
        else
            View.GONE

        inputCountryDropdown.setText(data.selectedCountry?.name ?: "", false)

        run {
            val adapter = CountriesAdapter(context, data.countries)
            inputCountryDropdown.setAdapter(adapter)
            inputCountryDropdown.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->

                val country = adapter.getItem(position) ?: return@OnItemClickListener
                presenter.onCountrySelected(country)
                inputCountryDropdown.setText(country.name, /* filter= */ false)
                adapter.filter.filter("")
            }
        }
    }

    private fun initElectionRegion(data: SectionsViewData, presenter: ISectionPickerPresenter) {

        inputMir.hint = R.string.mir.getSpannableStringRedWarnStar(context)

                inputMir.visibility = if (data.viewType != SectionViewType.Abroad)
            View.VISIBLE
        else
            View.GONE

        inputMirDropdown.setText(data.selectedElectionRegion?.name ?: "", false)
        run {
            val adapter = ElectionRegionsAdapter(context, data.electionRegions)
            inputMirDropdown.setAdapter(adapter)
            inputMirDropdown.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->

                val mir = adapter.getItem(position) ?: return@OnItemClickListener
                presenter.onElectionRegionSelected(mir)
                inputMirDropdown.setText(mir.name, /* filter= */ false)
                adapter.filter.filter("")
            }
        }
    }

    private fun initMunicipalities(data: SectionsViewData, presenter: ISectionPickerPresenter) {

        inputMunicipality.hint = R.string.municipality.getSpannableStringRedWarnStar(context)
        inputMunicipality.visibility = if (data.viewType != SectionViewType.Abroad)
            View.VISIBLE
        else
            View.GONE

        inputMunicipalityDropdown.isEnabled = data.selectedElectionRegion != null
        inputMunicipalityDropdown.setText(data.selectedMunicipality?.name ?: "", false)

        run {
            val adapter = MunicipalitiesAdapter(context, data.municipalities)
            inputMunicipalityDropdown.setAdapter(adapter)
            inputMunicipalityDropdown.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->

                val munic = adapter.getItem(position) ?: return@OnItemClickListener
                presenter.onMunicipalitySelected(munic)
                inputMunicipalityDropdown.setText(munic.name, /* filter= */ false)
                adapter.filter.filter("")
            }
        }
    }

    private fun initTowns(data: SectionsViewData, presenter: ISectionPickerPresenter) {

        inputTown.hint = R.string.town.getSpannableStringRedWarnStar(context)
        inputTownDropdown.isEnabled = data.selectedCountry != null || data.selectedMunicipality != null
        inputTownDropdown.setText(data.selectedTown?.name ?: "", false)

        run {
            val adapter = TownsAdapter(context, data.towns)
            inputTownDropdown.setAdapter(adapter)
            inputTownDropdown.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->

                val town = adapter.getItem(position) ?: return@OnItemClickListener
                presenter.onTownSelected(town)
                inputTownDropdown.setText(town.name, /* filter= */ false)
                adapter.filter.filter("")
            }
        }
    }

    private fun initCityRegions(data: SectionsViewData, presenter: ISectionPickerPresenter) {

        inputCityRegion.hint = R.string.region.getSpannableStringRedWarnStar(context)
        inputCityRegion.visibility = if (data.viewType == SectionViewType.HomeCityRegion)
            View.VISIBLE
        else
            View.GONE

        inputCityRegionDropdown.isEnabled = data.selectedTown != null
        inputCityRegionDropdown.setText(data.selectedCityRegion?.name ?: "", false)

        run {
            val adapter = CityRegionsAdapter(context, data.cityRegions)
            inputCityRegionDropdown.setAdapter(adapter)
            inputCityRegionDropdown.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->

                val cRegion = adapter.getItem(position) ?: return@OnItemClickListener
                presenter.onCityRegionSelected(cRegion)
                inputCityRegionDropdown.setText(cRegion.name, /* filter= */ false)
                adapter.filter.filter("")
            }
        }
    }

    private fun initSections(data: SectionsViewData, presenter: ISectionPickerPresenter) {

        @StringRes val resId = R.string.section_number
        inputSection.hint = if(data.isSectionRequired)
            resId.getSpannableStringRedWarnStar(context)
        else
            context.getString(resId)

        inputSectionDropdown.isEnabled = (data.viewType != SectionViewType.HomeCityRegion
                && data.selectedTown != null) || data.selectedCityRegion != null

        inputSectionDropdown.setText(data.selectedSection?.code ?: "", false)

        run {
            val adapter = SectionsAdapter(context, data.sections)
            inputSectionDropdown.setAdapter(adapter)
            inputSectionDropdown.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->

                val section = adapter.getItem(position) ?: return@OnItemClickListener
                data.selectedSection = section
                presenter.onSectionSelected(section)
                inputSectionDropdown.setText(section.code, /* filter= */ false)
                uniqueSectionValueTextView.setText(section.id)
                adapter.filter.filter("")
            }
        }
    }

    private fun initUniqueSection(data: SectionsViewData) {

        val visibility = if (data.hideUniqueUntilSectionIsSelected && data.selectedSection == null)
            View.GONE
        else
            View.VISIBLE

        uniqueSectionLabelTextView.visibility = visibility
        uniqueSectionValueTextView.visibility = visibility

        uniqueSectionValueTextView.isEnabled = false

        @StringRes val resId = R.string.unique_section_number
        uniqueSectionLabelTextView.text = if (data.isSectionRequired)
            resId.getSpannableStringRedWarnStar(context)
        else
            context.getString(resId)

        uniqueSectionValueTextView.setText((data.selectedSection?.id ?: "").toString())
    }


}