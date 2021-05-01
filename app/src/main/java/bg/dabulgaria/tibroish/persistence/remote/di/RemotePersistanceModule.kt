package bg.dabulgaria.tibroish.persistence.remote.di

import bg.dabulgaria.tibroish.domain.organisation.IOrganisationRepository
import bg.dabulgaria.tibroish.persistence.remote.repo.OrganisationRepository
import dagger.Binds
import dagger.Module

@Module
interface RemotePersistenceModule {

    @Binds
    fun bindsIOrganisationRepository(implementation: OrganisationRepository):IOrganisationRepository
}