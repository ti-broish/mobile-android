package bg.dabulgaria.tibroish.presentation.ui.protocol.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bg.dabulgaria.tibroish.R
import bg.dabulgaria.tibroish.domain.protocol.ProtocolRemote
import bg.dabulgaria.tibroish.presentation.base.BasePresentableFragment
import bg.dabulgaria.tibroish.presentation.base.IBaseView
import bg.dabulgaria.tibroish.presentation.ui.common.IStatusColorUtil
import kotlinx.android.synthetic.main.fragment_protocols_details.*
import javax.inject.Inject

interface IProtocolDetailsView : IBaseView {

}

class ProtocolDetailsFragment : BasePresentableFragment<IProtocolDetailsView,
        IProtocolDetailsPresenter>
    (), IProtocolDetailsView {

    @Inject
    lateinit var statusColorUtil: IStatusColorUtil

    @Inject
    lateinit var adapter: ProtocolPicturesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(
        R.layout.fragment_protocols_details, container, false
    )

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val protocol =
            arguments?.getSerializable(ProtocolDetailsConstants.VIEW_DATA_KEY) as ProtocolRemote
        listRecyclerView.layoutManager =
            LinearLayoutManager(this.context, RecyclerView.VERTICAL, false)
        adapter.protocol = protocol
        listRecyclerView.adapter = adapter
    }

    companion object {
        val TAG = ProtocolDetailsFragment::class.java.simpleName

        @JvmStatic
        fun newInstance(protocol: ProtocolRemote): ProtocolDetailsFragment {
            return ProtocolDetailsFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ProtocolDetailsConstants.VIEW_DATA_KEY, protocol)
                }
            }
        }
    }
}