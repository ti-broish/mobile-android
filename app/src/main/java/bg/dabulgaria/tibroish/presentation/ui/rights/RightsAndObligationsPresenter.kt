package bg.dabulgaria.tibroish.presentation.ui.rights

import android.os.Bundle
import bg.dabulgaria.tibroish.domain.providers.ILogger
import bg.dabulgaria.tibroish.presentation.base.BasePresenter
import bg.dabulgaria.tibroish.presentation.base.IBasePresenter
import bg.dabulgaria.tibroish.presentation.base.IDisposableHandler
import bg.dabulgaria.tibroish.infrastructure.schedulers.ISchedulersProvider
import bg.dabulgaria.tibroish.presentation.main.IMainRouter


import javax.inject.Inject

interface IRightsAndObligationsPresenter: IBasePresenter<IRightsAndObligationsView> {

}

class RightsAndObligationsPresenter @Inject constructor(private val schedulersProvider : ISchedulersProvider,
                                               private val mainRouter : IMainRouter,
                                               private val logger : ILogger,
                                               dispHandler: IDisposableHandler)
    : BasePresenter<IRightsAndObligationsView>(dispHandler), IRightsAndObligationsPresenter{

    var data :RightsAndObligationsViewData? = null

    //region IRightsAndObligationsPresenter implementation
    override fun onRestoreData(bundle: Bundle?) {
        bundle?.let {
            data = bundle.getSerializable(RightsAndObligationsConstants.VIEW_DATA_KEY) as RightsAndObligationsViewData?
        }
    }

    override fun onSaveData(outState: Bundle) {
        outState.putSerializable(RightsAndObligationsConstants.VIEW_DATA_KEY, data)
    }

    override fun loadData() {

        data?.title = "Test"
        data?.subtitle = "Test subtitle"
        data?.rightsAndObligationsText = "text"
    }

    override fun onViewHide() {
        mainRouter.permissionResponseListener = null
        super.onViewHide()
    }

    override fun onError( throwable : Throwable? ){

        logger.e(TAG, throwable)

        super.onError(throwable)
    }
    //endregion IRightsAndObligationsPresenter implementation



    companion object {

        private val TAG = RightsAndObligationsPresenter::class.simpleName
    }
}
