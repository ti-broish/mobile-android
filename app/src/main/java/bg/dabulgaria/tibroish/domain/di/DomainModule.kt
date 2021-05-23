package bg.dabulgaria.tibroish.domain.di

import bg.dabulgaria.tibroish.domain.auth.IRemoteRepoAuthenticator
import bg.dabulgaria.tibroish.domain.auth.RemoteRepoAuthenticator
import bg.dabulgaria.tibroish.domain.user.IUserAuthenticator
import bg.dabulgaria.tibroish.domain.user.IUserTypeAdapter
import bg.dabulgaria.tibroish.domain.user.UserAuthenticator
import bg.dabulgaria.tibroish.domain.user.UserTypeAdapter
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface DomainModule {

    @Binds
    fun bindIUserTypeAdapter(implementation: UserTypeAdapter): IUserTypeAdapter

    @Binds
    fun bindIUserAuthenticator(implementation: UserAuthenticator): IUserAuthenticator

    @Binds
    @Singleton
    fun bindIRemoteRepoAuthenticator(implementation: RemoteRepoAuthenticator): IRemoteRepoAuthenticator

}