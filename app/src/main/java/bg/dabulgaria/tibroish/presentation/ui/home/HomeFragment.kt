package bg.dabulgaria.tibroish.presentation.ui.home

import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import bg.dabulgaria.tibroish.R
import bg.dabulgaria.tibroish.domain.providers.ILogger
import bg.dabulgaria.tibroish.infrastructure.schedulers.ISchedulersProvider
import bg.dabulgaria.tibroish.presentation.base.BaseFragment
import bg.dabulgaria.tibroish.presentation.main.IMainRouter
import bg.dabulgaria.tibroish.presentation.navigation.NavItemAction
import bg.dabulgaria.tibroish.presentation.push.IPushTokenSender
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.Inject

class HomeFragment : BaseFragment() {

    @Inject
    protected lateinit var mainRouter: IMainRouter
    @Inject
    lateinit var logger: ILogger
    @Inject
    lateinit var schedulersProvider: ISchedulersProvider
    @Inject
    lateinit var pushTokenSender: IPushTokenSender

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

        pushTokenSender.sendPushToken()

        sendProtocol?.setOnClickListener { mainRouter.onNavigateToItem(NavItemAction.SendProtocol) }
        sendSignal?.setOnClickListener { mainRouter.onNavigateToItem(NavItemAction.SendSignal) }
        rightsAndObligations?.setOnClickListener { mainRouter.onNavigateToItem(NavItemAction.RightsAndObligations) }
        homeCheckIn?.setOnClickListener { mainRouter.onNavigateToItem(NavItemAction.CheckIn) }

        initLiveButton()
    }

    private fun initLiveButton(){

        val color = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            resources.getColor(R.color.textRed, null)
        } else {
            resources.getColor(R.color.textRed)
        }

        val firstString = getString(R.string.ti_broish)
        val secondString = getString(R.string.live)

        val spannableString = SpannableString( "${firstString} ${secondString}" )
        spannableString.setSpan(ForegroundColorSpan(color),
                firstString.length + 1,
                firstString.length + secondString.length + 1,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        tiBroishLive?.text = spannableString

        tiBroishLive?.setOnClickListener {  mainRouter.onNavigateToItem( NavItemAction.YouCountLive ) }
    }

    companion object {

        val TAG = HomeFragment::class.java.simpleName

        @JvmStatic
        fun newInstance() =
                HomeFragment().apply {
                }
    }
}