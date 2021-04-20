package bg.dabulgaria.tibroish.presentation.main

import android.hardware.input.InputManager
import bg.dabulgaria.tibroish.presentation.ui.protocol.add.ComicDetailsViewData

interface IMainNavigator {

        fun setView(view: IMainScreenView?)

        fun showHomeScreen()

        fun showComicList()

        fun showComicDetails(comicDetailsViewData: ComicDetailsViewData)
}
