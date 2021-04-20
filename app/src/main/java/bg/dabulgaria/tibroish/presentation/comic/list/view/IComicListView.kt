package bg.dabulgaria.tibroish.presentation.comic.list.view

import bg.dabulgaria.tibroish.domain.Locations.LocationsS
import bg.dabulgaria.tibroish.presentation.base.IBaseView


interface IComicListView :IBaseView {

    fun onLoadingStateChange(isLoading : Boolean );

    fun onComicsLoaded(locations : LocationsS )

    fun onError( errorMessage: String)
}
