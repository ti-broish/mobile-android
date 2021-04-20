package bg.dabulgaria.tibroish.presentation.comic.list.presenter

import bg.dabulgaria.tibroish.presentation.base.IBasePresenter
import bg.dabulgaria.tibroish.presentation.comic.details.model.ComicDetailsViewData
import bg.dabulgaria.tibroish.presentation.comic.list.view.IComicListView

interface IComicListPresenter : IBasePresenter<IComicListView> {

    fun onComicClick( viewData : ComicDetailsViewData)

    fun loadComics(refresh: Boolean )
}
