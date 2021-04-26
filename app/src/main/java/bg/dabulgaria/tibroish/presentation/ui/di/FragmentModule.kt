package bg.dabulgaria.tibroish.presentation.ui.di

import bg.dabulgaria.tibroish.presentation.ui.home.HomeFragment
import bg.dabulgaria.tibroish.presentation.ui.protocol.add.*
import dagger.Binds
import dagger.Module
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
}