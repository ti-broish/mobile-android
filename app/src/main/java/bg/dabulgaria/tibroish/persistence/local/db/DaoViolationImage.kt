package bg.dabulgaria.tibroish.persistence.local.db

import androidx.room.*
import bg.dabulgaria.tibroish.domain.violation.image.ViolationImage

@Dao
interface DaoViolationImage {

    @Insert
    fun insert(protocol: ViolationImage):Long

    @Query("SELECT * FROM ViolationImage WHERE id = :imageId")
    fun getById(imageId: Long): ViolationImage?

    @Query("SELECT * FROM ViolationImage WHERE violationId = :violationId ORDER BY id asc")
    fun getByViolationId(violationId:Long): List<ViolationImage>

    @Query("SELECT * FROM ViolationImage ORDER BY id asc")
    fun getAll(): List<ViolationImage>

    @Update
    fun update(image: ViolationImage)

    @Delete
    fun delete(image: ViolationImage)

    @Query("DELETE FROM ViolationImage where id= :imageId")
    fun delete(imageId: Long)
}