package bg.dabulgaria.tibroish.presentation.ui.protocol.list

import android.os.Bundle
import bg.dabulgaria.tibroish.domain.organisation.ITiBroishRemoteRepository
import bg.dabulgaria.tibroish.domain.protocol.IProtocolsRepository
import bg.dabulgaria.tibroish.domain.protocol.ProtocolRemote
import bg.dabulgaria.tibroish.presentation.base.BasePresenter
import bg.dabulgaria.tibroish.presentation.base.IBasePresenter
import bg.dabulgaria.tibroish.presentation.base.IDisposableHandler
import bg.dabulgaria.tibroish.presentation.main.IMainRouter
import bg.dabulgaria.tibroish.presentation.ui.profile.ProfileConstants.Companion.VIEW_DATA_KEY
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface IProtocolsPresenter : IBasePresenter<IProtocolsView> {
    fun getMyProtocols(initialLoading: Boolean, successCallback: (List<ProtocolRemote>) -> Unit)

    fun getState(): ProtocolsPresenter.State

    fun getCachedProtocols(): List<ProtocolRemote>?

    fun showProtocolAt(position: Int)
}

class ProtocolsPresenter @Inject constructor(
    disposableHandler: IDisposableHandler,
    private val tiBroishRemoteRepository: ITiBroishRemoteRepository,
    private val mainRouter: IMainRouter,
    private val protocolsRepository: IProtocolsRepository,
    private val gson: Gson,
) : BasePresenter<IProtocolsView>(disposableHandler),
    IProtocolsPresenter {

    private var viewData: ProtocolsViewData? = null

    companion object {
        @JvmField
        val TAG: String = ProtocolsPresenter::class.java.name
    }

    enum class State {
        STATE_LOADING_INITIAL,
        STATE_LOADING_SUBSEQUENT,
        STATE_LOADED_SUCCESS,
        STATE_LOADED_FAILURE
    }

    override fun getMyProtocols(
        initialLoading: Boolean,
        successCallback: (List<ProtocolRemote>) -> Unit) {
        if (!initialLoading) {
            viewData?.state = State.STATE_LOADING_SUBSEQUENT
        }
        if (viewData?.state == State.STATE_LOADED_SUCCESS) {
            successCallback.invoke(viewData?.userProtocols!!)
            return
        }
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val localProtocols = protocolsRepository.getAll()
                val userProtocols = localProtocols.map {
                    gson.fromJson(it.remoteProtocolJson, ProtocolRemote::class.java)
                }
                viewData?.userProtocols = userProtocols
                viewData?.state = State.STATE_LOADED_SUCCESS
                withContext(Dispatchers.Main) {
                    successCallback.invoke(userProtocols)
                }
            } catch (exception: Exception) {
                viewData?.state = State.STATE_LOADED_FAILURE
                withContext(Dispatchers.Main) {
                    onError(exception)
                }
            }
        }
    }

    override fun getState(): State {
        return viewData?.state ?: State.STATE_LOADING_INITIAL
    }

    override fun getCachedProtocols(): List<ProtocolRemote>? {
        return viewData?.userProtocols
    }

    override fun showProtocolAt(position: Int) {
        viewData?.userProtocols?.get(position)?.let { mainRouter.showProtocolDetails(it) }
    }

    override fun onRestoreData(bundle: Bundle?) {
        viewData = bundle?.getParcelable(
            VIEW_DATA_KEY
        )
            ?: ProtocolsViewData(
                /* userProtocols= */ mutableListOf(),
                State.STATE_LOADING_INITIAL
            )
    }

    override fun onSaveData(outState: Bundle) {
        outState.putParcelable(ProtocolsConstants.VIEW_DATA_KEY, viewData)
    }

    override fun loadData() {
        if (viewData == null) {
            viewData = ProtocolsViewData(
                /* userProtocols= */ mutableListOf(),
                State.STATE_LOADING_INITIAL
            )
        }
    }
}
