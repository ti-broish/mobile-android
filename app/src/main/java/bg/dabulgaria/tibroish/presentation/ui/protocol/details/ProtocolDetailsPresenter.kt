package bg.dabulgaria.tibroish.presentation.ui.protocol.details

import android.os.Bundle
import bg.dabulgaria.tibroish.presentation.base.BasePresenter
import bg.dabulgaria.tibroish.presentation.base.IBasePresenter
import bg.dabulgaria.tibroish.presentation.base.IDisposableHandler
import javax.inject.Inject

interface IProtocolDetailsPresenter : IBasePresenter<IProtocolDetailsView> {

}

class ProtocolDetailsPresenter @Inject constructor(
    disposableHandler: IDisposableHandler
) : BasePresenter<IProtocolDetailsView>(disposableHandler),
    IProtocolDetailsPresenter {

    companion object {
        @JvmField
        val TAG: String = ProtocolDetailsPresenter::class.java.name
    }

    override fun onRestoreData(bundle: Bundle?) {}

    override fun onSaveData(outState: Bundle) {}

    override fun loadData() {}

}
