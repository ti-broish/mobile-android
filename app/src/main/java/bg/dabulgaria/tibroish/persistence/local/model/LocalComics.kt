package bg.dabulgaria.tibroish.persistence.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LocalComics
    constructor(
            @PrimaryKey
            var id: Long =0,

            var title: String = "",

            var description:String = "",

            var thumbUrl: String = ""){
}