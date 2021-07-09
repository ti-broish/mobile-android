package bg.dabulgaria.tibroish.presentation.ui.checkin

import bg.dabulgaria.tibroish.R
import bg.dabulgaria.tibroish.domain.io.IFileRepository
import bg.dabulgaria.tibroish.domain.providers.ILogger
import bg.dabulgaria.tibroish.infrastructure.permission.IPermissionRequester
import bg.dabulgaria.tibroish.presentation.base.IDisposableHandler
import bg.dabulgaria.tibroish.infrastructure.schedulers.ISchedulersProvider
import bg.dabulgaria.tibroish.presentation.ui.common.item.send.ISendItemPresenter
import bg.dabulgaria.tibroish.presentation.ui.common.item.send.SendItemPresenter
import bg.dabulgaria.tibroish.presentation.main.IMainRouter
import bg.dabulgaria.tibroish.presentation.providers.ICameraTakenImageProvider
import bg.dabulgaria.tibroish.presentation.ui.common.item.send.SendItemViewData


import javax.inject.Inject

interface ISendCheckInPresenter: ISendItemPresenter

class SendCheckInPresenter @Inject constructor(schedulersProvider: ISchedulersProvider,
                                               mainRouter: IMainRouter,
                                               interactor: ISendCheckInInteractor,
                                               fileRepository: IFileRepository,
                                               logger: ILogger,
                                               dispHandler: IDisposableHandler,
                                               cameraTakenImageProvider: ICameraTakenImageProvider,
                                               permissionRequester : IPermissionRequester)
    : SendItemPresenter(schedulersProvider,
        mainRouter,
        interactor,
        fileRepository,
        logger,
        dispHandler,
        cameraTakenImageProvider,
        permissionRequester),
        ISendCheckInPresenter{

    override val MinSelectedImages: Int = 0

    override fun validateData(data: SendItemViewData): Boolean {

        if(data.sectionsData?.selectedSection == null){

            view?.onError(resourceProvider.getString(R.string.please_choose_section))
            return false
        }

        return true
    }

    companion object{

    }

    override fun onManualSectionChanged(sectionId: String) {}
}