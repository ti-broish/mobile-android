package bg.dabulgaria.tibroish.persistence.local.db

import androidx.room.*
import bg.dabulgaria.tibroish.domain.protocol.Protocol

@Dao
interface DaoProtocol {

    @Insert
    fun insert(protocol: Protocol):Long

    @Query("SELECT * FROM Protocol WHERE id = :protocolId")
    fun getById(protocolId: Long): Protocol

    @Query("SELECT * FROM Protocol ORDER BY id desc")
    fun getAll(): List<Protocol>

    @Update
    fun update(protocol: Protocol)

    @Delete
    fun delete(protocol: Protocol)
}