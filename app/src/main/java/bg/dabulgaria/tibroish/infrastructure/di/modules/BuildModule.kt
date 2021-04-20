package bg.dabulgaria.tibroish.infrastructure.di.modules

import bg.dabulgaria.tibroish.infrastructure.di.annotations.ActivityScope
import bg.dabulgaria.tibroish.presentation.ui.protocol.add.ComicDetailsFragmentModule
import bg.dabulgaria.tibroish.presentation.ui.protocol.list.ComicListFragmentModule
import bg.dabulgaria.tibroish.presentation.main.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface BuildModule {

    @ContributesAndroidInjector(modules = arrayOf(ActivityModule::class, ComicListFragmentModule::class, ComicDetailsFragmentModule::class))
    @ActivityScope
    fun bindsMainActivity(): MainActivity
}