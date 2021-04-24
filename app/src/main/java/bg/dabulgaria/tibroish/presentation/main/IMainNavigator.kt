package bg.dabulgaria.tibroish.presentation.main


interface IMainNavigator {

        fun setView(view: IMainScreenView?)

        fun showHomeScreen()

        fun showComicList()

        //fun showComicDetails(comicDetailsViewData: ComicDetailsViewData)
}
