package bg.dabulgaria.tibroish

import bg.dabulgaria.tibroish.infrastructure.di.components.DaggerTestApplicationComponent
import bg.dabulgaria.tibroish.infrastructure.di.modules.DeviceModule

class TestApplication : DaApplication() {

    override fun createApplicationComponent() = DaggerTestApplicationComponent.builder()
        .application(this)
        .device(DeviceModule(this))
        .build()
}