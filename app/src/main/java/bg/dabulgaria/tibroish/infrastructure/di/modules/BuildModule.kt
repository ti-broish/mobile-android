package bg.dabulgaria.tibroish.infrastructure.di.modules

import bg.dabulgaria.tibroish.infrastructure.di.annotations.ActivityScope
import bg.dabulgaria.tibroish.live.FetchStreamActivity
import bg.dabulgaria.tibroish.presentation.main.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface BuildModule {

    @ContributesAndroidInjector(modules = [ActivityModule::class, FragmentModule::class])
    @ActivityScope
    fun bindsMainActivity(): MainActivity

    @ContributesAndroidInjector
    @ActivityScope
    fun bindsFetchStreamActivity(): FetchStreamActivity

}
