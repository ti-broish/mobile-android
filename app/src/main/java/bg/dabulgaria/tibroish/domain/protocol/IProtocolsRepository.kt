package bg.dabulgaria.tibroish.domain.protocol

import bg.dabulgaria.tibroish.domain.io.IBaseTransactionalRepository

interface IProtocolsRepository : IBaseTransactionalRepository {

    fun getAll(): List<Protocol>
    fun get(id: Long): Protocol
    fun insert(protocol: Protocol)
    fun update(protocol: Protocol)
    fun delete(protocol: Protocol)
}