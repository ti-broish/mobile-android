package bg.dabulgaria.tibroish.presentation.ui.violation.send


import android.text.TextUtils
import android.util.Patterns
import bg.dabulgaria.tibroish.R
import bg.dabulgaria.tibroish.domain.io.IFileRepository
import bg.dabulgaria.tibroish.domain.providers.ILogger
import bg.dabulgaria.tibroish.infrastructure.permission.IPermissionRequester
import bg.dabulgaria.tibroish.infrastructure.schedulers.ISchedulersProvider
import bg.dabulgaria.tibroish.presentation.base.IDisposableHandler
import bg.dabulgaria.tibroish.presentation.main.IMainRouter
import bg.dabulgaria.tibroish.presentation.providers.ICameraTakenImageProvider
import bg.dabulgaria.tibroish.presentation.ui.common.item.send.ISendItemPresenter
import bg.dabulgaria.tibroish.presentation.ui.common.item.send.SendItemPresenter
import bg.dabulgaria.tibroish.presentation.ui.common.item.send.SendItemViewData
import javax.inject.Inject


interface ISendViolationPresenter: ISendItemPresenter

class SendViolationPresenter @Inject constructor(schedulersProvider: ISchedulersProvider,
                                                 mainRouter: IMainRouter,
                                                 interactor: ISendViolationInteractor,
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
        ISendViolationPresenter{

    override val MinSelectedImages: Int = 0

    override fun validateData(data: SendItemViewData): Boolean {

        var valid = true
        var errors = ""
        if(data.sectionsData?.selectedTown == null){

            errors += resourceProvider.getString(R.string.please_choose_town) +"\n\n"
            valid = false
        }

        if(data.sectionsData?.selectedTown?.cityRegions.orEmpty().isNotEmpty()
                && data.sectionsData?.selectedCityRegion == null){

            errors += resourceProvider.getString(R.string.please_choose_city_region) +"\n\n"
            valid = false
        }

        if(data.message.length < DESCRIPTION_MIN_LENGTH ){

            errors += resourceProvider.getString(R.string.please_type_description_more_than_d_symbols,
                    DESCRIPTION_MIN_LENGTH)+"\n\n"
            valid = false
        }

        if(data.names.length < NAMES_MIN_LENGTH){

            errors += resourceProvider.getString(
                R.string.please_type_name_more_than_d_symbols,
                NAMES_MIN_LENGTH-1
            )+"\n\n"
            valid = false
        }

        if(data.phone.length < PHONE_MIN_LENGTH){

            errors += resourceProvider.getString(
                R.string.please_type_phone_more_than_d_symbols,
                PHONE_MIN_LENGTH -1
            )+"\n\n"
            valid = false
        }

        if(!isValidEmail(data.email)){

            errors += resourceProvider.getString(R.string.invalid_email)+"\n\n"
            valid = false
        }

        if(!valid)
            view?.onError(errors)

        return valid
    }

    companion object{

        const val DESCRIPTION_MIN_LENGTH = 20
        const val NAMES_MIN_LENGTH = 5
        const val PHONE_MIN_LENGTH = 8

        fun isValidEmail(target: CharSequence?): Boolean {
            return !target.isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(target).matches()
        }
    }

    override fun onManualSectionChanged(sectionId: String) {}
}