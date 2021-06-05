package bg.dabulgaria.tibroish.presentation.ui.common.item.send

import android.text.SpannableString
import bg.dabulgaria.tibroish.domain.send.ImageSendStatus
import bg.dabulgaria.tibroish.presentation.ui.common.sectionpicker.SectionsViewData
import java.io.Serializable

enum class SendItemListItemType {
    Header, Section, Message, Image, Buttons, SendSuccess
}

class EntityItemImage(val id: Long,
                      val entityId: Long,
                      val localFilePath: String?,
                      val imageSendStatus: ImageSendStatus) : Serializable

class EntityItem(val id: Long) : Serializable {

    val images = mutableListOf<EntityItemImage>()
}

sealed class SendItemListItem(val type: SendItemListItemType) : Serializable

class SendItemListItemHeader(val titleText: String)
    : SendItemListItem(SendItemListItemType.Header), Serializable

class SendItemListItemSection(var sectionsViewData: SectionsViewData?)
    : SendItemListItem(SendItemListItemType.Section), Serializable

class SendItemListItemImage(val image: EntityItemImage)
    : SendItemListItem(SendItemListItemType.Image), Serializable

class SendItemListItemButtons
    : SendItemListItem(SendItemListItemType.Buttons), Serializable

class SendItemListItemSendSuccess(val messageText: String)
    : SendItemListItem(SendItemListItemType.SendSuccess), Serializable

class SendItemListItemMessage(val labelText:String, val messageText: String)
    : SendItemListItem(SendItemListItemType.Message), Serializable


class SendItemViewData() : Serializable {

    var entityItem: EntityItem? = null
    val items = mutableListOf<SendItemListItem>()
    var sectionsData: SectionsViewData? = null
    var imageForCameraPath: String = ""
    var message: String = ""

    constructor(source: SendItemViewData) : this() {

        this.entityItem = source.entityItem
        this.items.addAll(source.items)
        this.sectionsData = source.sectionsData
        this.imageForCameraPath = source.imageForCameraPath
    }
}

class SendItemConstants {

    companion object {
        val VIEW_DATA_KEY = "SendItemConstants.SendItemViewData"
    }
}