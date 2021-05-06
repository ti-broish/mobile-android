package bg.dabulgaria.tibroish.domain.protocol

import javax.inject.Inject

interface IProtocolReader {


}

class ProtocolReader @Inject constructor( private val protocolsRepository: IProtocolsRepository){


}