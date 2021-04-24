package bg.dabulgaria.tibroish.presentation.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import bg.dabulgaria.tibroish.R
import bg.dabulgaria.tibroish.presentation.base.BaseFragment
import bg.dabulgaria.tibroish.presentation.main.IMainNavigator
import bg.dabulgaria.tibroish.presentation.navigation.NavItemAction
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.Inject

class HomeFragment : BaseFragment() {


    @Inject
    protected lateinit var mainNavigator: IMainNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onStart() {
        super.onStart()

        sendProtocol.setOnClickListener {  mainNavigator.onNavigateToItem( NavItemAction.SendProtocol) }
        sendSignal.setOnClickListener {  mainNavigator.onNavigateToItem( NavItemAction.SendSignal) }
        rightsAndObligations.setOnClickListener {  mainNavigator.onNavigateToItem( NavItemAction.RightsAndObligations) }
        tiBorishLive.setOnClickListener {  mainNavigator.onNavigateToItem( NavItemAction.YouCountLive ) }
    }

    companion object {

        val TAG = HomeFragment::class.java.simpleName

        @JvmStatic
        fun newInstance() =
                HomeFragment().apply {
                }
    }
}