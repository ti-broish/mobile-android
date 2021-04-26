package bg.dabulgaria.tibroish.persistence.local

import androidx.room.*
import bg.dabulgaria.tibroish.domain.protocol.Protocol
import bg.dabulgaria.tibroish.persistence.local.model.LocalComics

@Dao
public interface DaoProtocol {

    @Insert
    fun insert(protocol: Protocol):Protocol

    @Query("SELECT * FROM LocalComics WHERE id = :comicId")
    fun getById(comicId: Long): Protocol

    @Query("SELECT * FROM Protocol ORDER BY id desc")
    fun getAll(): List<Protocol>

    @Update
    fun update(comics: Protocol): Protocol

    @Delete
    fun delete(comics: Protocol)
}