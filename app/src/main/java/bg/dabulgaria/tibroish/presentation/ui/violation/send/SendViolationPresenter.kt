package bg.dabulgaria.tibroish.presentation.ui.violation.send

import bg.dabulgaria.tibroish.R
import bg.dabulgaria.tibroish.domain.io.IFileRepository
import bg.dabulgaria.tibroish.domain.providers.ILogger
import bg.dabulgaria.tibroish.presentation.base.IDisposableHandler
import bg.dabulgaria.tibroish.infrastructure.schedulers.ISchedulersProvider
import bg.dabulgaria.tibroish.presentation.ui.common.item.send.ISendItemPresenter
import bg.dabulgaria.tibroish.presentation.ui.common.item.send.SendItemPresenter
import bg.dabulgaria.tibroish.presentation.main.IMainRouter
import bg.dabulgaria.tibroish.presentation.providers.ICameraTakenImageProvider
import bg.dabulgaria.tibroish.presentation.ui.common.item.send.SendItemViewData


import javax.inject.Inject

interface ISendViolationPresenter: ISendItemPresenter

class SendViolationPresenter @Inject constructor(schedulersProvider: ISchedulersProvider,
                                               mainRouter: IMainRouter,
                                               interactor: ISendViolationInteractor,
                                               fileRepository: IFileRepository,
                                               logger: ILogger,
                                               dispHandler: IDisposableHandler,
                                                 cameraTakenImageProvider: ICameraTakenImageProvider)
    : SendItemPresenter(schedulersProvider,
        mainRouter,
        interactor,
        fileRepository,
        logger,
        dispHandler,
        cameraTakenImageProvider),
        ISendViolationPresenter{

    override val MinSelectedImages: Int = 0

    override fun validateData(data: SendItemViewData): Boolean {

        if(data.sectionsData?.selectedTown == null){

            view?.onError(resourceProvider.getString(R.string.please_choose_town))
            return false
        }

        if(data.sectionsData?.selectedTown?.cityRegions.orEmpty().isNotEmpty()
                && data.sectionsData?.selectedCityRegion == null){

            view?.onError(resourceProvider.getString(R.string.please_choose_city_region))
            return false
        }

        if(data.message.length < DESCRIPTION_MIN_LENGTH ){
            view?.onError(resourceProvider.getString(R.string.please_type_description_more_than_d_symbols,
                    DESCRIPTION_MIN_LENGTH))
            return false
        }

        return true
    }

    companion object{

        const val DESCRIPTION_MIN_LENGTH = 20
    }
}