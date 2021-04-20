package bg.dabulgaria.tibroish

import android.app.Application
import bg.dabulgaria.tibroish.di.components.DaggerApplicationComponent

import bg.dabulgaria.tibroish.di.modules.DeviceModule
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector

import javax.inject.Inject

class DaApplication : Application(), HasAndroidInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate() {

        super.onCreate()

        DaggerApplicationComponent.builder()
                .application( this )
                .device( DeviceModule( this ) )
                .build()
                .inject(this)
    }

    override fun androidInjector(): AndroidInjector<Any> {

        return dispatchingAndroidInjector
    }

//    override fun activityInjector(): AndroidInjector<Activity> {
//       return dispatchingAndroidInjector
//    }
}
