package bg.dabulgaria.tibroish.presentation.ui.protocol.add

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

interface IAddProtocolPresenter: ISendItemPresenter

class AddProtocolPresenter @Inject constructor(schedulersProvider: ISchedulersProvider,
                                               mainRouter: IMainRouter,
                                               interactor: IAddProtocolInteractor,
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
        IAddProtocolPresenter{

    override val MinSelectedImages: Int = 4

    override fun validateData(data: SendItemViewData): Boolean {


        if (!isManualSectionValid(data.manualSectionId)) {

            return false
        }

        return true
    }

    override fun onManualSectionChanged(sectionId: String) {
        data?.manualSectionId = sectionId
    }
}