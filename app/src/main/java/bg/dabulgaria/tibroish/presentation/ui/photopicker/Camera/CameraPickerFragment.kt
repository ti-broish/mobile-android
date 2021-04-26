package bg.dabulgaria.tibroish.presentation.ui.photopicker.Camera//package bg.dabulgaria.tibroish.presentation.ui.protocol.list


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

import bg.dabulgaria.tibroish.R
import bg.dabulgaria.tibroish.domain.Locations.LocationsS
import bg.dabulgaria.tibroish.presentation.base.BasePresentableFragment
import bg.dabulgaria.tibroish.presentation.base.IBaseView
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_comic_list.*

interface ICameraPickerView : IBaseView {

    fun onLoadingStateChange(isLoading : Boolean );

    fun onComicsLoaded(locations : LocationsS )

    fun onError( errorMessage: String)
}

class CameraPickerFragment : BasePresentableFragment<ICameraPickerView,ICameraPickerPresenter>(), ICameraPickerView {

    private var mAdapter :CameraPickerAdapter ?=null

    override fun onAttach(context: Context) {

        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater : LayoutInflater, container : ViewGroup?, savedInstanceState : Bundle?) : View? {
        return inflater.inflate(R.layout.fragment_photo_picker, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        listRecyclerView?.setLayoutManager(LinearLayoutManager(activity, RecyclerView.VERTICAL, false))
        mAdapter = CameraPickerAdapter(presenter)
        listRecyclerView?.setAdapter( mAdapter )

        emptyListText?.setVisibility(View.GONE)

        listSwipeRefreshLayout?.setColorSchemeResources(android.R.color.holo_orange_dark)
        listSwipeRefreshLayout?.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {

            presenter.loadComics(true)
        })
    }

    //region IComicListView implementation
    override fun onLoadingStateChange(isLoading: Boolean) {

        listSwipeRefreshLayout?.setRefreshing(isLoading)
    }

    override fun onComicsLoaded(locationsS: LocationsS) {

        mAdapter?.updateList(locationsS )

        val listIsEmpty = locationsS.regions.isNullOrEmpty()
        emptyListText?.setVisibility(if (listIsEmpty) View.VISIBLE else View.GONE)
    }

    override fun onError(errorMessage: String) {

        mAdapter?.notifyDataSetChanged()

        emptyListText?.setVisibility( if( mAdapter?.itemCount == 0 ) View.VISIBLE else View.INVISIBLE )

        Toast.makeText(activity, errorMessage, Toast.LENGTH_LONG).show()
    }
    //endregion IComicListView implementation

    companion object {

        val TAG = CameraPickerFragment::class.java.simpleName

        fun newInstance(protocolId:Long): CameraPickerFragment {
            return CameraPickerFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(CameraPickerConstants.VIEW_DATA_KEY, CameraPickerViewData(protocolId))
                }
            }
        }
    }
}
