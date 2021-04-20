package bg.dabulgaria.tibroish.presentation.ui.protocol.list

import java.io.Serializable

open class ComicViewData(val id :Long?,
                         val title: String?,
                         val thumbUlr: String?,
                         val description: String? ): Serializable {
}
