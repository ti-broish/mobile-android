package bg.dabulgaria.tibroish.presentation.ui.violation.details

import android.os.Bundle
import bg.dabulgaria.tibroish.presentation.base.BasePresenter
import bg.dabulgaria.tibroish.presentation.base.IBasePresenter
import bg.dabulgaria.tibroish.presentation.base.IDisposableHandler
import javax.inject.Inject

interface IViolationDetailsPresenter : IBasePresenter<IViolationDetailsView> {

}

class ViolationDetailsPresenter @Inject constructor(
    disposableHandler: IDisposableHandler
) : BasePresenter<IViolationDetailsView>(disposableHandler),
    IViolationDetailsPresenter {

    companion object {
        @JvmField
        val TAG: String = ViolationDetailsPresenter::class.java.name
    }

    override fun onRestoreData(bundle: Bundle?) {}

    override fun onSaveData(outState: Bundle) {}

    override fun loadData() {}

}
