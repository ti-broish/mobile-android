package bg.dabulgaria.tibroish.persistence.remote.di

import bg.dabulgaria.tibroish.domain.organisation.IOrganisationRepository
import bg.dabulgaria.tibroish.domain.user.IUserRemoteRepository
import bg.dabulgaria.tibroish.persistence.remote.repo.OrganisationRepository
import bg.dabulgaria.tibroish.persistence.remote.repo.UserRemoteRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface RemotePersistenceModule {

    @Binds
    fun bindsIOrganisationRepository(implementation: OrganisationRepository):IOrganisationRepository

    @Binds
    @Singleton
    fun bindsIUserRemoteRepository(implementation: UserRemoteRepository):IUserRemoteRepository
}