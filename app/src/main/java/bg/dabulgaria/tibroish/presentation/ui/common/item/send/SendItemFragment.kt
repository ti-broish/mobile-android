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
import bg.dabulgaria.tibroish.presentation.navigation.BackHandlerInterface
import bg.dabulgaria.tibroish.presentation.navigation.BackHandlerObject
import bg.dabulgaria.tibroish.presentation.ui.common.DialogUtil
import bg.dabulgaria.tibroish.presentation.ui.common.IDialogUtil
import bg.dabulgaria.tibroish.presentation.ui.common.preview.images.CloseListener
import bg.dabulgaria.tibroish.presentation.ui.common.preview.images.PreviewImage
import bg.dabulgaria.tibroish.presentation.ui.common.preview.images.PreviewImageCheckListener
import bg.dabulgaria.tibroish.presentation.ui.common.preview.images.PreviewImageDeleteListener
import bg.dabulgaria.tibroish.presentation.ui.photopicker.gallery.PhotoPickerFragment
import kotlinx.android.synthetic.main.fragment_photo_picker.*
import kotlinx.android.synthetic.main.fragment_send_item.*
import kotlinx.android.synthetic.main.fragment_send_item.previewImagesView
import javax.inject.Inject

interface ISendItemView : IBaseView {

    fun onLoadingStateChange(isLoading: Boolean)

    fun setData(data: SendItemViewData)

    fun setSectionsData(data: SendItemViewData)

    fun hideSoftKeyboard()
}

open class SendItemFragment<SendPresenter : ISendItemPresenter> constructor()
    : BasePresentableFragment<ISendItemView, SendPresenter>(), ISendItemView, BackHandlerObject {

    private var backHandlerInterface: BackHandlerInterface? = null
    lateinit var adapter: SendItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (activity is BackHandlerInterface)
            backHandlerInterface = activity as BackHandlerInterface?
        else
            throw ClassCastException("Hosting activity must implement BackHandlerInterface")
    }

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

    override fun onStart() {
        super.onStart()
        backHandlerInterface?.setSelectedHandler(this, PhotoPickerFragment.TAG)
    }

    override fun onStop() {

        backHandlerInterface?.setSelectedHandler(null, null)
        super.onStop()
    }

    override fun handleBackPressed(): Boolean {

        return presenter.onHandleBack(previewImagesView?.getPosition())
    }

    override fun setData(data: SendItemViewData) {

        adapter.list.clear()
        adapter.list.addAll(data.items)
        adapter.notifyDataSetChanged()

        previewImagesView?.visibility = if(data.imagePreviewOpen) View.VISIBLE else View.GONE

        if(data.imagePreviewOpen){

            previewImagesView?.bindView(imagesList = data.entityItem?.images.orEmpty(),
                    initialPosition = data.previewImageIndex,
                    closeListener = object: CloseListener {
                        override fun onClose(lastPosition: Int) {
                            presenter.onPreviewCloseClick(lastPosition)
                        } },
                    deleteListener = object: PreviewImageDeleteListener{
                        override fun onDelete(position: Int, image: PreviewImage) {
                            presenter.onPreviewDelete(position, image)
                        }
                    },
                    checkListener = null
            )
        }
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
