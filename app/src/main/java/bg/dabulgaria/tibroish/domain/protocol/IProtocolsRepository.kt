package bg.dabulgaria.tibroish.domain.protocol

interface IProtocolsRepository {

    fun getAll(): List<Protocol>
    fun get(id: Long): Protocol
    fun insert(protocol: Protocol):Protocol
    fun update(protocol: Protocol):Protocol
    fun delete(protocol: Protocol)
}