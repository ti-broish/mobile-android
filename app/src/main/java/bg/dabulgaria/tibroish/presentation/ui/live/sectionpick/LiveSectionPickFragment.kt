package bg.dabulgaria.tibroish.presentation.ui.live.sectionpick


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import bg.dabulgaria.tibroish.R
import bg.dabulgaria.tibroish.presentation.base.BasePresentableFragment
import bg.dabulgaria.tibroish.presentation.base.IBaseView
import bg.dabulgaria.tibroish.presentation.ui.common.item.send.*
import kotlinx.android.synthetic.main.fragment_live_section_pick.*
import javax.inject.Inject

interface ILiveSectionPickView: IBaseView{

    fun setSectionsData(data: LiveSectionPickViewData)

    fun onLoadingStateChange(isLoading: Boolean)

    fun hideSoftKeyboard()
}

class LiveSectionPickFragment @Inject constructor()
    : BasePresentableFragment<ILiveSectionPickView, ILiveSectionPickPresenter>(), ILiveSectionPickView {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_live_section_pick, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        liveSectionPickContinueBtn.setOnClickListener { presenter.onContinue() }
    }

    override fun setSectionsData(data: LiveSectionPickViewData) {

        liveSectionPickerView?.bindView(data.sectionsData, presenter)
    }

    override fun onError(errorMessage: String) {

        dialogUtil.showDismissableDialog(activity = requireActivity(), message = errorMessage){}
    }

    override fun onLoadingStateChange(isLoading: Boolean) {

        val visibility = if (isLoading) View.VISIBLE else View.GONE
        liveSectionPickProgressBar.visibility = visibility
        liveSectionPickProcessingOverlay.visibility = visibility
    }

    companion object {

        val TAG = LiveSectionPickFragment::class.java.simpleName

        fun newInstance(): LiveSectionPickFragment =LiveSectionPickFragment()
    }
}
