package bg.dabulgaria.tibroish.presentation.ui.violation.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bg.dabulgaria.tibroish.R
import bg.dabulgaria.tibroish.presentation.base.BasePresentableFragment
import bg.dabulgaria.tibroish.presentation.base.IBaseView
import bg.dabulgaria.tibroish.presentation.ui.violation.list.ViolationsListPresenter.State
import bg.dabulgaria.tibroish.databinding.FragmentViolationsListBinding
import javax.inject.Inject

interface IViolationsListView : IBaseView {

}

class ViolationsListFragment
    : BasePresentableFragment<IViolationsListView, IViolationsListPresenter>(), IViolationsListView {

    @Inject
    lateinit var adapter: ViolationsAdapter

    private var _violationsListBinding: FragmentViolationsListBinding? = null
    private val violationsListBinding get() = _violationsListBinding!!

    override fun onDestroyView() {
        super.onDestroyView()
        _violationsListBinding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(
        R.layout.fragment_violations_list, container, false
    )

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupRecyclerView()

        updateState()

        violationsListBinding.listSwipeRefreshLayout.setOnRefreshListener {
            refreshMyViolations(initialLoading = false)
        }
    }

    private fun updateState() {
        when (presenter.getState()) {
            State.STATE_LOADING_INITIAL -> refreshMyViolations(initialLoading = true)
            State.STATE_LOADING_SUBSEQUENT -> {
                val cachedProtocols = presenter.getCachedViolations()
                if (cachedProtocols != null) {
                    adapter.updateList(cachedProtocols)
                }
                showList()
                refreshMyViolations(initialLoading = false)
            }
            State.STATE_LOADED_SUCCESS -> showList()
            State.STATE_LOADED_FAILURE -> showList()
        }
    }

    private fun showList() {
        violationsListBinding.listSwipeRefreshLayout.visibility = View.VISIBLE
        violationsListBinding.progressBar.visibility = View.GONE
    }

    private fun setupRecyclerView() {
        violationsListBinding.listRecyclerView.layoutManager =
            LinearLayoutManager(this.context, RecyclerView.VERTICAL, false)
        violationsListBinding.listRecyclerView.adapter = adapter
        adapter.onItemClickListener = View.OnClickListener {
            val position: Int = violationsListBinding.listRecyclerView.getChildLayoutPosition(it)
            presenter.showViolationAt(position)
        }
    }

    private fun refreshMyViolations(initialLoading: Boolean) {
        presenter.getMyViolations(initialLoading) {
            adapter.updateList(it)
            violationsListBinding.listSwipeRefreshLayout.isRefreshing = false
            updateState()
        }
    }

    override fun onError(errorMessage: String) {
        dialogUtil.showDismissableDialog(activity = requireActivity(), message = errorMessage){}
    }

    companion object {

        val TAG = ViolationsListFragment::class.java.simpleName

        @JvmStatic
        fun newInstance() = ViolationsListFragment()

    }
}