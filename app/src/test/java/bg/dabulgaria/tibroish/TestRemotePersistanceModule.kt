package bg.dabulgaria.tibroish

import bg.dabulgaria.tibroish.domain.locations.Country
import bg.dabulgaria.tibroish.domain.organisation.ITiBorishRemoteRepository
import bg.dabulgaria.tibroish.domain.organisation.Organization
import bg.dabulgaria.tibroish.domain.user.IUserRemoteRepository
import bg.dabulgaria.tibroish.domain.user.User
import bg.dabulgaria.tibroish.persistence.remote.di.RemotePersistenceModule
import bg.dabulgaria.tibroish.persistence.remote.repo.TiBroishRemoteRepository
import bg.dabulgaria.tibroish.persistence.remote.repo.UserRemoteRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class TestRemotePersistenceModule : RemotePersistenceModule {

    @Singleton
    @Provides
    override fun bindsIOrganisationRepository(implementation: TiBroishRemoteRepository): ITiBorishRemoteRepository {
        return object : ITiBorishRemoteRepository {
            override fun getOrganisations(): List<Organization> {
                TODO("Not yet implemented")
            }

            override fun getCountries(): List<Country> {
                TODO("Not yet implemented")
            }

            override fun createUser(firebaseJwt: String, userData: User) {
                TODO("Not yet implemented")
            }

        }
    }

    @Singleton
    @Provides
    override fun bindsIUserRemoteRepository(implementation: UserRemoteRepository): IUserRemoteRepository {
        return object : IUserRemoteRepository {
            override fun getUser(): User? {
                TODO("Not yet implemented")
            }

            override fun logout() {
                TODO("Not yet implemented")
            }

        }
    }

}