package bg.dabulgaria.tibroish.presentation.ui.live.sectionpick

import bg.dabulgaria.tibroish.domain.locations.SectionsViewData
import java.io.Serializable

class LiveSectionPickViewData(): Serializable {

    var sectionsData: SectionsViewData? = null
}


object LiveSectionPickConstants{

    val KEY = "LiveSectionPickConstants.data"
}