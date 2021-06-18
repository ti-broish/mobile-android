package bg.dabulgaria.tibroish

import android.app.Application
import android.app.Service
import bg.dabulgaria.tibroish.infrastructure.di.components.DaggerApplicationComponent
import bg.dabulgaria.tibroish.infrastructure.di.modules.DeviceModule
import bg.dabulgaria.tibroish.presentation.notification.IChannelInitializer
import bg.dabulgaria.tibroish.presentation.push.IPushTokenSender
import com.google.firebase.FirebaseApp
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class DaApplication : Application(), HasAndroidInjector  {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>
    @Inject
    lateinit var pushTokenSender: IPushTokenSender
    @Inject
    lateinit var channelInitializer: IChannelInitializer

    override fun onCreate() {

        super.onCreate()
        FirebaseApp.initializeApp(this)

        DaggerApplicationComponent.builder()
                .application(this)
                .device( DeviceModule(this))
                .build()
                .inject(this)

        channelInitializer.initChannels()

        pushTokenSender.initPushToken()
    }

    override fun androidInjector(): AndroidInjector<Any> {

        return dispatchingAndroidInjector
    }
}
