package bg.dabulgaria.tibroish.presentation.ui.common.sectionpicker

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import bg.dabulgaria.tibroish.R
import bg.dabulgaria.tibroish.domain.locations.SectionViewType
import bg.dabulgaria.tibroish.domain.locations.SectionsViewData
import bg.dabulgaria.tibroish.presentation.providers.getSpannableStringRedWarnStar
import bg.dabulgaria.tibroish.databinding.LayoutSectionsViewBinding

class SectionPickerView : ConstraintLayout {

    //region construction
    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(
        context: Context, attrs: AttributeSet?, defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        binding = LayoutSectionsViewBinding.inflate(LayoutInflater.from(context), this, true)
    }

    private lateinit var binding: LayoutSectionsViewBinding

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

        binding.sectionInText.text = R.string.section_in.getSpannableStringRedWarnStar(context)

        binding.localSectionsRadioButton.isChecked = data.viewType != SectionViewType.Abroad
        binding.abroadSectionsRadioButton.isChecked = data.viewType == SectionViewType.Abroad

        binding.localSectionsRadioButton.setOnClickListener {

            if (data.viewType == SectionViewType.Abroad)
                presenter.onAbroadChecked(false)
        }

        binding.abroadSectionsRadioButton.setOnClickListener {

            if (data.viewType != SectionViewType.Abroad)
                presenter.onAbroadChecked(true)
        }
    }

    private fun initCountry(data: SectionsViewData, presenter: ISectionPickerPresenter) {

        binding.inputCountry.hint = R.string.country.getSpannableStringRedWarnStar(context)

        binding.inputCountry.visibility = if (data.viewType == SectionViewType.Abroad)
            View.VISIBLE
        else
            View.GONE

        binding.inputCountryDropdown.setText(data.selectedCountry?.name ?: "", false)

        run {
            val adapter = CountriesAdapter(context, data.countries)
            binding.inputCountryDropdown.setAdapter(adapter)
            binding.inputCountryDropdown.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->

                val country = adapter.getItem(position) ?: return@OnItemClickListener
                presenter.onCountrySelected(country)
                binding.inputCountryDropdown.setText(country.name, /* filter= */ false)
                adapter.filter.filter("")
            }
        }
    }

    private fun initElectionRegion(data: SectionsViewData, presenter: ISectionPickerPresenter) {

        binding.inputMir.hint = R.string.mir.getSpannableStringRedWarnStar(context)

        binding.inputMir.visibility = if (data.viewType != SectionViewType.Abroad)
            View.VISIBLE
        else
            View.GONE

        binding.inputMirDropdown.setText(data.selectedElectionRegion?.name ?: "", false)
        run {
            val adapter = ElectionRegionsAdapter(context, data.electionRegions)
            binding.inputMirDropdown.setAdapter(adapter)
            binding.inputMirDropdown.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->

                val mir = adapter.getItem(position) ?: return@OnItemClickListener
                presenter.onElectionRegionSelected(mir)
                binding.inputMirDropdown.setText(mir.name, /* filter= */ false)
                adapter.filter.filter("")
            }
        }
    }

    private fun initMunicipalities(data: SectionsViewData, presenter: ISectionPickerPresenter) {

        binding.inputMunicipality.hint = R.string.municipality.getSpannableStringRedWarnStar(context)
        binding.inputMunicipality.visibility = if (data.viewType != SectionViewType.Abroad)
            View.VISIBLE
        else
            View.GONE

        binding.inputMunicipalityDropdown.isEnabled = data.selectedElectionRegion != null
        binding.inputMunicipalityDropdown.setText(data.selectedMunicipality?.name ?: "", false)

        run {
            val adapter = MunicipalitiesAdapter(context, data.municipalities)
            binding.inputMunicipalityDropdown.setAdapter(adapter)
            binding.inputMunicipalityDropdown.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->

                val munic = adapter.getItem(position) ?: return@OnItemClickListener
                presenter.onMunicipalitySelected(munic)
                binding.inputMunicipalityDropdown.setText(munic.name, /* filter= */ false)
                adapter.filter.filter("")
            }
        }
    }

    private fun initTowns(data: SectionsViewData, presenter: ISectionPickerPresenter) {

        binding.inputTown.hint = R.string.town.getSpannableStringRedWarnStar(context)
        binding.inputTownDropdown.isEnabled = data.selectedCountry != null || data.selectedMunicipality != null
        binding.inputTownDropdown.setText(data.selectedTown?.name ?: "", false)

        run {
            val adapter = TownsAdapter(context, data.towns)
            binding.inputTownDropdown.setAdapter(adapter)
            binding.inputTownDropdown.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->

                val town = adapter.getItem(position) ?: return@OnItemClickListener
                presenter.onTownSelected(town)
                binding.inputTownDropdown.setText(town.name, /* filter= */ false)
                adapter.filter.filter("")
            }
        }
    }

    private fun initCityRegions(data: SectionsViewData, presenter: ISectionPickerPresenter) {

        binding.inputCityRegion.hint = R.string.region.getSpannableStringRedWarnStar(context)
        binding.inputCityRegion.visibility = if (data.viewType == SectionViewType.HomeCityRegion)
            View.VISIBLE
        else
            View.GONE

        binding.inputCityRegionDropdown.isEnabled = data.selectedTown != null
        binding.inputCityRegionDropdown.setText(data.selectedCityRegion?.name ?: "", false)

        run {
            val adapter = CityRegionsAdapter(context, data.cityRegions)
            binding.inputCityRegionDropdown.setAdapter(adapter)
            binding.inputCityRegionDropdown.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->

                val cRegion = adapter.getItem(position) ?: return@OnItemClickListener
                presenter.onCityRegionSelected(cRegion)
                binding.inputCityRegionDropdown.setText(cRegion.name, /* filter= */ false)
                adapter.filter.filter("")
            }
        }
    }

    private fun initSections(data: SectionsViewData, presenter: ISectionPickerPresenter) {

        @StringRes val resId = R.string.section_number
        binding.inputSection.hint = if(data.isSectionRequired)
            resId.getSpannableStringRedWarnStar(context)
        else
            context.getString(resId)

        binding.inputSectionDropdown.isEnabled = (data.viewType != SectionViewType.HomeCityRegion
                && data.selectedTown != null) || data.selectedCityRegion != null

        binding.inputSectionDropdown.setText(data.selectedSection?.code ?: "", false)

        run {
            val adapter = SectionsAdapter(context, data.sections)
            binding.inputSectionDropdown.setAdapter(adapter)
            binding.inputSectionDropdown.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->

                val section = adapter.getItem(position) ?: return@OnItemClickListener
                data.selectedSection = section
                presenter.onSectionSelected(section)
                binding.inputSectionDropdown.setText(section.code, /* filter= */ false)
                binding.uniqueSectionValueTextView.setText(section.id)
                adapter.filter.filter("")
            }
        }
    }

    private fun initUniqueSection(data: SectionsViewData) {

        val visibility = if (data.hideUniqueUntilSectionIsSelected && data.selectedSection == null)
            View.GONE
        else
            View.VISIBLE

        binding.uniqueSectionLabelTextView.visibility = visibility
        binding.uniqueSectionValueTextView.visibility = visibility

        binding.uniqueSectionValueTextView.isEnabled = false

        @StringRes val resId = R.string.unique_section_number
        binding.uniqueSectionLabelTextView.text = if (data.isSectionRequired)
            resId.getSpannableStringRedWarnStar(context)
        else
            context.getString(resId)

        binding.uniqueSectionValueTextView.setText((data.selectedSection?.id ?: "").toString())
    }


}