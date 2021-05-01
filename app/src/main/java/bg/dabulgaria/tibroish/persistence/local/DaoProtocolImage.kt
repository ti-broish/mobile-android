package bg.dabulgaria.tibroish.persistence.local

import androidx.room.*
import bg.dabulgaria.tibroish.domain.protocol.Protocol
import bg.dabulgaria.tibroish.domain.protocol.image.ProtocolImage

@Dao
interface DaoProtocolImage {

    @Insert
    fun insert(protocol: ProtocolImage):Long

    @Query("SELECT * FROM ProtocolImage WHERE id = :imageId")
    fun getById(imageId: Long): ProtocolImage

    @Query("SELECT * FROM ProtocolImage WHERE protocolId = :_protocolId ORDER BY id asc")
    fun getByProtocolId(_protocolId:Long): List<ProtocolImage>

    @Query("SELECT * FROM ProtocolImage ORDER BY id asc")
    fun getAll(): List<ProtocolImage>

    @Update
    fun update(image: ProtocolImage)

    @Delete
    fun delete(image: ProtocolImage)
}