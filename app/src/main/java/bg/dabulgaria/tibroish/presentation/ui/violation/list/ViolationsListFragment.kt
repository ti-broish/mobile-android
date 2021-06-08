package bg.dabulgaria.tibroish.presentation.ui.violation.list

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import bg.dabulgaria.tibroish.R
import bg.dabulgaria.tibroish.presentation.base.BasePresentableFragment
import bg.dabulgaria.tibroish.presentation.base.IBaseView
import bg.dabulgaria.tibroish.presentation.ui.common.IDialogUtil
import bg.dabulgaria.tibroish.presentation.ui.protocol.list.IViolationsListPresenter
import kotlinx.android.synthetic.main.fragment_violations_list.*
import javax.inject.Inject


interface IViolationsListView : IBaseView {

    fun onLoadingStateChange(isLoading: Boolean)

    fun setData(data: ViolationListViewData)
}

class ViolationsListFragment
    : BasePresentableFragment<IViolationsListView, IViolationsListPresenter>(), IViolationsListView {

    @Inject
    protected lateinit var dialogUtil: IDialogUtil

    private lateinit var adapter: ViolationsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_violations_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter = ViolationsAdapter(mutableListOf(), presenter)
        violationsListRecyclerView.adapter = adapter
        violationsListRecyclerView.layoutManager = LinearLayoutManager(context)

        violationsListSwipeRefreshLayout?.setOnRefreshListener {

            presenter.loadData()
            violationsListSwipeRefreshLayout?.isRefreshing = false
        }
    }

    override fun onLoadingStateChange(isLoading: Boolean) {

        val visibility = if (isLoading) View.VISIBLE else View.GONE
        violationsListProcessingOverlay.visibility = visibility
        violationsListProgressBar.visibility = visibility
    }

    override fun setData(data: ViolationListViewData) {

        adapter.items.clear()
        adapter.items.addAll(data.items)
        adapter.notifyDataSetChanged()
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