package bg.dabulgaria.tibroish.presentation.ui.common.preview.images


interface PreviewImage {

    val photoId: String
    val photoFilePath: String
    val photoSelected: Boolean
    val photoPreviouslySelected: Boolean
}