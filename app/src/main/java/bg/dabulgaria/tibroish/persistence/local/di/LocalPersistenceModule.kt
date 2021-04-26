package bg.dabulgaria.tibroish.persistence.local.di

import bg.dabulgaria.tibroish.domain.protocol.IProtocolsRepository
import bg.dabulgaria.tibroish.persistence.local.ProtocolsRepository
import dagger.Binds
import dagger.Module

@Module
interface LocalPersistenceModule {

    @Binds
    fun bindsIProtocolsRepository(implementation: ProtocolsRepository): IProtocolsRepository
}