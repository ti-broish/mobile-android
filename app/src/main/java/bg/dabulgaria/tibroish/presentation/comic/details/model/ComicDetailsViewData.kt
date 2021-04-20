package bg.dabulgaria.tibroish.presentation.comic.details.model

import bg.dabulgaria.tibroish.presentation.comic.list.model.ComicViewData
import java.io.Serializable


class ComicDetailsViewData(id :Long?,
                           title: String?,
                           thumbUlr: String?,
                           description:String? )
    : ComicViewData(id, title, thumbUlr, description ), Serializable {

    constructor( ): this(  null, null, null, null ){
    }
    constructor(comicViewData: ComicViewData ): this(comicViewData.id, comicViewData.title, comicViewData.thumbUlr,comicViewData.description )
}