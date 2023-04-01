package bg.dabulgaria.tibroish.presentation.ui.violation.list

import android.os.Bundle
import bg.dabulgaria.tibroish.domain.organisation.ITiBroishRemoteRepository
import bg.dabulgaria.tibroish.domain.providers.ILogger
import bg.dabulgaria.tibroish.domain.violation.IViolationRepository
import bg.dabulgaria.tibroish.domain.violation.VoteViolationRemote
import bg.dabulgaria.tibroish.infrastructure.schedulers.ISchedulersProvider
import bg.dabulgaria.tibroish.persistence.local.Logger
import bg.dabulgaria.tibroish.presentation.base.BasePresenter
import bg.dabulgaria.tibroish.presentation.base.IBasePresenter
import bg.dabulgaria.tibroish.presentation.base.IDisposableHandler
import bg.dabulgaria.tibroish.presentation.main.IMainRouter
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface IViolationsListPresenter : IBasePresenter<IViolationsListView> {

    fun getMyViolations(initialLoading: Boolean, successCallback: (List<VoteViolationRemote>) -> Unit)

    fun getState(): ViolationsListPresenter.State

    fun getCachedViolations(): List<VoteViolationRemote>?

    fun showViolationAt(position: Int)
}

class ViolationsListPresenter @Inject constructor(
    private val tiBroishRemoteRepository: ITiBroishRemoteRepository,
    dispHandler: IDisposableHandler,
    private val mainRouter: IMainRouter,
    private val violationRepository: IViolationRepository,
    private val gson: Gson,
    private val logger: ILogger
    )
    : BasePresenter<IViolationsListView>(dispHandler), IViolationsListPresenter {

    enum class State {
        STATE_LOADING_INITIAL,
        STATE_LOADING_SUBSEQUENT,
        STATE_LOADED_SUCCESS,
        STATE_LOADED_FAILURE
    }

    private var viewData: ViolationsViewData? = null

    override fun getMyViolations(
        initialLoading: Boolean,
        successCallback: (List<VoteViolationRemote>) -> Unit) {
        if (!initialLoading) {
            viewData?.state = State.STATE_LOADING_SUBSEQUENT
        }
        if (viewData?.state == State.STATE_LOADED_SUCCESS) {
            successCallback.invoke(viewData?.userViolations!!)
            return
        }
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val local = violationRepository.getAll()
                val userViolations = mutableListOf<VoteViolationRemote>()

                local.forEach {
                    if(!it.remoteViolationJson.isNullOrEmpty()) {
                        val violationRemote = gson.fromJson(it.remoteViolationJson, VoteViolationRemote::class.java)

                        violationRemote?.section?.let {
                            userViolations.add(violationRemote)
                        }
                    }
                }
                viewData?.userViolations = userViolations
                viewData?.state = State.STATE_LOADED_SUCCESS
                withContext(Dispatchers.Main) {
                    successCallback.invoke(userViolations)
                }
            } catch (exception: Exception) {

                logger.e(TAG, exception)
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

    override fun getCachedViolations(): List<VoteViolationRemote>? {
        return viewData?.userViolations
    }

    override fun showViolationAt(position: Int) {
        viewData?.userViolations?.get(position)?.let {
            mainRouter.showViolationDetails(it)
        }
    }

    override fun onRestoreData(bundle: Bundle?) {
        viewData = bundle?.getParcelable(
            ViolationConstants.VIEW_DATA_KEY
        )
            ?: ViolationsViewData(
                /* userViolation= */ mutableListOf(),
                State.STATE_LOADING_INITIAL
            )
    }

    override fun onSaveData(outState: Bundle) {
        outState.putParcelable(ViolationConstants.VIEW_DATA_KEY, viewData)
    }

    override fun loadData() {
        if (viewData == null) {
            viewData = ViolationsViewData(
                /* userViolations= */ mutableListOf(),
                State.STATE_LOADING_INITIAL
            )
        }
    }

    companion object{
        val TAG = ViolationsListPresenter::class.java.simpleName
    }
}
