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
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_photo_picker.*

interface IPhotoPickerView : IBaseView {

    fun onLoadingStateChange(viewState: ViewState)

    fun onDataLoaded(viewData: PhotoPickerViewData)

    fun onItemUpdated(photoItem:PhotoItem, index:Int)
}

class PhotoPickerFragment : BasePresentableFragment<IPhotoPickerView, IPhotoPickerPresenter>(), IPhotoPickerView {

    private var adapter : GridPickerAdapter?=null

    override fun onAttach(context: Context) {

        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater : LayoutInflater, container : ViewGroup?, savedInstanceState : Bundle?) : View? {
        return inflater.inflate(R.layout.fragment_photo_picker, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        listRecyclerView?.layoutManager = GridLayoutManager(activity, 3)
        adapter = GridPickerAdapter(presenter = presenter)
        listRecyclerView?.adapter = adapter

        infoText?.setVisibility(View.GONE)

        listSwipeRefreshLayout?.setColorSchemeResources(R.color.colorPrimary)
        listSwipeRefreshLayout?.setOnRefreshListener { presenter.loadData() }
    }

    //region IComicListView implementation
    override fun onLoadingStateChange(viewState: ViewState) {

        listSwipeRefreshLayout?.isRefreshing = (viewState == ViewState.Loading)
        photoPickerOverlayImageView?.visibility = if(viewState == ViewState.Loading) View.VISIBLE else View.GONE

        actionButton?.setOnClickListener { presenter.onDoneClick() }
        infoText?.visibility =  View.GONE
        actionButton?.setText( R.string.add )

        when(viewState){

            ViewState.Loaded -> {
                infoText?.setText(R.string.list_is_empty)
            }

            ViewState.NoPermission -> {

                infoText?.setText(R.string.app_has_no_image_permissions)
                infoText?.visibility =  View.VISIBLE
                actionButton?.setText( R.string.give_permission )
                actionButton?.setOnClickListener { presenter.onRequestPermissionClick() }
            }
        }
    }

    override fun onDataLoaded(data: PhotoPickerViewData) {

        adapter?.updateList( data.photoItems )
        infoText?.visibility = if (data.photoItems.isEmpty()) View.VISIBLE else View.GONE
    }

    override fun onItemUpdated(photoItem:PhotoItem, index:Int){

        adapter?.updateItem(photoItem, index)
    }

    override fun onError(errorMessage: String) {

        adapter?.notifyDataSetChanged()

        infoText?.setVisibility( if( adapter?.itemCount == 0 ) View.VISIBLE else View.INVISIBLE )

        Toast.makeText(activity, errorMessage, Toast.LENGTH_LONG).show()
    }
    //endregion IComicListView implementation

    companion object {

        val TAG = PhotoPickerFragment::class.java.simpleName

        fun newInstance(protocolId:Long): PhotoPickerFragment {
            return PhotoPickerFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(PhotoPickerConstants.VIEW_DATA_KEY, PhotoPickerViewData(protocolId))
                }
            }
        }
    }
}
