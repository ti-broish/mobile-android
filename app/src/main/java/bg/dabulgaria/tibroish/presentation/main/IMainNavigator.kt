package bg.dabulgaria.tibroish.presentation.main

import bg.dabulgaria.tibroish.presentation.comic.details.model.ComicDetailsViewData

interface IMainNavigator {

        fun showHomeScreen()

        fun showComicList()

        fun showComicDetails(comicDetailsViewData: ComicDetailsViewData)
}