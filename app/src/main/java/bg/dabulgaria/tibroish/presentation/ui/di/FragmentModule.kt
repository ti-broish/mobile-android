package bg.dabulgaria.tibroish.presentation.ui.di

import bg.dabulgaria.tibroish.presentation.ui.auth.login.ILoginPresenter
import bg.dabulgaria.tibroish.presentation.ui.auth.login.LoginFragment
import bg.dabulgaria.tibroish.presentation.ui.auth.login.LoginPresenter
import bg.dabulgaria.tibroish.presentation.ui.forgotpassword.ForgotPasswordFragment
import bg.dabulgaria.tibroish.presentation.ui.forgotpassword.ForgotPasswordPresenter
import bg.dabulgaria.tibroish.presentation.ui.forgotpassword.IForgotPasswordPresenter
import bg.dabulgaria.tibroish.presentation.ui.home.HomeFragment
import bg.dabulgaria.tibroish.presentation.ui.photopicker.gallery.*
import bg.dabulgaria.tibroish.presentation.ui.protocol.add.*
import bg.dabulgaria.tibroish.presentation.ui.registration.IRegistrationPresenter
import bg.dabulgaria.tibroish.presentation.ui.registration.RegistrationFragment
import bg.dabulgaria.tibroish.presentation.ui.registration.RegistrationPresenter
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface FragmentModule {

    @ContributesAndroidInjector
    fun providesHomeFragment(): HomeFragment

    //region Add Protocol screen
    @ContributesAndroidInjector
    fun bindAddProtocolFragment(): AddProtocolFragment

    @Binds
    fun bindsAddProtocolView(detailsFragment: AddProtocolFragment): IAddProtocolView

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
}