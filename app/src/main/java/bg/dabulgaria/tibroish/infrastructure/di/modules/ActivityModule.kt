package bg.dabulgaria.tibroish.infrastructure.di.modules

import androidx.appcompat.app.AppCompatActivity
import bg.dabulgaria.tibroish.infrastructure.di.annotations.ActivityScope
import bg.dabulgaria.tibroish.infrastructure.permission.IPermissionRequester
import bg.dabulgaria.tibroish.infrastructure.permission.IPermissionResponseHandler
import bg.dabulgaria.tibroish.infrastructure.permission.PermissionRequester
import bg.dabulgaria.tibroish.infrastructure.permission.PermissionResponseHandler
import bg.dabulgaria.tibroish.presentation.base.DisposableHandler
import bg.dabulgaria.tibroish.presentation.base.IDisposableHandler
import bg.dabulgaria.tibroish.presentation.main.MainActivity
import dagger.Binds
import dagger.Module

@Module
interface ActivityModule {

    @Binds
    @ActivityScope
    fun bindsIDisposableHandler(implementation: DisposableHandler):IDisposableHandler

    @Binds
    @ActivityScope
    fun appCompatActivity(mainActivity: MainActivity): AppCompatActivity

    @Binds
    @ActivityScope
    fun bindsIPermissionRequester(implementation: PermissionRequester): IPermissionRequester

    @Binds
    @ActivityScope
    fun bindsIPermissionResponseHandler(implementation: PermissionResponseHandler): IPermissionResponseHandler

}