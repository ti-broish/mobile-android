package bg.dabulgaria.tibroish.presentation.ui.protocol.add

import android.view.View
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView
import bg.dabulgaria.tibroish.R
import bg.dabulgaria.tibroish.domain.protocol.image.UploadStatus
import bg.dabulgaria.tibroish.presentation.ui.common.sectionpicker.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import kotlinx.android.synthetic.main.add_protocol_buttons_item.view.*
import kotlinx.android.synthetic.main.add_protocol_photo_item.view.*
import kotlinx.android.synthetic.main.add_protocol_section_item.view.*
import kotlinx.android.synthetic.main.add_protocol_send_success_item.view.*

sealed class AddProtocolViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(position: Int,
                      item: AddProtocolListItem,
                      presenter: IAddProtocolPresenter)
}

class AddProtocolHeaderViewHolder(itemView: View) : AddProtocolViewHolder(itemView) {
    override fun bind(position: Int, item: AddProtocolListItem,
                      presenter: IAddProtocolPresenter) {}
}

class AddProtocolSectionViewHolder(itemView: View) : AddProtocolViewHolder(itemView) {

    override fun bind(position: Int,
                      item: AddProtocolListItem,
                      presenter: IAddProtocolPresenter) {

        if (item.type != AddProtocolListItemType.Section)
            return

        val sectionItem = item as AddProtocolListItemSection

        val data = sectionItem.sectionsViewData ?: return

        itemView.sectionPickerView.bindView(data, presenter)
    }
}

class AddProtocolImageViewHolder(itemView: View) : AddProtocolViewHolder(itemView) {
    override fun bind(position: Int,
                      item: AddProtocolListItem,
                      presenter: IAddProtocolPresenter) {

        if( item.type != AddProtocolListItemType.Image )
            return

        val imageItem = item as AddProtocolListItemImage
        Glide.with(itemView)
                .load(imageItem.image.localFilePath)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(itemView.protocolPhotoImageView)

        if( imageItem.image.uploadStatus != UploadStatus.Uploaded)
            itemView.protocolPhotoDeleteImageView.setOnClickListener {
            presenter.onImageDeleteClick(imageItem, position) }
        else
            itemView.protocolPhotoDeleteImageView.setOnClickListener(null)
    }
}

class AddProtocolButtonsViewHolder(itemView: View) : AddProtocolViewHolder(itemView) {

    override fun bind(position: Int, item: AddProtocolListItem, presenter: IAddProtocolPresenter) {

        itemView.addProtocolGalleryBtn?.setOnClickListener { presenter.onAddFromGalleryClick() }
        itemView.addProtocolCameraBtn?.setOnClickListener { presenter.onAddFromCameraClick() }
        itemView.addProtocolContinueBtn?.setOnClickListener { presenter.onContinue() }
    }
}

class AddProtocolSendSuccessViewHolder(itemView: View) : AddProtocolViewHolder(itemView) {

    override fun bind(position: Int, item: AddProtocolListItem, presenter: IAddProtocolPresenter) {

        itemView.addProtocolSendOkBtn?.setOnClickListener { presenter.onSuccessOkClick() }
    }
}