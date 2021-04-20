package bg.dabulgaria.tibroish.di.modules

import bg.dabulgaria.tibroish.di.annotations.ActivityScope
import bg.dabulgaria.tibroish.presentation.base.DisposableHandler
import bg.dabulgaria.tibroish.presentation.base.IDisposableHandler
import bg.dabulgaria.tibroish.presentation.main.IMainNavigator
import bg.dabulgaria.tibroish.presentation.main.MainActivity
import dagger.Binds
import dagger.Module

@Module
interface ActivityModule {

    @Binds
    @ActivityScope
    fun bindsMainNavigator(mainActivity: MainActivity): IMainNavigator

    @Binds
    @ActivityScope
    fun bindsIDisposableHandler(disposableHandler: DisposableHandler):IDisposableHandler
}