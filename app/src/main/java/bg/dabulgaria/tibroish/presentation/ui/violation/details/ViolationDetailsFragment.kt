package bg.dabulgaria.tibroish.presentation.ui.violation.details

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bg.dabulgaria.tibroish.R
import bg.dabulgaria.tibroish.domain.violation.VoteViolationRemote
import bg.dabulgaria.tibroish.presentation.base.BasePresentableFragment
import bg.dabulgaria.tibroish.presentation.base.IBaseView
import bg.dabulgaria.tibroish.presentation.ui.common.IStatusColorUtil
import bg.dabulgaria.tibroish.databinding.FragmentViolationDetailsBinding
import javax.inject.Inject

interface IViolationDetailsView : IBaseView {

}

class ViolationDetailsFragment : BasePresentableFragment<IViolationDetailsView,
        IViolationDetailsPresenter>
    (), IViolationDetailsView {

    @Inject
    lateinit var statusColorUtil: IStatusColorUtil

    @Inject
    lateinit var adapter: ViolationPicturesAdapter

    private var _violationDetailsBinding: FragmentViolationDetailsBinding? = null
    private val violationDetailsBinding get() = _violationDetailsBinding!!

    override fun onDestroyView() {
        super.onDestroyView()
        _violationDetailsBinding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(
        R.layout.fragment_violation_details, container, false
    )

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val violation =
            arguments?.getSerializable(ViolationDetailsConstants.VIEW_DATA_KEY) as VoteViolationRemote
        violationDetailsBinding.listRecyclerView.layoutManager =
            LinearLayoutManager(this.context, RecyclerView.VERTICAL, false)
        adapter.item = violation
        adapter.onItemClickListener = View.OnClickListener {
            val position: Int = violationDetailsBinding.listRecyclerView.getChildLayoutPosition(it)
            val picture = violation.pictures[position - 1]
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(picture.url))
            intent.flags.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
        violationDetailsBinding.listRecyclerView.adapter = adapter
    }

    companion object {
        val TAG = ViolationDetailsFragment::class.java.simpleName

        @JvmStatic
        fun newInstance(violation: VoteViolationRemote): ViolationDetailsFragment {
            return ViolationDetailsFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ViolationDetailsConstants.VIEW_DATA_KEY, violation)
                }
            }
        }
    }
}