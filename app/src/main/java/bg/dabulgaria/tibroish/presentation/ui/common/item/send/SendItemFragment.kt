package bg.dabulgaria.tibroish.presentation.ui.common.item.send


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import bg.dabulgaria.tibroish.R
import bg.dabulgaria.tibroish.presentation.base.BasePresentableFragment
import bg.dabulgaria.tibroish.presentation.base.IBaseView
import bg.dabulgaria.tibroish.presentation.ui.common.DialogUtil
import bg.dabulgaria.tibroish.presentation.ui.common.IDialogUtil
import kotlinx.android.synthetic.main.fragment_send_item.*
import javax.inject.Inject

interface ISendItemView : IBaseView {

    fun onLoadingStateChange(isLoading: Boolean)

    fun setData(data: SendItemViewData)

    fun setSectionsData(data: SendItemViewData)

    fun hideSoftKeyboard()
}

open class SendItemFragment<SendPresenter : ISendItemPresenter> constructor()
    : BasePresentableFragment<ISendItemView, SendPresenter>(), ISendItemView {

    lateinit var adapter: SendItemAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_send_item, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter = SendItemAdapter(presenter)
        sendItemRecyclerView.adapter = adapter
        val layoutManager = GridLayoutManager(this.activity, 3)

        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {

            override fun getSpanSize(position: Int): Int {

                if (position < 0 || position >= adapter.list.size)
                    return 3

                return when (adapter.list[position].type) {
                    SendItemListItemType.Image -> 1
                    else -> 3
                }
            }
        }

        sendItemRecyclerView.layoutManager = layoutManager
    }

    override fun setData(data: SendItemViewData) {

        adapter.list.clear()
        adapter.list.addAll(data.items)
        adapter.notifyDataSetChanged()
    }

    override fun setSectionsData(data: SendItemViewData) {

        val index = adapter.list.indexOfFirst { it.type == SendItemListItemType.Section }
        if (index < 0)
            return

        val item = adapter.list.getOrNull(index) ?: return

        val sectionItem = item as SendItemListItemSection
        sectionItem.sectionsViewData = data.sectionsData

        adapter.notifyItemChanged(index)
    }

    override fun onLoadingStateChange(isLoading: Boolean) {

        val visibility = if (isLoading) View.VISIBLE else View.GONE
        sendItemProgressBar.visibility = visibility
        sendItemProcessingOverlay.visibility = visibility
    }

    override fun onError(errorMessage: String) {

        dialogUtil.showDismissableDialog(activity = requireActivity(), message = errorMessage){}
    }

    companion object {

        val TAG = SendItemFragment::class.java.simpleName
    }
}
