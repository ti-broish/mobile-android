package bg.dabulgaria.tibroish.infrastructure.di.components


import bg.dabulgaria.tibroish.TestApplication
import bg.dabulgaria.tibroish.persistence.remote.di.TestRemotePersistenceModule
import bg.dabulgaria.tibroish.domain.di.DomainModule
import bg.dabulgaria.tibroish.infrastructure.di.modules.*
import bg.dabulgaria.tibroish.persistence.local.di.LocalPersistenceModule
import bg.dabulgaria.tibroish.infrastructure.di.modules.TestApplicationModule

import javax.inject.Singleton

import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule

@Singleton // Constraints this component to one-per-application or unscoped bindings.
@Component(
        modules = arrayOf(AndroidInjectionModule::class,
                TestApplicationModule::class,
                ActivityModule::class,
                DeviceModule::class,
                NetworkModule::class,
                TestRemotePersistenceModule::class,
                LocalPersistenceModule::class,
                DomainModule::class,
                BuildModule::class)
)
interface TestApplicationComponent : ApplicationComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(app : TestApplication) : Builder
        //fun network(networkModule : NetworkModule) : Builder
        fun device( deviceModule : DeviceModule) : Builder
        fun build(): TestApplicationComponent
    }

    fun inject(application : TestApplication)
}
