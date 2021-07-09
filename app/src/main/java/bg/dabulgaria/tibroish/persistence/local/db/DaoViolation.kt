package bg.dabulgaria.tibroish.persistence.local.db

import androidx.room.*
import bg.dabulgaria.tibroish.domain.violation.VoteViolation

@Dao
interface DaoViolation {

    @Insert
    fun insert(voteViolation: VoteViolation):Long

    @Query("SELECT * FROM VoteViolation WHERE id = :violationId")
    fun getById(violationId: Long): VoteViolation?

    @Query("SELECT * FROM VoteViolation ORDER BY id desc")
    fun getAll(): List<VoteViolation>

    @Update
    fun update(voteViolation: VoteViolation)

    @Delete
    fun delete(voteViolation: VoteViolation)
}