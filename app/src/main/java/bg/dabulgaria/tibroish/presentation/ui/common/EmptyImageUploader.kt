package bg.dabulgaria.tibroish.presentation.ui.common

import bg.dabulgaria.tibroish.domain.image.IEntityImageUploader

class EmptyImageUploader constructor() :IEntityImageUploader{

    override fun uploadImages(entityId: Long) {}
}