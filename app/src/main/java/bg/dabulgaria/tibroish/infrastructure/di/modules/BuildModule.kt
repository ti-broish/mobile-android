package bg.dabulgaria.tibroish.infrastructure.di.modules

import bg.dabulgaria.tibroish.infrastructure.di.annotations.ActivityScope
import bg.dabulgaria.tibroish.persistence.local.di.LocalPersistenceModule
import bg.dabulgaria.tibroish.presentation.main.MainActivity
import bg.dabulgaria.tibroish.presentation.ui.di.FragmentModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface BuildModule {

    @ContributesAndroidInjector(modules = [ActivityModule::class,
        FragmentModule::class,
        LocalPersistenceModule::class])

    @ActivityScope
    fun bindsMainActivity(): MainActivity
}