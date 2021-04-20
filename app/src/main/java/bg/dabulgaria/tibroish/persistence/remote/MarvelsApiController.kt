package bg.dabulgaria.tibroish.persistence.remote

import bg.dabulgaria.tibroish.persistence.remote.model.ComicDataResponse
import io.reactivex.rxjava3.core.Single

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MarvelsApiController {

    @GET("comics ")
    fun getComics( @Query("ts")  timeStamp:String,
                   @Query("apikey") apiKey: String ,
                   @Query("hash") hash:String,
                   @Query("limit") limit:Long ): Single<ComicDataResponse>

    @GET("comics/{comicId}")
    fun getComicDetails(@Path("comicId") comicId: Long,
                          @Query("ts") timeStamp: String,
                          @Query("apikey") apiKey: String,
                          @Query("hash") hash: String): Single<ComicDataResponse>
}
