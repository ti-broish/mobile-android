package bg.dabulgaria.tibroish.domain.interactors

import bg.dabulgaria.tibroish.domain.models.Comic
import bg.dabulgaria.tibroish.domain.models.ComicList
import io.reactivex.rxjava3.core.Single

interface IComicInteractor {

    fun getComicList( refresh: Boolean, limit: Long ) : Single<ComicList>

    fun getComic( refresh: Boolean, id: Long ): Single<Comic>
}