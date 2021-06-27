package bg.dabulgaria.tibroish

import bg.dabulgaria.tibroish.infrastructure.di.components.DaggerApplicationComponent
import bg.dabulgaria.tibroish.infrastructure.di.modules.DeviceModule
import dagger.android.AndroidInjector
import javax.inject.Inject

class TestApplication : DaApplication() {

    override fun createApplicationComponent() = DaggerTestApplicationComponent.builder()
        .application(this)
        .device(DeviceModule(this))
        .build()
}