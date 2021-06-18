package bg.dabulgaria.tibroish.presentation.push

import android.content.Intent
import bg.dabulgaria.tibroish.R
import bg.dabulgaria.tibroish.domain.organisation.ITiBroishRemoteRepository
import bg.dabulgaria.tibroish.domain.providers.ILogger
import bg.dabulgaria.tibroish.infrastructure.schedulers.ISchedulersProvider
import bg.dabulgaria.tibroish.presentation.main.IMainPresenter
import bg.dabulgaria.tibroish.presentation.main.IMainRouter
import bg.dabulgaria.tibroish.presentation.providers.INetworkInfoProvider
import bg.dabulgaria.tibroish.presentation.providers.IResourceProvider
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject


interface IPushActionRouter {

    fun onIntent(intent: Intent)
}

class PushActionRouter @Inject constructor(private val logger: ILogger,
                                           private val mainPresenter: IMainPresenter,
                                           private val mainRouter: IMainRouter,
                                           private val tiBroishRemoteRepo: ITiBroishRemoteRepository,
                                           private val schedulersProvider: ISchedulersProvider,
                                           private val networkInfoProvider: INetworkInfoProvider,
                                           private val resourceProvider: IResourceProvider) : IPushActionRouter {

    override fun onIntent(intent: Intent) {

        when (PushActionType[intent.extras?.getString(ACTION_TYPE_KEY) ?: ""]) {

            PushActionType.ShowScreen -> showScreen(intent)
        }
    }

    private fun showScreen(intent: Intent) {

        val entityId = intent.extras?.getString(ENTITY_ID_KEY)
        val detailMessage = intent.extras?.getString(DETAIL_MESSAGE_KEY)

        when (PushActionValuesShowScreen[intent.extras?.getString(ACTION_VALUE_KEY) ?: ""]) {

            PushActionValuesShowScreen.Protocols -> mainRouter.showMyProtocols()
            PushActionValuesShowScreen.ProtocolDetails -> showProtocolDetails(entityId, detailMessage)
            PushActionValuesShowScreen.Violations -> mainRouter.showViolations()
            PushActionValuesShowScreen.ViolationDetails -> showViolationDetails(entityId, detailMessage)
        }
    }

    private fun showProtocolDetails(protocolServerId: String?, detailsMessage: String?) {

        val protocolId = protocolServerId
                ?:return

        mainPresenter.onProcessStart()

        mainPresenter.add(Single.fromCallable { tiBroishRemoteRepo.getUserProtocols() }
                .subscribeOn(schedulersProvider.ioScheduler())
                .observeOn(schedulersProvider.uiScheduler())
                .subscribe( { protocols ->

                    val protocol = protocols.find { it.id.equals( protocolId, ignoreCase = true) }
                    mainPresenter.onProcessEnd()
                    protocol?.errorMessage = detailsMessage
                    protocol?.let {  mainRouter.showProtocolDetails(it) }
                },{ throwable ->

                    mainPresenter.onProcessEnd()
                    onError()
                    logger.e(TAG, throwable)
                }))
    }

    private fun showViolationDetails(violationServerId: String?, detailsMessage: String?) {

        val violationId = violationServerId
                ?:return

        mainPresenter.onProcessStart()

        mainPresenter.add(Single.fromCallable { tiBroishRemoteRepo.getViolations() }
                .subscribeOn(schedulersProvider.ioScheduler())
                .observeOn(schedulersProvider.uiScheduler())
                .subscribe( { violations ->

                    mainPresenter.onProcessEnd()
                    val violation = violations.find { it.id.equals( violationId, ignoreCase = true) }
                    violation?.errorMessage = detailsMessage
                    violation?.let{ mainRouter.showViolationDetails(it) }
                },{ throwable ->

                    mainPresenter.onProcessEnd()
                    onError()
                    logger.e(TAG, throwable)
                }))
    }

    fun onError(){

        val resId = if( !networkInfoProvider.isNetworkConnected )
            R.string.internet_connection_offline
        else
            R.string.oops_went_wrong_try

        mainRouter.showDismissableDialog(resourceProvider.getString(resId ), {})
    }

    companion object {

        val TAG = PushActionRouter::class.java.simpleName

        val ACTION_TYPE_KEY = "ActionType"
        val ACTION_VALUE_KEY = "ActionValue"
        val ENTITY_ID_KEY = "EntityId"
        val DETAIL_MESSAGE_KEY = "DetailMessage"
    }
}
