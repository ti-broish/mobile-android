package bg.dabulgaria.tibroish.domain.protocol

import bg.dabulgaria.tibroish.domain.io.IBaseTiBroishRepository

interface IProtocolsRepository : IBaseTiBroishRepository {

    fun getAll(): List<Protocol>
    fun get(id: Long): Protocol
    fun insert(protocol: Protocol)
    fun update(protocol: Protocol)
    fun delete(protocol: Protocol)
}