package bg.dabulgaria.tibroish.persistence.remote


import bg.dabulgaria.tibroish.BuildConfig
import bg.dabulgaria.tibroish.persistence.remote.model.ComicRemote
import bg.dabulgaria.tibroish.domain.calculators.IHashCalculator
import bg.dabulgaria.tibroish.domain.models.Comic
import bg.dabulgaria.tibroish.domain.providers.ITimestampProvider
import bg.dabulgaria.tibroish.domain.repositories.remote.IMarvelsRemoteRepository
import io.reactivex.rxjava3.core.Single

import javax.inject.Inject


class MarvelsRemoteRepository
@Inject
constructor(private var apiController : MarvelsApiController,
            private var hashCalculator: IHashCalculator,
            private val timestampProvider: ITimestampProvider) : IMarvelsRemoteRepository {

    private val privateKey = BuildConfig.PRIVATE_KEY
    private val publicKey = BuildConfig.PUBLIC_KEY

    override fun getComicsList(limit: Long ) : Single<List<Comic>> {

        return Single.just( emptyList())
    }

    override fun getComicDetails(comicId : Long) : Single<Comic> {

        val timeStamp = timestampProvider.getTimeStamp()
        val hash = calculateHash(timeStamp)
        return apiController.getComicDetails(comicId, timeStamp, publicKey, hash )
                .map{

                    comicDataResponse -> createFrom( comicDataResponse.data?.results?.get( 0 ) ?: ComicRemote() )
                }
    }

    private fun calculateHash(timeStamp: String): String {

        return hashCalculator.calculate(timeStamp, privateKey, publicKey)
    }

    private fun createFrom( comicRemote: ComicRemote ) : Comic {

        val comic = Comic()
        comic.id = comicRemote.id
        comic.title = comicRemote.title
        comic.description = comicRemote.description
        comic.thumbnailUrl = comicRemote.thumbnail?.url

        return  comic
    }
}