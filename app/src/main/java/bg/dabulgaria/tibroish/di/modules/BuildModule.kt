package bg.dabulgaria.tibroish.di.modules

import bg.dabulgaria.tibroish.di.annotations.ActivityScope
import bg.dabulgaria.tibroish.presentation.comic.details.di.ComicDetailsFragmentModule
import bg.dabulgaria.tibroish.presentation.comic.list.di.ComicListFragmentModule
import bg.dabulgaria.tibroish.presentation.main.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface BuildModule {

    @ContributesAndroidInjector(modules = arrayOf(ActivityModule::class, ComicListFragmentModule::class, ComicDetailsFragmentModule::class))
    @ActivityScope
    fun bindsMainActivity(): MainActivity
}