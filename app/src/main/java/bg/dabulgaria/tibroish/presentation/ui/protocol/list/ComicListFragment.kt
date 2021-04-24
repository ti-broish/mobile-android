//package bg.dabulgaria.tibroish.presentation.ui.protocol.list
//
//
//import android.content.Context
//import android.os.Bundle
//import androidx.fragment.app.Fragment
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.TextView
//import android.widget.Toast
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
//
//import bg.dabulgaria.tibroish.R
//import bg.dabulgaria.tibroish.domain.Locations.LocationsS
//import bg.dabulgaria.tibroish.presentation.base.IBaseView
//import dagger.android.support.AndroidSupportInjection
//import javax.inject.Inject
//
//interface IComicListView : IBaseView {
//
//    fun onLoadingStateChange(isLoading : Boolean );
//
//    fun onComicsLoaded(locations : LocationsS )
//
//    fun onError( errorMessage: String)
//}
//
//class ComicListFragment : Fragment(), IComicListView {
//
//    companion object {
//
//        val TAG = ComicListFragment::class.java.simpleName
//
//        fun newInstance(): ComicListFragment {
//            return ComicListFragment()
//        }
//    }
//
//    private var listRecyclerView: RecyclerView? = null
//    private var listSwipeRefreshLayout: SwipeRefreshLayout? = null
//    private var emptyListText: TextView? = null
//    private var adapter: ComicsAdapter? = null
//
//    @Inject
//    protected lateinit var listPresenter: IComicListPresenter
//
//    override fun onAttach(context: Context) {
//
//        AndroidSupportInjection.inject(this)
//        super.onAttach(context)
//    }
//
//    override fun onCreateView(inflater : LayoutInflater, container : ViewGroup?,
//                              savedInstanceState : Bundle?) : View? {
//
//        val view = inflater.inflate(R.layout.fragment_comic_list, container, false)
//
//        listRecyclerView = view.findViewById(R.id.listRecyclerView)
//        listSwipeRefreshLayout = view.findViewById(R.id.listSwipeRefreshLayout)
//        emptyListText = view.findViewById(R.id.emptyListText)
//
//        listRecyclerView?.setLayoutManager(LinearLayoutManager(activity, RecyclerView.VERTICAL, false))
//        adapter = ComicsAdapter(listPresenter)
//        listRecyclerView?.setAdapter( adapter )
//
//        emptyListText?.setVisibility(View.GONE)
//
//        listSwipeRefreshLayout?.setColorSchemeResources(android.R.color.holo_orange_dark)
//        listSwipeRefreshLayout?.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
//
//            listPresenter.loadComics(true)
//        })
//
//        return view
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        listPresenter.onViewCreated( this )
//    }
//
//    override fun onResume() {
//
//        super.onResume()
//        listPresenter.loadComics(false )
//    }
//
//    override fun onPause() {
//
//        super.onPause()
//    }
//
//    //region IComicListView implementation
//    override fun onLoadingStateChange(isLoading: Boolean) {
//
//        listSwipeRefreshLayout?.setRefreshing(isLoading)
//    }
//
//    override fun onComicsLoaded(locationsS: LocationsS) {
//
//        adapter?.updateList(locationsS )
//
//        val listIsEmpty = locationsS.regions.isNullOrEmpty()
//        emptyListText?.setVisibility(if (listIsEmpty) View.VISIBLE else View.GONE)
//    }
//
//    override fun onError(errorMessage: String) {
//
//        adapter?.notifyDataSetChanged()
//
//        emptyListText?.setVisibility( if( adapter?.itemCount == 0 ) View.VISIBLE else View.INVISIBLE )
//
//        Toast.makeText(activity, errorMessage, Toast.LENGTH_LONG).show()
//    }
//    //endregion IComicListView implementation
//}
