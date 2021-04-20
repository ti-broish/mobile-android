package bg.dabulgaria.tibroish.domain.repositories.remote

import bg.dabulgaria.tibroish.domain.models.Comic
import io.reactivex.rxjava3.core.Single

interface IMarvelsRemoteRepository {

    fun getComicsList(limit : Long ): Single<List<Comic>>

    fun getComicDetails(comicId: Long): Single<Comic>
}
