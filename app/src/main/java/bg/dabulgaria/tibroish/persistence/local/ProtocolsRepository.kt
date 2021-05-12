package bg.dabulgaria.tibroish.persistence.local


import bg.dabulgaria.tibroish.domain.protocol.IProtocolsRepository
import bg.dabulgaria.tibroish.domain.protocol.Protocol
import javax.inject.Inject

class ProtocolsRepository @Inject
constructor(private val database:TiBroishDatabase ) : BaseTiBroishRepository(database), IProtocolsRepository {

    companion object {

        private val TAG = ProtocolsRepository::class.simpleName
    }

    override fun getAll(): List<Protocol> {

        return database.daoProtocol().getAll()
    }

    override fun get(id: Long): Protocol {

        return database.daoProtocol().getById( id )
    }

    override fun insert(protocol: Protocol) {

        val id = database.daoProtocol().insert( protocol )
        protocol.id = id
    }

    override fun update(protocol: Protocol) {
        database.daoProtocol().update(protocol)
    }

    override fun delete(protocol: Protocol) {
        database.daoProtocol().delete(protocol)
    }

    //    override fun updateProtocolList(protocolList: ProtocolList): Single<ProtocolList> {
//
//        for( protocol in protocolList.protocols){
//
//            var local :LocalProtocols? =null
//            try{
//                local = database.daoAccess().getProtocolById( protocol.id ?:0 )
//            }
//            catch ( ex: Exception){}
//
//            val updated = createFrom( protocol )
//            if ( local == null )
//                database.daoAccess().insertProtocol( updated )
//            else
//                database.daoAccess().updateProtocol( updated )
//        }
//
//        return Single.just( protocolList )
//    }
}