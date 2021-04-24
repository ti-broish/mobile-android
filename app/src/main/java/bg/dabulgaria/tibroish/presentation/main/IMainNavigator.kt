package bg.dabulgaria.tibroish.presentation.main

import bg.dabulgaria.tibroish.presentation.navigation.OnMenuClickListener

interface IMainNavigator: OnMenuClickListener {

        fun setView(view: IMainScreenView?)

        fun showHomeScreen()

        //fun showComicList()

        //fun showComicDetails(comicDetailsViewData: ComicDetailsViewData)
}
