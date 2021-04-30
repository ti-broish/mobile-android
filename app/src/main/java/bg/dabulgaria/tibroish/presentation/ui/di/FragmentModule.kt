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

    @ContributesAndroidInjector
    fun bindAddProtocolFragment(): AddProtocolFragment

    @Binds
    fun bindsDetailsView( detailsFragment: AddProtocolFragment): IAddProtocolView

    @Binds
    fun bindsIAddProtocolPresenter( detailsPresenter: AddProtocolPresenter): IAddProtocolPresenter

    //region Gallery PhotoPicker
    @ContributesAndroidInjector
    fun bindPhotoPickerFragment(): PhotoPickerFragment

    @Binds
    fun bindsIPhotoPickerPresenter( implemenation: PhotoPickerPresenter): IPhotoPickerPresenter

    @Binds
    fun bindsIPhotoPickerInteractor( implemenation: PhotoPickerInteractor): IPhotoPickerInteractor

    @Binds
    fun bindsIPhotoItemTypeAdapter( implemenation: PhotoItemTypeAdapter): IPhotoItemTypeAdapter
    //endregion Gallery PhotoPicker


}