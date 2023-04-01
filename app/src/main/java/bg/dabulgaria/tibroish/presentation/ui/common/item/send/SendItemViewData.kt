package bg.dabulgaria.tibroish.presentation.ui.common.item.send

import bg.dabulgaria.tibroish.domain.send.ImageSendStatus
import bg.dabulgaria.tibroish.domain.locations.SectionsViewData
import bg.dabulgaria.tibroish.domain.send.SendStatus
import bg.dabulgaria.tibroish.presentation.ui.common.preview.images.PreviewImage
import bg.dabulgaria.tibroish.presentation.ui.registration.CountryCode
import java.io.Serializable

enum class SendItemListItemType {
    Header, Section, SectionManual, Message, Image, Buttons, SendSuccess, InfoText
}

class EntityItemImage(val id: Long,
                      val entityId: Long,
                      val localFilePath: String?,
                      val imageSendStatus: ImageSendStatus) : Serializable, PreviewImage{

    override val photoFilePath = localFilePath?:""
    override val photoId = id.toString()
    override val photoPreviouslySelected = false
    override val photoSelected = false
}

class EntityItem(val id: Long, var sendStatus: SendStatus = SendStatus.New) : Serializable {

    val images = mutableListOf<EntityItemImage>()
}

sealed class SendItemListItem(val type: SendItemListItemType) : Serializable

class SendItemListItemHeader(val titleText: String)
    : SendItemListItem(SendItemListItemType.Header), Serializable

class SendItemListItemSection(var sectionsViewData: SectionsViewData?)
    : SendItemListItem(SendItemListItemType.Section), Serializable

class SendItemListItemSectionManual(var sectionId: String?)
    : SendItemListItem(SendItemListItemType.SectionManual), Serializable

class SendItemListItemImage(val image: EntityItemImage)
    : SendItemListItem(SendItemListItemType.Image), Serializable

class SendItemListItemButtons(val supportsImages: Boolean)
    : SendItemListItem(SendItemListItemType. Buttons), Serializable

class SendItemListItemSendSuccess(val messageText: String)
    : SendItemListItem(SendItemListItemType.SendSuccess), Serializable

class SendItemListItemMessage(
    val labelText:String,
    val messageText: String,
    val names: String,
    val phone: String,
    val email: String,
    val countryCodes:List<CountryCode>?,
)
    : SendItemListItem(SendItemListItemType.Message), Serializable

class SendItemListItemInfoText()
    : SendItemListItem(SendItemListItemType.InfoText), Serializable


class SendItemViewData(val entityDbId: Long?=null) : Serializable {

    var entityItem: EntityItem? = null
    val items = mutableListOf<SendItemListItem>()
    var sectionsData: SectionsViewData? = null
    var message: String = ""
    var email: String = ""
    var phone: String = ""
    var names: String = ""
    var cameraPermissionRequested = false
    var imagePreviewOpen = false
    var previewImageIndex = 0
    var imagesIndexesOffset = 0
    var manualSectionId: String? = null
    var countryCodes: List<CountryCode>? = null

    constructor(source: SendItemViewData) : this(null) {

        this.entityItem = source.entityItem
        this.items.addAll(source.items.toMutableList())
        this.sectionsData = source.sectionsData
    }
}

class SendItemConstants {

    companion object {
        val VIEW_DATA_KEY = "SendItemConstants.SendItemViewData"
    }
}