package bg.dabulgaria.tibroish.presentation.ui.protocol.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bg.dabulgaria.tibroish.R
import bg.dabulgaria.tibroish.presentation.base.BasePresentableFragment
import bg.dabulgaria.tibroish.presentation.base.IBaseView
import bg.dabulgaria.tibroish.presentation.ui.common.IDialogUtil
import bg.dabulgaria.tibroish.presentation.ui.protocol.list.ProtocolsPresenter.State
import kotlinx.android.synthetic.main.fragment_protocols_list.*
import javax.inject.Inject

interface IProtocolsView : IBaseView {

}

class ProtocolsFragment : BasePresentableFragment<IProtocolsView,
        IProtocolsPresenter>
    (), IProtocolsView {

    @Inject
    lateinit var adapter: ProtocolsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(
        R.layout.fragment_protocols_list, container, false
    )

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupRecyclerView()

        updateState()

        listSwipeRefreshLayout.setOnRefreshListener {
            refreshMyProtocols(initialLoading = false)
        }
    }

    private fun updateState() {
        when (presenter.getState()) {
            State.STATE_LOADING_INITIAL -> refreshMyProtocols(initialLoading = true)
            State.STATE_LOADING_SUBSEQUENT -> {
                val cachedProtocols = presenter.getCachedProtocols()
                if (cachedProtocols != null) {
                    adapter.updateList(cachedProtocols)
                }
                showList()
                refreshMyProtocols(initialLoading = false)
            }
            State.STATE_LOADED_SUCCESS -> showList()
            State.STATE_LOADED_FAILURE -> showList()
        }
    }

    private fun showList() {
        listSwipeRefreshLayout.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
    }

    private fun setupRecyclerView() {
        listRecyclerView.layoutManager =
            LinearLayoutManager(this.context, RecyclerView.VERTICAL, false)
        listRecyclerView.adapter = adapter
        adapter.onItemClickListener = View.OnClickListener {
            val position: Int = listRecyclerView.getChildLayoutPosition(it)
            presenter.showProtocolAt(position)
        }
    }

    private fun refreshMyProtocols(initialLoading: Boolean) {
        presenter.getMyProtocols(initialLoading) {
            adapter.updateList(it)
            listSwipeRefreshLayout.isRefreshing = false
            updateState()
        }
    }

    override fun onError(errorMessage: String) {
        dialogUtil.showDismissableDialog(activity = requireActivity(), message = errorMessage){}
    }

    companion object {
        val TAG = ProtocolsFragment::class.java.simpleName

        @JvmStatic
        fun newInstance() = ProtocolsFragment()
    }
}