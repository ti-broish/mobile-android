package bg.dabulgaria.tibroish.infrastructure.di.modules

import bg.dabulgaria.tibroish.presentation.ui.login.ILoginPresenter
import bg.dabulgaria.tibroish.presentation.ui.login.LoginFragment
import bg.dabulgaria.tibroish.presentation.ui.login.LoginPresenter
import bg.dabulgaria.tibroish.presentation.ui.common.sectionpicker.ISectionPickerInteractor
import bg.dabulgaria.tibroish.presentation.ui.common.sectionpicker.SectionPickerInteractor
import bg.dabulgaria.tibroish.presentation.ui.checkin.*
import bg.dabulgaria.tibroish.presentation.ui.forgotpassword.ForgotPasswordFragment
import bg.dabulgaria.tibroish.presentation.ui.forgotpassword.ForgotPasswordPresenter
import bg.dabulgaria.tibroish.presentation.ui.forgotpassword.IForgotPasswordPresenter
import bg.dabulgaria.tibroish.presentation.ui.home.HomeFragment
import bg.dabulgaria.tibroish.presentation.ui.licenses.ILicensesPresenter
import bg.dabulgaria.tibroish.presentation.ui.licenses.LicensesFragment
import bg.dabulgaria.tibroish.presentation.ui.licenses.LicensesPresenter
import bg.dabulgaria.tibroish.presentation.ui.photopicker.gallery.*
import bg.dabulgaria.tibroish.presentation.ui.profile.IProfilePresenter
import bg.dabulgaria.tibroish.presentation.ui.profile.ProfileFragment
import bg.dabulgaria.tibroish.presentation.ui.profile.ProfilePresenter
import bg.dabulgaria.tibroish.presentation.ui.protocol.add.*
import bg.dabulgaria.tibroish.presentation.ui.protocol.details.IProtocolDetailsPresenter
import bg.dabulgaria.tibroish.presentation.ui.protocol.details.ProtocolDetailsFragment
import bg.dabulgaria.tibroish.presentation.ui.protocol.details.ProtocolDetailsPresenter
import bg.dabulgaria.tibroish.presentation.ui.protocol.list.IProtocolsPresenter
import bg.dabulgaria.tibroish.presentation.ui.protocol.list.ProtocolsFragment
import bg.dabulgaria.tibroish.presentation.ui.protocol.list.ProtocolsPresenter
import bg.dabulgaria.tibroish.presentation.ui.registration.IRegistrationPresenter
import bg.dabulgaria.tibroish.presentation.ui.registration.RegistrationFragment
import bg.dabulgaria.tibroish.presentation.ui.registration.RegistrationPresenter
import bg.dabulgaria.tibroish.presentation.ui.rights.IRightsAndObligationsPresenter
import bg.dabulgaria.tibroish.presentation.ui.rights.RightsAndObligationsFragment
import bg.dabulgaria.tibroish.presentation.ui.rights.RightsAndObligationsPresenter
import bg.dabulgaria.tibroish.presentation.ui.violation.details.IViolationDetailsPresenter
import bg.dabulgaria.tibroish.presentation.ui.violation.details.ViolationDetailsFragment
import bg.dabulgaria.tibroish.presentation.ui.violation.details.ViolationDetailsPresenter
import bg.dabulgaria.tibroish.presentation.ui.violation.list.IViolationsListPresenter
import bg.dabulgaria.tibroish.presentation.ui.violation.list.ViolationsListFragment
import bg.dabulgaria.tibroish.presentation.ui.violation.list.ViolationsListPresenter
import bg.dabulgaria.tibroish.presentation.ui.violation.send.*
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface FragmentModule {

    @ContributesAndroidInjector
    fun providesHomeFragment(): HomeFragment

    //region Section Picker
    @Binds
    fun bindsISectionPickerInteractor(implemenation: SectionPickerInteractor): ISectionPickerInteractor
    //endregion Section Picker

    //region Add Protocol screen
    @ContributesAndroidInjector
    fun bindAddProtocolFragment(): AddProtocolFragment

    @Binds
    fun bindsIAddProtocolPresenter(detailsPresenter: AddProtocolPresenter): IAddProtocolPresenter

    @Binds
    fun bindsIAddProtocolInteractor(detailsPresenter: AddProtocolInteractor): IAddProtocolInteractor
    //endregion Add Protocol screen

    //region Gallery Photo Picker screen
    @ContributesAndroidInjector
    fun bindPhotoPickerFragment(): PhotoPickerFragment

    @Binds
    fun bindsIPhotoPickerPresenter(implemenation: PhotoPickerPresenter): IPhotoPickerPresenter

    @Binds
    fun bindsIPhotoPickerInteractor(implemenation: PhotoPickerInteractor): IPhotoPickerInteractor

    @Binds
    fun bindsIPhotoItemTypeAdapter(implemenation: PhotoItemTypeAdapter): IPhotoItemTypeAdapter
    //endregion Gallery Photo Picker screen

    //region Login screen
    @ContributesAndroidInjector
    fun bindLoginFragment(): LoginFragment

    @Binds
    fun bindILoginPresenter(implementation: LoginPresenter): ILoginPresenter
    //endregion Login screen

    //region Registration screen
    @ContributesAndroidInjector
    fun bindRegistrationFragment(): RegistrationFragment

    @Binds
    fun bindIRegistrationPresenter(implementation: RegistrationPresenter): IRegistrationPresenter
    // endregion Registration screen

    //region Forgot Password screen
    @ContributesAndroidInjector
    fun bindForgotPasswordFragment(): ForgotPasswordFragment

    @Binds
    fun bindIForgotPasswordPresenter(implementation: ForgotPasswordPresenter):
            IForgotPasswordPresenter
    //endregion Forgot Password screen

    //region Profile screen
    @ContributesAndroidInjector
    fun bindProfileFragment(): ProfileFragment

    @Binds
    fun bindIProfilePresenter(implementation: ProfilePresenter):
            IProfilePresenter
    //endregion Profile screen

    //region Add Protocol screen
    @ContributesAndroidInjector
    fun bindSendViolationFragment(): SendViolationFragment

    @Binds
    fun bindsISendViolationPresenter(presenter: SendViolationPresenter): ISendViolationPresenter

    @Binds
    fun bindsISendViolationInteractor(interactor: SendViolationInteractor): ISendViolationInteractor
    //endregion Add Protocol screen

    //region RightsAndObligations screen
    @ContributesAndroidInjector
    fun bindRightsAndObligationsFragment(): RightsAndObligationsFragment

    @Binds
    fun bindIRightsAndObligationsPresenter(implemenation: RightsAndObligationsPresenter): IRightsAndObligationsPresenter
    //endregion Login screen

    //region My Protocols screen
    @ContributesAndroidInjector
    fun bindProtocolsFragment(): ProtocolsFragment

    @Binds
    fun bindIProtocolsPresenter(implementation: ProtocolsPresenter):
            IProtocolsPresenter
    //endregion My Protocols screen

    //region Protocol Details screen
    @ContributesAndroidInjector
    fun bindProtocolDetailsFragment(): ProtocolDetailsFragment

    @Binds
    fun bindIProtocolDetailsPresenter(implementation: ProtocolDetailsPresenter):
            IProtocolDetailsPresenter
    //endregion Protocol Details screen

    //region ViolationsList screen
    @ContributesAndroidInjector
    fun bindViolationsListFragment(): ViolationsListFragment

    @Binds
    fun bindIViolationsListPresenter(implemenation: ViolationsListPresenter): IViolationsListPresenter
    //endregion ViolationsList screen

    //region Send CheckIn screen
    @ContributesAndroidInjector
    fun bindSendCheckInFragment(): SendCheckInFragment

    @Binds
    fun bindsISendCheckInPresenter(presenter: SendCheckInPresenter): ISendCheckInPresenter

    @Binds
    fun bindsISendCheckInInteractor(interactor: SendCheckInInteractor): ISendCheckInInteractor
    //endregion Send CheckIn screen

    //region Violation Details screen
    @ContributesAndroidInjector
    fun bindViolationDetailsFragment(): ViolationDetailsFragment

    @Binds
    fun bindIViolationDetailsPresenter(implementation: ViolationDetailsPresenter):
            IViolationDetailsPresenter
    //endregion Violation Details screen

    //region Licenses screen
    @ContributesAndroidInjector
    fun bindLicensesFragment(): LicensesFragment

    @Binds
    fun bindILicensesPresenter(implementation: LicensesPresenter):
            ILicensesPresenter
    //endregion Licenses screen
}