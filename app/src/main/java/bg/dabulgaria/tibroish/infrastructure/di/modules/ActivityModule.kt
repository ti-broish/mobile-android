package bg.dabulgaria.tibroish.infrastructure.di.modules

import bg.dabulgaria.tibroish.infrastructure.di.annotations.ActivityScope
import bg.dabulgaria.tibroish.presentation.base.DisposableHandler
import bg.dabulgaria.tibroish.presentation.base.IDisposableHandler
import bg.dabulgaria.tibroish.presentation.main.IMainNavigator
import bg.dabulgaria.tibroish.presentation.main.MainNavigator
import dagger.Binds
import dagger.Module

@Module
interface ActivityModule {

    @Binds
    @ActivityScope
    fun bindsMainNavigator(mainActivity: MainNavigator): IMainNavigator

    @Binds
    @ActivityScope
    fun bindsIDisposableHandler(disposableHandler: DisposableHandler):IDisposableHandler
}