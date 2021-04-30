package bg.dabulgaria.tibroish.presentation.ui.protocol.add


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.CallSuper
import bg.dabulgaria.tibroish.R
import bg.dabulgaria.tibroish.presentation.base.BasePresentableFragment
import bg.dabulgaria.tibroish.presentation.base.IBaseView
import kotlinx.android.synthetic.main.fragment_add_protocol.*
import kotlinx.android.synthetic.main.fragment_comic_list.*
import javax.inject.Inject

interface IAddProtocolView : IBaseView {

    fun onLoadingStateChange( isLoading : Boolean )
}

class AddProtocolFragment @Inject constructor()
    : BasePresentableFragment<IAddProtocolView, IAddProtocolPresenter>(), IAddProtocolView {

    override fun onCreateView(inflater : LayoutInflater, container : ViewGroup?,
                              savedInstanceState : Bundle?) : View? {
        return inflater.inflate(R.layout.fragment_add_protocol, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        addProtocolGalleryBtn.setOnClickListener { presenter.onAddFromGalleryClick() }
        addProtocolCameraBtn.setOnClickListener { presenter.onAddFromCameraClick() }
    }

    override fun onLoadingStateChange(isLoading: Boolean) {

        listSwipeRefreshLayout?.isRefreshing = isLoading
    }

    override fun onError(errorMessage: String) {

        Toast.makeText(activity, errorMessage, Toast.LENGTH_LONG).show()
    }

    private fun setData(){

//        titleTextView?.text = mAddProtocolViewData?.title
//        descriptionTextView?.text = mAddProtocolViewData?.description
//
//        if( thumbnailImageView != null && mAddProtocolViewData != null ){
//
//            Glide.with(this)
//                    .load(mAddProtocolViewData?.thumbUlr)
//                    .transition(DrawableTransitionOptions.withCrossFade())
//                    .into(thumbnailImageView!!)
//        }
    }

    companion object {

        val TAG = AddProtocolFragment::class.java.simpleName

        fun newInstance(addProtocolViewData: AddProtocolViewData): AddProtocolFragment {

            return AddProtocolFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(AddProtocolConstants.VIEW_DATA_KEY, addProtocolViewData)
                }
            }
        }
    }
}
