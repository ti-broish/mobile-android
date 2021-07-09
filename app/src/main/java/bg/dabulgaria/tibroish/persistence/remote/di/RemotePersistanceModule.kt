package bg.dabulgaria.tibroish.persistence.remote.di

import bg.dabulgaria.tibroish.domain.organisation.ITiBroishRemoteRepository
import bg.dabulgaria.tibroish.domain.user.IUserRemoteRepository
import bg.dabulgaria.tibroish.persistence.remote.repo.TiBroishRemoteRepository
import bg.dabulgaria.tibroish.persistence.remote.repo.UserRemoteRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface RemotePersistenceModule {

    @Binds
    fun bindsIOrganisationRepository(implementation: TiBroishRemoteRepository):ITiBroishRemoteRepository

    @Binds
    @Singleton
    fun bindsIUserRemoteRepository(implementation: UserRemoteRepository):IUserRemoteRepository
}