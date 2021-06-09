package bg.dabulgaria.tibroish.presentation.ui.protocol.list

import android.os.Bundle
import androidx.annotation.ColorInt
import bg.dabulgaria.tibroish.R
import bg.dabulgaria.tibroish.domain.organisation.ITiBroishRemoteRepository
import bg.dabulgaria.tibroish.domain.violation.ViolationRemoteStatus
import bg.dabulgaria.tibroish.domain.violation.VoteViolationRemote
import bg.dabulgaria.tibroish.infrastructure.schedulers.ISchedulersProvider
import bg.dabulgaria.tibroish.presentation.base.BasePresenter
import bg.dabulgaria.tibroish.presentation.base.IBasePresenter
import bg.dabulgaria.tibroish.presentation.base.IDisposableHandler
import bg.dabulgaria.tibroish.presentation.main.IMainRouter
import bg.dabulgaria.tibroish.presentation.providers.IResourceProvider
import bg.dabulgaria.tibroish.presentation.ui.violation.list.IViolationsListView
import bg.dabulgaria.tibroish.presentation.ui.violation.list.ViolationListViewData
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

interface IViolationsListPresenter : IBasePresenter<IViolationsListView> {

    fun onItemClick(viewData : VoteViolationRemote)

    fun getStatusColor(violationRemoteStatus: ViolationRemoteStatus): Int
}

class ViolationsListPresenter @Inject constructor(private val schedulersProvider : ISchedulersProvider,
                                                  private val tiBroishRemoteRepository: ITiBroishRemoteRepository,
                                                  dispHandler: IDisposableHandler,
                                                  private val mainRouter: IMainRouter)
    : BasePresenter<IViolationsListView>(dispHandler), IViolationsListPresenter {

    var viewData = ViolationListViewData()

    override fun onRestoreData(bundle: Bundle?) {}

    override fun onSaveData(outState: Bundle) {}

    override fun onItemClick(viewData: VoteViolationRemote) {

        //mainRouter.showViolationDetails( viewData )
    }

    override fun loadData() {

        if(viewData.items.isNotEmpty())
            view?.setData(viewData)

        view?.onLoadingStateChange(true)

        add(Single.fromCallable{ tiBroishRemoteRepository.getViolations() }
                .subscribeOn( schedulersProvider.ioScheduler())
                .observeOn( schedulersProvider.uiScheduler())
                .subscribe({ result ->

                    viewData.items.clear()
                    viewData.items.addAll(result)
                    view?.setData(viewData)
                    view?.onLoadingStateChange(false )
                },
                        { th:Throwable->
                            view?.onLoadingStateChange(false)
                            onError( throwable = th )
                        }))
    }

    override fun getStatusColor(violationRemoteStatus: ViolationRemoteStatus): Int = resourceProvider.getColor(when (violationRemoteStatus) {
        ViolationRemoteStatus.Processed -> R.color.color_status_processed
        ViolationRemoteStatus.Received -> R.color.color_status_received
        ViolationRemoteStatus.Approved -> R.color.color_status_approved
        ViolationRemoteStatus.Rejected -> R.color.color_status_rejected
        else -> R.color.color_status_unknown
    })
}
