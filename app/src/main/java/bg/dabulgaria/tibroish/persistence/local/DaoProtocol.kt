package bg.dabulgaria.tibroish.persistence.local

import androidx.room.*
import bg.dabulgaria.tibroish.domain.protocol.Protocol
import bg.dabulgaria.tibroish.persistence.local.model.LocalComics

@Dao
interface DaoProtocol {

    @Insert
    fun insert(protocol: Protocol)

    @Query("SELECT * FROM Protocol WHERE id = :protocolId")
    fun getById(protocolId: Long): Protocol

    @Query("SELECT * FROM Protocol ORDER BY id desc")
    fun getAll(): List<Protocol>

    @Update
    fun update(comics: Protocol)

    @Delete
    fun delete(comics: Protocol)
}