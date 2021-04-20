package bg.dabulgaria.tibroish.persistence.local

import androidx.room.*
import bg.dabulgaria.tibroish.persistence.local.model.LocalComics

@Dao
public interface DaoAccess {

    @Insert
    fun insertComic(comics: LocalComics )

    @Query("SELECT * FROM LocalComics WHERE id = :comicId")
    fun getComicById( comicId: Long ) :LocalComics

    @Query("SELECT * FROM LocalComics ORDER BY id desc")
    fun getAllComics() : List<LocalComics>

    @Update
    fun updateComic( comics: LocalComics )

    @Delete
    fun deleteComic( comics: LocalComics )
}