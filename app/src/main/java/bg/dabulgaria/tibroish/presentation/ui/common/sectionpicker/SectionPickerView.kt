package bg.dabulgaria.tibroish.presentation.ui.common.sectionpicker

import android.content.Context
import android.os.Build
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.view.View
import android.widget.AdapterView
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import bg.dabulgaria.tibroish.R
import kotlinx.android.synthetic.main.layout_sections_view.view.*

class SectionPickerView  :ConstraintLayout{

    //region construction
    constructor(context: Context): this(context, null)

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

    private fun initHomeAbroad(data: SectionsViewData, presenter: ISectionPickerPresenter){

        sectionInText.text = getSpannableStringRedWarnStar(context, R.string.section_in)

        localSectionsRadioButton.isChecked = data.viewType != SectionViewType.Abroad
        abroadSectionsRadioButton.isChecked = data.viewType == SectionViewType.Abroad

        localSectionsRadioButton.setOnClickListener {

            if(data.viewType == SectionViewType.Abroad)
                presenter.onAbroadChecked(false)
        }

        abroadSectionsRadioButton.setOnClickListener {

            if(data.viewType != SectionViewType.Abroad)
                presenter.onAbroadChecked(true)
        }
    }

    private fun initCountry(data: SectionsViewData, presenter: ISectionPickerPresenter){

        inputCountry.hint = getSpannableStringRedWarnStar(context, R.string.country)

        inputCountry.visibility = if(data.viewType == SectionViewType.Abroad)
            View.VISIBLE
        else
            View.GONE

        inputCountryDropdown.setText(data.mSelectedCountry?.name?:"", false)

        run {
            val adapter = CountriesAdapter(context, data.countries)
            inputCountryDropdown.setAdapter(adapter)
            inputCountryDropdown.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->

                val country = adapter.getItem(position)?:return@OnItemClickListener
                presenter.onCountrySelected(country)
                inputCountryDropdown.setText(country.name, /* filter= */ false)
                adapter.filter.filter("")
            }
        }
    }

    private fun initElectionRegion(data: SectionsViewData, presenter: ISectionPickerPresenter){

        inputMir.hint = getSpannableStringRedWarnStar(context, R.string.mir)

        inputMir.visibility = if(data.viewType != SectionViewType.Abroad)
            View.VISIBLE
        else
            View.GONE

        inputMirDropdown.setText(data.mSelectedElectionRegion?.name?:"", false)
        run {
            val adapter = ElectionRegionsAdapter(context, data.electionRegions)
            inputMirDropdown.setAdapter(adapter)
            inputMirDropdown.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->

                val mir = adapter.getItem(position) ?:return@OnItemClickListener
                presenter.onElectionRegionSelected(mir)
                inputMirDropdown.setText(mir.name, /* filter= */ false)
                adapter.filter.filter("")
            }
        }
    }

    private fun initMunicipalities(data: SectionsViewData, presenter: ISectionPickerPresenter){

        inputMunicipality.hint = getSpannableStringRedWarnStar(context, R.string.municipality)
        inputMunicipality.visibility = if(data.viewType != SectionViewType.Abroad)
            View.VISIBLE
        else
            View.GONE

        inputMunicipalityDropdown.isEnabled = data.mSelectedElectionRegion != null
        inputMunicipalityDropdown.setText(data.mSelectedMunicipality?.name?:"", false)

        run {
            val adapter = MunicipalitiesAdapter(context, data.municipalities)
            inputMunicipalityDropdown.setAdapter(adapter)
            inputMunicipalityDropdown.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->

                val munic = adapter.getItem(position) ?:return@OnItemClickListener
                presenter.onMunicipalitySelected(munic)
                inputMunicipalityDropdown.setText(munic.name, /* filter= */ false)
                adapter.filter.filter("")
            }
        }
    }

    private fun initTowns(data: SectionsViewData, presenter: ISectionPickerPresenter){

        inputTown.hint = getSpannableStringRedWarnStar(context, R.string.town)
        inputTownDropdown.isEnabled = data.mSelectedCountry != null || data.mSelectedMunicipality != null
        inputTownDropdown.setText(data.mSelectedTown?.name?:"", false)

        run {
            val adapter = TownsAdapter(context, data.towns)
            inputTownDropdown.setAdapter(adapter)
            inputTownDropdown.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->

                val town = adapter.getItem(position) ?:return@OnItemClickListener
                presenter.onTownSelected(town)
                inputTownDropdown.setText(town.name, /* filter= */ false)
                adapter.filter.filter("")
            }
        }
    }

    private fun initCityRegions(data: SectionsViewData, presenter: ISectionPickerPresenter){

        inputCityRegion.hint = getSpannableStringRedWarnStar(context, R.string.region)
        inputCityRegion.visibility = if(data.viewType == SectionViewType.HomeCityRegion)
            View.VISIBLE
        else
            View.GONE

        inputCityRegionDropdown.isEnabled = data.mSelectedTown != null
        inputCityRegionDropdown.setText(data.mSelectedCityRegion?.name?:"", false)

        run {
            val adapter = CityRegionsAdapter(context, data.cityRegions)
            inputCityRegionDropdown.setAdapter(adapter)
            inputCityRegionDropdown.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->

                val cRegion = adapter.getItem(position) ?:return@OnItemClickListener
                presenter.onCityRegionSelected(cRegion)
                inputCityRegionDropdown.setText(cRegion.name, /* filter= */ false)
                adapter.filter.filter("")
            }
        }
    }

    private fun initSections(data: SectionsViewData, presenter: ISectionPickerPresenter){

        inputSection.hint = getSpannableStringRedWarnStar(context, R.string.section_number)

        inputSectionDropdown.isEnabled = ( data.viewType != SectionViewType.HomeCityRegion
                && data.mSelectedTown != null) || data.mSelectedCityRegion != null

        inputSectionDropdown.setText(data.mSelectedSection?.code?:"", false)

        run {
            val adapter = SectionsAdapter(context, data.sections)
            inputSectionDropdown.setAdapter(adapter)
            inputSectionDropdown.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->

                val section = adapter.getItem(position) ?:return@OnItemClickListener
                data.mSelectedSection = section
                presenter.onSectionSelected(section)
                inputSectionDropdown.setText(section.code, /* filter= */ false)
                uniqueSectionValueTextView.setText( section.id )
                adapter.filter.filter("")
            }
        }
    }

    private fun initUniqueSection(data: SectionsViewData)
    {
        uniqueSectionLabelTextView.text = getSpannableStringRedWarnStar(context, R.string.unique_section_number)
        uniqueSectionValueTextView.setText( (data.mSelectedSection?.id ?:"").toString() )
    }

    private fun getSpannableStringRedWarnStar( context:Context, @StringRes stringRes:Int): SpannableString {

        val sectionInText = context.getString(stringRes)

        val color:Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context.resources.getColor(R.color.textRed, null)
        } else {
            context.resources.getColor(R.color.textRed)
        }

        val spannableString = SpannableString( "$sectionInText *" )
        spannableString.setSpan(ForegroundColorSpan(color),
                sectionInText.length + 1,
                sectionInText.length + 2,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return spannableString
    }

}