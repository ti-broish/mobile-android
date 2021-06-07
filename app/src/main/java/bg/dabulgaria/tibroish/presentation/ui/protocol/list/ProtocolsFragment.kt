package bg.dabulgaria.tibroish.presentation.ui.protocol.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bg.dabulgaria.tibroish.R
import bg.dabulgaria.tibroish.domain.providers.ILogger
import bg.dabulgaria.tibroish.presentation.base.BasePresentableFragment
import bg.dabulgaria.tibroish.presentation.base.IBaseView
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

    @Inject
    lateinit var logger: ILogger

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(
        R.layout.fragment_protocols_list, container, false
    )

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupRecyclerView()

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

        listSwipeRefreshLayout.setOnRefreshListener {
            refreshMyProtocols(initialLoading = false)
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
            val protocolRemote = adapter.list[position]
            logger.i(TAG, "on protocol clicked = $protocolRemote")
            // TODO: Launch the protocol details panel here
        }
    }

    private fun refreshMyProtocols(initialLoading: Boolean) {
        presenter.getMyProtocols(initialLoading) {
            adapter.updateList(it)
            listSwipeRefreshLayout.isRefreshing = false
            progressBar.visibility = View.GONE
            listSwipeRefreshLayout.visibility = View.VISIBLE
        }
    }

    companion object {
        val TAG = ProtocolsFragment::class.java.simpleName

        @JvmStatic
        fun newInstance() = ProtocolsFragment()
    }
}