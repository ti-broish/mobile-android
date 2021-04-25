//package bg.dabulgaria.tibroish.persistence.local
//
//import bg.dabulgaria.tibroish.domain.models.Comic
//import bg.dabulgaria.tibroish.domain.models.ComicList
//import bg.dabulgaria.tibroish.domain.repositories.local.IComicsLocalRepository
//import bg.dabulgaria.tibroish.persistence.local.model.LocalComics
//import io.reactivex.rxjava3.core.Single
//import javax.inject.Inject
//
//class ComicsLocalRepository @Inject
//constructor(private val database:MarvelsDatabase ) : IComicsLocalRepository {
//
//    companion object {
//
//        private val TAG = ComicsLocalRepository::class.simpleName
//    }
//
//    override fun getComicList(): Single<ComicList> {
//
//        return Single.just( createFrom(database.daoAccess().getAllComics()  ) )
//    }
//
//    override fun updateComicList(comicList: ComicList): Single<ComicList> {
//
//        for( comic in comicList.comics){
//
//            var local :LocalComics? =null
//            try{
//                local = database.daoAccess().getComicById( comic.id ?:0 )
//            }
//            catch ( ex: Exception){}
//
//            val updated = createFrom( comic )
//            if ( local == null )
//                database.daoAccess().insertComic( updated )
//            else
//                database.daoAccess().updateComic( updated )
//        }
//
//        return Single.just( comicList )
//    }
//
//    override fun getComic(id: Long): Single<Comic> {
//
//        return Single.just( createFrom( database.daoAccess().getComicById( id )) )
//    }
//
//    override fun updateComic(comic: Comic): Single<Comic> {
//        return Single.just( comic )
//    }
//
//    private fun createFrom(localComics: List<LocalComics>) : ComicList{
//
//        val comicList = ComicList()
//        for( localComic in localComics )
//            comicList.comics.add( createFrom( localComic ) )
//        return  comicList
//    }
//
//    private fun createFrom(localComics: LocalComics) : Comic {
//
//        val comic = Comic()
//        comic.id = localComics.id
//        comic.title = localComics.title
//        comic.description = localComics.description
//        comic.thumbnailUrl = localComics.thumbUrl
//
//        return  comic
//    }
//
//    private fun createFrom(comic: Comic) :LocalComics  {
//
//        val localComic = LocalComics()
//        localComic.id = comic.id ?:0L
//        localComic.title = comic.title ?:""
//        localComic.description = comic.description ?:""
//        localComic.thumbUrl = comic.thumbnailUrl?:""
//
//        return  localComic
//    }
//}