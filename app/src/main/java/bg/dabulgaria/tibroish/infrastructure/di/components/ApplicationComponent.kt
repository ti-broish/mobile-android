package bg.dabulgaria.tibroish.infrastructure.di.components


import bg.dabulgaria.tibroish.DaApplication
import bg.dabulgaria.tibroish.infrastructure.di.modules.*

import javax.inject.Singleton

import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector

@Singleton // Constraints this component to one-per-application or unscoped bindings.
@Component(
        modules = arrayOf(AndroidInjectionModule::class,
                ApplicationModule::class,
                ActivityModule::class,
                DeviceModule::class,
                NetworkModule::class,
                BuildModule::class)
)
interface ApplicationComponent :AndroidInjector<DaApplication> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(app : DaApplication) : Builder
        //fun network(networkModule : NetworkModule) : Builder
        fun device( deviceModule : DeviceModule) : Builder
        fun build(): ApplicationComponent
    }

    override fun inject(application : DaApplication)
}
