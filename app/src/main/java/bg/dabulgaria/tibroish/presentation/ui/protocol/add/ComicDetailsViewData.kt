//package bg.dabulgaria.tibroish.presentation.ui.protocol.add
//
//import bg.dabulgaria.tibroish.presentation.ui.protocol.list.ComicViewData
//import java.io.Serializable
//
//
//class ComicDetailsViewData(id :Long?,
//                           title: String?,
//                           thumbUlr: String?,
//                           description:String? )
//    : ComicViewData(id, title, thumbUlr, description ), Serializable {
//
//    constructor( ): this(  null, null, null, null ){
//    }
//    constructor(comicViewData: ComicViewData): this(comicViewData.id, comicViewData.title, comicViewData.thumbUlr,comicViewData.description )
//}