package bg.dabulgaria.tibroish.persistence.local.db

import androidx.room.*
import bg.dabulgaria.tibroish.domain.protocol.Protocol
import bg.dabulgaria.tibroish.domain.protocol.image.ProtocolImage

@Dao
interface DaoProtocolImage {

    @Insert
    fun insert(protocol: ProtocolImage):Long

    @Query("SELECT * FROM ProtocolImage WHERE id = :imageId")
    fun getById(imageId: Long): ProtocolImage?

    @Query("SELECT * FROM ProtocolImage WHERE protocolId = :protocolId ORDER BY id asc")
    fun getByProtocolId(protocolId:Long): List<ProtocolImage>

    @Query("SELECT * FROM ProtocolImage ORDER BY id asc")
    fun getAll(): List<ProtocolImage>

    @Update
    fun update(image: ProtocolImage)

    @Delete
    fun delete(image: ProtocolImage)

    @Query("DELETE FROM ProtocolImage where id= :imageId")
    fun delete(imageId: Long)
}