package bg.dabulgaria.tibroish.infrastructure.di.modules

import android.content.Context
import bg.dabulgaria.tibroish.presentation.providers.INetworkInfoProvider
import bg.dabulgaria.tibroish.presentation.providers.IResourceProvider
import bg.dabulgaria.tibroish.presentation.providers.NetworkInfoProvider
import bg.dabulgaria.tibroish.presentation.providers.ResourceProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DeviceModule(val context: Context) {

    @Provides
    @Singleton
    fun providesNetworkInfoProvider(): INetworkInfoProvider {

        return NetworkInfoProvider(context)
    }

    @Provides
    @Singleton
    fun providesResourceProvider(): IResourceProvider {

        return ResourceProvider(context)
    }

}