package bg.dabulgaria.tibroish.persistence.local

import androidx.room.*
import bg.dabulgaria.tibroish.domain.protocol.Protocol
import bg.dabulgaria.tibroish.domain.protocol.image.ProtocolImage

@Dao
interface DaoProtocolImage {

    @Insert
    fun insert(protocol: ProtocolImage)

    @Query("SELECT * FROM ProtocolImage WHERE id = :imageId")
    fun getById(imageId: Long): ProtocolImage

    @Query("SELECT * FROM Protocol WHERE protocolId = :_protocolId ORDER BY id asc")
    fun getAll(_protocolId:Long): List<ProtocolImage>

    @Update
    fun update(comics: Protocol)

    @Delete
    fun delete(comics: Protocol)
}