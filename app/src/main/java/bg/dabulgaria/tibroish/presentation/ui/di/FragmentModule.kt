package bg.dabulgaria.tibroish.presentation.ui.di

import bg.dabulgaria.tibroish.presentation.ui.home.HomeFragment
import bg.dabulgaria.tibroish.presentation.ui.photopicker.gallery.*
import bg.dabulgaria.tibroish.presentation.ui.protocol.add.*
import dagger.Binds
import dagger.BindsInstance
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector

@Module
interface FragmentModule {

    @ContributesAndroidInjector
    fun providesHomeFragment(): HomeFragment

    //region Add Protocol screen
    @ContributesAndroidInjector
    fun bindAddProtocolFragment(): AddProtocolFragment

    @Binds
    fun bindsAddProtocolView( detailsFragment: AddProtocolFragment): IAddProtocolView

    @Binds
    fun bindsIAddProtocolPresenter( detailsPresenter: AddProtocolPresenter): IAddProtocolPresenter

    @Binds
    fun bindsIAddProtocolInteractor( detailsPresenter: AddProtocolInteractor): IAddProtocolInteractor
    //endregion Add Protocol screen

    //region Gallery Photo Picker screen
    @ContributesAndroidInjector
    fun bindPhotoPickerFragment(): PhotoPickerFragment

    @Binds
    fun bindsIPhotoPickerPresenter( implemenation: PhotoPickerPresenter): IPhotoPickerPresenter

    @Binds
    fun bindsIPhotoPickerInteractor( implemenation: PhotoPickerInteractor): IPhotoPickerInteractor

    @Binds
    fun bindsIPhotoItemTypeAdapter( implemenation: PhotoItemTypeAdapter): IPhotoItemTypeAdapter
    //endregion Gallery Photo Picker screen


}