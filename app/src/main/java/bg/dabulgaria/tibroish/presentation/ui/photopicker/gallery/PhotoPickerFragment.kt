package bg.dabulgaria.tibroish.presentation.ui.photopicker.gallery//package bg.dabulgaria.tibroish.presentation.ui.protocol.list


import android.content.Context
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
import bg.dabulgaria.tibroish.presentation.ui.common.preview.images.CloseListener
import bg.dabulgaria.tibroish.presentation.ui.common.preview.images.PreviewImage
import bg.dabulgaria.tibroish.presentation.ui.common.preview.images.PreviewImageCheckListener
import dagger.android.support.AndroidSupportInjection
import bg.dabulgaria.tibroish.databinding.FragmentPhotoPickerBinding

interface IPhotoPickerView : IBaseView {

    fun onLoadingStateChange(viewState: ViewState)

    fun onDataLoaded(viewData: PhotoPickerViewData)

    fun onItemUpdated(photoItem: PhotoItem, index: Int)
}

class PhotoPickerFragment : BasePresentableFragment<IPhotoPickerView, IPhotoPickerPresenter>(),
        IPhotoPickerView, BackHandlerObject {

    private var adapter: GridPickerAdapter? = null
    private var backHandlerInterface: BackHandlerInterface? = null

    private var _photoPickerBinding: FragmentPhotoPickerBinding? = null
    private val photoPickerBinding get() = _photoPickerBinding!!

    override fun onDestroyView() {
        super.onDestroyView()
        _photoPickerBinding = null
    }

    override fun onAttach(context: Context) {

        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (activity is BackHandlerInterface)
            backHandlerInterface = activity as BackHandlerInterface?
        else
            throw ClassCastException("Hosting activity must implement BackHandlerInterface")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        return inflater.inflate(R.layout.fragment_photo_picker, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _photoPickerBinding = FragmentPhotoPickerBinding.bind(view)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        photoPickerBinding.listRecyclerView.layoutManager = GridLayoutManager(activity, 3)
        adapter = GridPickerAdapter(presenter = presenter)
        photoPickerBinding.listRecyclerView.adapter = adapter

        photoPickerBinding.infoText.setVisibility(View.GONE)

        photoPickerBinding.listSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary)
        photoPickerBinding.listSwipeRefreshLayout.setOnRefreshListener { presenter.loadData() }
    }

    //region IComicListView implementation
    override fun onLoadingStateChange(viewState: ViewState) {

        photoPickerBinding.listSwipeRefreshLayout.isRefreshing = (viewState == ViewState.Loading)
        photoPickerBinding.photoPickerOverlayImageView.visibility = if (viewState == ViewState.Loading) View.VISIBLE else View.GONE

        photoPickerBinding.actionButton.setOnClickListener { presenter.onDoneClick() }
        photoPickerBinding.infoText.visibility = View.GONE
        photoPickerBinding.actionButton.setText(R.string.add)

        when (viewState) {

            ViewState.Loaded -> {
                photoPickerBinding.infoText.setText(R.string.list_is_empty)
            }

            ViewState.NoPermission -> {

                photoPickerBinding.infoText.setText(R.string.app_has_no_image_permissions)
                photoPickerBinding.infoText.visibility = View.VISIBLE
                photoPickerBinding.actionButton.setText(R.string.give_permission)
                photoPickerBinding.actionButton.setOnClickListener { presenter.onRequestPermissionClick() }
            }

            else -> {
                // implement if needed
            }
        }
    }

    override fun onDataLoaded(viewData: PhotoPickerViewData) {

        adapter?.updateList(viewData.photoItems)
        photoPickerBinding.infoText.visibility = if (viewData.photoItems.isEmpty()) View.VISIBLE else View.GONE

        photoPickerBinding.previewImagesView.visibility = if(viewData.previewOpen) View.VISIBLE else View.GONE

        if(viewData.lastPhotoIndex > -1 )
            photoPickerBinding.listRecyclerView.scrollToPosition(viewData.lastPhotoIndex)

        if(viewData.previewOpen){

            photoPickerBinding.previewImagesView.bindView(imagesList = viewData.photoItems,
                    initialPosition = viewData.lastPhotoIndex,
                    closeListener = object:CloseListener{
                        override fun onClose(lastPosition: Int) {
                            presenter.onPreviewCloseClick(lastPosition)
                        } },
                    deleteListener = null,
                    checkListener = object:PreviewImageCheckListener{
                        override fun onCheckClick(position: Int, image: PreviewImage) {
                            presenter.onImageClick(position)
                        }
                    }
            )
        }

        viewData.lastPhotoIndex = -1
    }

    override fun onItemUpdated(photoItem: PhotoItem, index: Int) {

        adapter?.updateItem(photoItem, index)

        photoPickerBinding.previewImagesView.updateItem(photoItem, index)
    }

    override fun onError(errorMessage: String) {

        adapter?.notifyDataSetChanged()

        photoPickerBinding.infoText.visibility = if (adapter?.itemCount == 0) View.VISIBLE else View.INVISIBLE

        Toast.makeText(activity, errorMessage, Toast.LENGTH_LONG).show()
    }

    override fun onStart() {
        super.onStart()
        backHandlerInterface?.setSelectedHandler(this, TAG)
    }

    override fun onStop() {

        backHandlerInterface?.setSelectedHandler(null, null)
        super.onStop()
    }

    override fun handleBackPressed(): Boolean {

        return presenter.onHandleBack(photoPickerBinding.previewImagesView.getPosition())
    }
    //endregion IComicListView implementation

    companion object {

        val TAG = PhotoPickerFragment::class.java.simpleName

        fun newInstance(selectedImages: List<PhotoId>): PhotoPickerFragment {
            return PhotoPickerFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(PhotoPickerConstants.VIEW_DATA_KEY, PhotoPickerViewData(selectedImages))
                }
            }
        }
    }
}
