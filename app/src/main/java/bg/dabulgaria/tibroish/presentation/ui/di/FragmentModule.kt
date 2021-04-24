package bg.dabulgaria.tibroish.presentation.ui.di

import bg.dabulgaria.tibroish.presentation.ui.home.HomeFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract fun providesHomeFragment(): HomeFragment
}