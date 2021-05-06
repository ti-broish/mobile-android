package bg.dabulgaria.tibroish.domain.protocol

import javax.inject.Inject

interface IProtocolCreator {

}


class ProtocolCreator @Inject constructor(private val protocolsRepository: IProtocolsRepository){


}