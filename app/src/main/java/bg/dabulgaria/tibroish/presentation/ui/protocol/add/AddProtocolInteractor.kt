package bg.dabulgaria.tibroish.presentation.ui.protocol.add

import bg.dabulgaria.tibroish.domain.protocol.IProtocolsRepository
import bg.dabulgaria.tibroish.domain.protocol.Protocol
import bg.dabulgaria.tibroish.domain.protocol.ProtocolExt
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
