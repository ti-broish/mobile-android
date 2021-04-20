package bg.dabulgaria.tibroish.presentation.comic.details.presenter

import bg.dabulgaria.tibroish.presentation.base.IBasePresenter
import bg.dabulgaria.tibroish.presentation.comic.details.view.IComicDetailsView

interface IComicDetailsPresenter: IBasePresenter<IComicDetailsView> {

    fun loadComicDetails(refresh: Boolean, id:Long )


    fun onViewDisposed()
}