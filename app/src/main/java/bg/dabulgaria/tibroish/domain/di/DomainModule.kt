package bg.dabulgaria.tibroish.domain.di

import bg.dabulgaria.tibroish.domain.auth.IRemoteRepoAuthenticator
import bg.dabulgaria.tibroish.domain.auth.RemoteRepoAuthenticator
import bg.dabulgaria.tibroish.domain.protocol.image.IProtocolImageUploader
import bg.dabulgaria.tibroish.domain.protocol.image.ProtocolImageUploader
import bg.dabulgaria.tibroish.domain.user.IUserAuthenticator
import bg.dabulgaria.tibroish.domain.user.IUserTypeAdapter
import bg.dabulgaria.tibroish.domain.user.UserAuthenticator
import bg.dabulgaria.tibroish.domain.user.UserTypeAdapter
import bg.dabulgaria.tibroish.domain.violation.image.ViolationImageUploader
import dagger.Binds
import dagger.Module
import javax.inject.Singleton
import bg.dabulgaria.tibroish.domain.violation.image.IViolationImageUploader as IViolationImageUploader

@Module
interface DomainModule {

    @Binds
    fun bindIUserTypeAdapter(implementation: UserTypeAdapter): IUserTypeAdapter

    @Binds
    fun bindIUserAuthenticator(implementation: UserAuthenticator): IUserAuthenticator

    @Binds
    @Singleton
    fun bindIRemoteRepoAuthenticator(implementation: RemoteRepoAuthenticator): IRemoteRepoAuthenticator

    @Binds
    fun bindProtocolImageUploader(implementation: ProtocolImageUploader): IProtocolImageUploader

    @Binds
    fun bindIViolationImageUploader(implementedError: ViolationImageUploader): IViolationImageUploader
}