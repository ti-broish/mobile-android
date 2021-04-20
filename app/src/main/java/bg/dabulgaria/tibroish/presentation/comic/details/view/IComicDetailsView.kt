package bg.dabulgaria.tibroish.presentation.comic.details.view

import bg.dabulgaria.tibroish.presentation.base.IBaseView
import bg.dabulgaria.tibroish.presentation.comic.details.model.ComicDetailsViewData

interface IComicDetailsView :IBaseView{

    fun onDetailsLoaded(comicDetailsViewData: ComicDetailsViewData )

    fun onLoadingStateChange( isLoading : Boolean )

    fun onError( errorMessage: String)
}