package bg.dabulgaria.tibroish.presentation.ui.protocol.add

import android.os.Bundle
import android.util.Log
import bg.dabulgaria.tibroish.R
import bg.dabulgaria.tibroish.domain.protocol.IProtocolsRepository
import bg.dabulgaria.tibroish.domain.protocol.Protocol
import bg.dabulgaria.tibroish.domain.protocol.ProtocolExt
import bg.dabulgaria.tibroish.infrastructure.permission.AppPermission
import bg.dabulgaria.tibroish.infrastructure.permission.IPermissionRequester
import bg.dabulgaria.tibroish.infrastructure.permission.PermissionCodes
import bg.dabulgaria.tibroish.presentation.base.BasePresenter
import bg.dabulgaria.tibroish.presentation.base.IBasePresenter
import bg.dabulgaria.tibroish.presentation.base.IDisposableHandler
import bg.dabulgaria.tibroish.presentation.providers.INetworkInfoProvider
import bg.dabulgaria.tibroish.presentation.providers.IResourceProvider
import bg.dabulgaria.tibroish.infrastructure.schedulers.ISchedulersProvider
import bg.dabulgaria.tibroish.persistence.local.ProtocolsRepository
import bg.dabulgaria.tibroish.presentation.main.IMainNavigator
import bg.dabulgaria.tibroish.presentation.main.IPermissionResponseListener
import java.util.*


import javax.inject.Inject

interface IAddProtocolInteractor {

    fun addNew(protocol: Protocol): ProtocolExt
}

class AddProtocolInteractor @Inject constructor(private val protocolsRepository: IProtocolsRepository)
    : IAddProtocolInteractor{

    override fun addNew(protocol: Protocol): ProtocolExt {

        protocol.uuid = UUID.randomUUID().toString()
        protocolsRepository.insert(protocol)
        return ProtocolExt( protocol )
    }

    companion object {

        private val TAG = AddProtocolInteractor::class.simpleName
    }
}
