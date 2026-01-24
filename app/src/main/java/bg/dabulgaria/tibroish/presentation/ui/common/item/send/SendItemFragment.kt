package bg.dabulgaria.tibroish.presentation.ui.common.item.send


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import bg.dabulgaria.tibroish.R
import bg.dabulgaria.tibroish.presentation.base.BasePresentableFragment
import bg.dabulgaria.tibroish.presentation.base.IBaseView
import bg.dabulgaria.tibroish.presentation.navigation.BackHandlerInterface
import bg.dabulgaria.tibroish.presentation.navigation.BackHandlerObject
import bg.dabulgaria.tibroish.presentation.ui.common.preview.images.CloseListener
import bg.dabulgaria.tibroish.presentation.ui.common.preview.images.PreviewImage
import bg.dabulgaria.tibroish.presentation.ui.common.preview.images.PreviewImageDeleteListener
import bg.dabulgaria.tibroish.presentation.ui.photopicker.gallery.PhotoPickerFragment
import bg.dabulgaria.tibroish.databinding.FragmentPhotoPickerBinding
import bg.dabulgaria.tibroish.databinding.FragmentSendItemBinding

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

    private var _sendItemBinding: FragmentSendItemBinding? = null
    private val sendItemBinding get() = _sendItemBinding!!

    private var _photoPickerBinding: FragmentPhotoPickerBinding? = null
    private val photoPickerBinding get() = _photoPickerBinding!!

    override fun onDestroyView() {
        super.onDestroyView()
        _sendItemBinding = null
        _photoPickerBinding = null
    }

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _sendItemBinding = FragmentSendItemBinding.bind(view)
        _photoPickerBinding = FragmentPhotoPickerBinding.bind(view)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter = SendItemAdapter(presenter)
        sendItemBinding.sendItemRecyclerView.adapter = adapter
        val layoutManager = GridLayoutManager(this.activity, 3)

        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {

            override fun getSpanSize(position: Int): Int {

                if (position < 0 || position >= adapter.listItems.size)
                    return 3

                return when (adapter.listItems[position].type) {
                    SendItemListItemType.Image -> 1
                    else -> 3
                }
            }
        }

        sendItemBinding.sendItemRecyclerView.layoutManager = layoutManager
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

        return presenter.onHandleBack(photoPickerBinding.previewImagesView.getPosition())
    }

    override fun setData(data: SendItemViewData) {

        adapter.listItems.clear()
        adapter.listItems.addAll(data.items)
        adapter.notifyDataSetChanged()

        photoPickerBinding.previewImagesView.visibility = if(data.imagePreviewOpen) View.VISIBLE else View.GONE

        if(data.imagePreviewOpen){

            photoPickerBinding.previewImagesView.bindView(imagesList = data.entityItem?.images.orEmpty(),
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

        val index = adapter.listItems.indexOfFirst { it.type == SendItemListItemType.Section }
        if (index < 0)
            return

        val item = adapter.listItems.getOrNull(index) ?: return

        val sectionItem = item as SendItemListItemSection
        sectionItem.sectionsViewData = data.sectionsData

        adapter.notifyItemChanged(index)
    }

    override fun onLoadingStateChange(isLoading: Boolean) {

        val visibility = if (isLoading) View.VISIBLE else View.GONE
        sendItemBinding.sendItemProgressBar.visibility = visibility
        sendItemBinding.sendItemProcessingOverlay.visibility = visibility
    }

    override fun onError(errorMessage: String) {

        dialogUtil.showDismissableDialog(activity = requireActivity(), message = errorMessage){}
    }

    companion object {

        val TAG = SendItemFragment::class.java.simpleName
    }
}
