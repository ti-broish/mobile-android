package bg.dabulgaria.tibroish.domain.interactors

import bg.dabulgaria.tibroish.domain.models.Comic
import bg.dabulgaria.tibroish.domain.models.ComicList
import bg.dabulgaria.tibroish.domain.repositories.remote.IMarvelsRemoteRepository
import bg.dabulgaria.tibroish.domain.repositories.local.IComicsLocalRepository
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.functions.Predicate

import javax.inject.Inject

open class ComicInteractor
@Inject constructor(val marvelsRemoteRepository : IMarvelsRemoteRepository,
                    val comicsLocalRepository : IComicsLocalRepository) : IComicInteractor{


    override fun getComicList( refresh: Boolean, limit : Long  ) : Single<ComicList> {

        return loadComicList( refresh, limit )
    }

    override fun getComic(refresh: Boolean, id : Long) : Single<Comic> {

        if( refresh )
            return getComicDetailFromServer( id )

        return comicsLocalRepository.getComic( id)
                .concatWith( getComicDetailFromServer( id ) )
                .filter( Predicate<Comic?> { comic-> comic?.id != 0L
                }).first( Comic() )
    }


    //region private methods
    private fun loadComicList( refresh: Boolean, limit: Long ) : Single<ComicList> {

        if( refresh )
            return getComicListFromServer( limit )

        return comicsLocalRepository.getComicList()
                .flatMap {
                    if( it.comics.isEmpty() == false )
                        Single.just( it )
                    else
                        getComicListFromServer(limit )
                }
    }

    private fun getComicListFromServer( limit : Long ) : Single<ComicList> {

        return Single.just( ComicList.empty() )
//        return marvelsRemoteRepository.getComicsList( limit )
//                .flatMap(object : Function<List<Comic>, SingleSource<ComicList>> {
//                    @Throws(Exception::class)
//                    override fun apply( comicList : List<Comic>) : SingleSource<ComicList> {
//
//                        val list = ComicList()
//                        list.comics.addAll( comicList )
//
//                        return comicsLocalRepository.updateComicList( list )
//                    }
//                })
    }


    private fun getComicDetailFromServer( id : Long ) : Single<Comic> {

        return Single.just( Comic() )
//        return marvelsRemoteRepositor.getComicDetails( id )
//                .flatMap(object : Function<Comic, SingleSource<Comic>> {
//                    @Throws(Exception::class)
//                    override fun apply( comic : Comic) : SingleSource<Comic> {
//
//                        return comicsLocalRepository.updateComic( comic )
//                    }
//                })
    }
}