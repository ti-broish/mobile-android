//package bg.dabulgaria.tibroish.presentation.navigation
//
//import android.content.Context
//import android.util.AttributeSet
//import android.view.LayoutInflater
//import android.widget.TextView
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import bg.dabulgaria.tibroish.R
//import com.google.android.material.navigation.NavigationView
//
//enum class NavItem{
//    Home,
//    Profile,
//    SendProtocol
//}
//
//interface OnClickMenu{
//
//    fun onItemClicked(navItem: NavItem)
//}
//
//class SimpleLeftMenuView : NavigationView {
//
//    private var mInflater: LayoutInflater
//    private var mContext: Context
//    //private var mItemsList: ListView? = null
//    //private var mItemsAdapter: MenuItemsAdapter? = null
//    private var listener: OnClickMenu? = null
//    private var header: TextView? = null
//    private var navItemsRecyclerView: RecyclerView? = null
//
//
//    //region Constructors
//    constructor(context: Context) : super(context) {
//        mContext = context
//        mInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//        initLayout()
//        //setData()
//    }
//
//    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
//        mContext = context
//        mInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//        initLayout()
//        //setData()
//    }
//
//    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
//        mContext = context
//        mInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//        initLayout()
//        //setData()
//    }
//
//    //endregion
//    private fun initLayout() {
//
//        mInflater.inflate(R.layout.layout_left_menu, this)
//        header = findViewById(R.id.navMenuText)
//        navItemsRecyclerView = findViewById(R.id.navItemsRecyclerView)
//
//        navItemsRecyclerView?.layoutManager = LinearLayoutManager( this.context, RecyclerView.VERTICAL, false )
//
//        header?.setOnClickListener { listener?.onItemClicked(NavItem.Home) }
//
//        profileText?.setOnClickListener { listener?.onItemClicked(NavItem.Profile) }
//        sendProtocolText?.setOnClickListener { listener?.onItemClicked(NavItem.SendProtocol) }
//    }
//
////    fun setSelectedSection(idSection: String?) {
////        mItemsAdapter.setLastSelectedSection(idSection)
////    }
//
//    fun setListener(mListener: OnClickMenu?) {
//        this.listener = mListener
//    }
////
////    private fun setData() {
////        val sections: MutableList<String> = ArrayList()
////        sections.add(mContext.getString(R.string.home_id))
////        sections.add(mContext.getString(R.string.login_id))
////        sections.add(mContext.getString(R.string.settings_id))
////        //.........
////        //sections.add(mContext.getString(R.string.exit_id));
////        mItemsAdapter = MenuItemsAdapter(mContext, sections, object : OnClickMenu() {
////            fun onClick(id: String?) {
////                mItemsAdapter.setLastSelectedSection(id)
////                if (mListener != null) mListener.onClick(id)
////            }
////        })
////        mItemsList.setAdapter(mItemsAdapter)
////        mItemsList.setSelection(0)
////        mItemsList.setItemChecked(0, true)
////    }
//}