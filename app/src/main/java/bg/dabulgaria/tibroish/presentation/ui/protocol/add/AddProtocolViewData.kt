package bg.dabulgaria.tibroish.presentation.ui.protocol.add

import bg.dabulgaria.tibroish.domain.protocol.ProtocolExt
import bg.dabulgaria.tibroish.domain.protocol.image.ProtocolImage
import bg.dabulgaria.tibroish.presentation.ui.common.sectionpicker.SectionsViewData
import java.io.Serializable

enum class AddProtocolListItemType{
    Header, Section, Image, Buttons
}

sealed class AddProtocolListItem(val type: AddProtocolListItemType) :Serializable

class AddProtocolListItemHeader
    : AddProtocolListItem(AddProtocolListItemType.Header),Serializable

class AddProtocolListItemSection (var sectionsViewData: SectionsViewData?)
    : AddProtocolListItem(AddProtocolListItemType.Section),Serializable

class AddProtocolListItemImage(val image: ProtocolImage)
    : AddProtocolListItem(AddProtocolListItemType.Image),Serializable

class AddProtocolListItemButtons
    : AddProtocolListItem(AddProtocolListItemType.Buttons),Serializable


class AddProtocolViewData() : Serializable {

    var protocolId :Long? = null
    var protocol :ProtocolExt? =null
    val items = mutableListOf<AddProtocolListItem>()

    var sectionsData: SectionsViewData?=null
    var imageForCameraPath :String = ""
}

class AddProtocolConstants{

    companion object {
        val VIEW_DATA_KEY = "AddProtocolConstants.AddProtocolViewData"
    }
}