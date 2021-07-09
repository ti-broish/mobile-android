package bg.dabulgaria.tibroish.presentation.ui.licenses

import android.os.Bundle
import bg.dabulgaria.tibroish.R
import bg.dabulgaria.tibroish.domain.providers.ILogger
import bg.dabulgaria.tibroish.presentation.base.BasePresenter
import bg.dabulgaria.tibroish.presentation.base.IBasePresenter
import bg.dabulgaria.tibroish.presentation.base.IDisposableHandler
import bg.dabulgaria.tibroish.infrastructure.schedulers.ISchedulersProvider
import bg.dabulgaria.tibroish.presentation.main.IMainRouter


import javax.inject.Inject

interface ILicensesPresenter: IBasePresenter<ILicensesView> {
    fun getLicensesText(): String
}

class LicensesPresenter @Inject constructor(private val mainRouter : IMainRouter,
                                               private val logger : ILogger,
                                               dispHandler: IDisposableHandler)
    : BasePresenter<ILicensesView>(dispHandler), ILicensesPresenter{

    var data :LicensesViewData? = null

    //region ILicensesPresenter implementation

    override fun onRestoreData(bundle: Bundle?) {
        bundle?.let {
            data = bundle.getSerializable(LicensesConstants.VIEW_DATA_KEY) as LicensesViewData?
        }
    }

    override fun onSaveData(outState: Bundle) {
        outState.putSerializable(LicensesConstants.VIEW_DATA_KEY, data)
    }

    override fun loadData() {
        if (data == null) {
            data = LicensesViewData()
        }
        if (data?.licensesText == null) {
            data?.licensesText = resourceProvider.getString(R.string.licenses_text)
        }
    }

    override fun onViewHide() {
        mainRouter.permissionResponseListener = null
        super.onViewHide()
    }

    override fun onError( throwable : Throwable? ){

        logger.e(TAG, throwable)

        super.onError(throwable)
    }

    override fun getLicensesText(): String {
        loadData()
        return data?.licensesText!!
    }

    //endregion ILicensesPresenter implementation


    companion object {

        private val TAG = LicensesPresenter::class.simpleName
    }
}
