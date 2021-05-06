package bg.dabulgaria.tibroish.presentation.ui.protocol.add


import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import bg.dabulgaria.tibroish.R
import bg.dabulgaria.tibroish.presentation.base.BasePresentableFragment
import bg.dabulgaria.tibroish.presentation.base.IBaseView
import kotlinx.android.synthetic.main.fragment_add_protocol.*
import kotlinx.android.synthetic.main.fragment_comic_list.*
import javax.inject.Inject

interface IAddProtocolView : IBaseView {

    fun onLoadingStateChange( isLoading : Boolean )

    fun setData( data:AddProtocolViewData )
}

class AddProtocolFragment @Inject constructor()
    : BasePresentableFragment<IAddProtocolView, IAddProtocolPresenter>(), IAddProtocolView {

    lateinit var adapter:AddProtocolAdapter

    override fun onCreateView(inflater : LayoutInflater, container : ViewGroup?,
                              savedInstanceState : Bundle?) : View? {
        return inflater.inflate(R.layout.fragment_add_protocol, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter = AddProtocolAdapter(presenter)
        addProtocolRecyclerView.adapter = adapter
        val layoutManager = GridLayoutManager( this.activity, 3 )

        layoutManager.spanSizeLookup = object:GridLayoutManager.SpanSizeLookup(){

            override fun getSpanSize(position: Int): Int {

                if( position < 0 || position >= adapter.list.size)
                    return 3

                return when(adapter.list[position].type){
                    AddProtocolListItemType.Image -> 1
                    else -> 3
                }
            }
        }
        addProtocolRecyclerView.layoutManager = layoutManager
    }

    override fun setData( data:AddProtocolViewData ){

        adapter.list.clear()
        adapter.list.addAll(data.items)
        adapter.notifyDataSetChanged()


    }

    override fun onLoadingStateChange(isLoading: Boolean) {

        listSwipeRefreshLayout?.isRefreshing = isLoading
    }

    override fun onError(errorMessage: String) {

        Toast.makeText(activity, errorMessage, Toast.LENGTH_LONG).show()
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
