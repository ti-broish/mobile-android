package bg.dabulgaria.tibroish.domain.repositories.local

import bg.dabulgaria.tibroish.domain.models.Comic
import bg.dabulgaria.tibroish.domain.models.ComicList
import io.reactivex.rxjava3.core.Single


interface IComicsLocalRepository {

    fun getComicList() : Single<ComicList>
    fun updateComicList(comicList : ComicList) : Single<ComicList>

    fun getComic( id: Long ) : Single<Comic>
    fun updateComic(comic : Comic) : Single<Comic>
}