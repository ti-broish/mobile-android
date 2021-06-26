package bg.dabulgaria.tibroish.presentation.ui.common.item.send

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import bg.dabulgaria.tibroish.R
import bg.dabulgaria.tibroish.presentation.providers.getSpannableStringRedWarnStar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import kotlinx.android.synthetic.main.send_item_buttons_layout.view.*
import kotlinx.android.synthetic.main.send_item_header_layout.view.*
import kotlinx.android.synthetic.main.send_item_message_layout.view.*
import kotlinx.android.synthetic.main.send_item_photo_layout.view.*
import kotlinx.android.synthetic.main.send_item_section_layout.view.*
import kotlinx.android.synthetic.main.send_item_success_layout.view.*

sealed class SendItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(position: Int,
                      item: SendItemListItem,
                      presenter: ISendItemPresenter)
}

class SendItemHeaderViewHolder(itemView: View) : SendItemViewHolder(itemView) {
    override fun bind(position: Int, item: SendItemListItem,
                      presenter: ISendItemPresenter) {

        if (item.type != SendItemListItemType.Header)
            return

        val headerItem = item as SendItemListItemHeader

        itemView.sendItemTitle?.text = headerItem.titleText
    }
}

class SendItemSectionViewHolder(itemView: View) : SendItemViewHolder(itemView) {

    override fun bind(position: Int,
                      item: SendItemListItem,
                      presenter: ISendItemPresenter) {

        if (item.type != SendItemListItemType.Section)
            return

        val sectionItem = item as SendItemListItemSection

        val data = sectionItem.sectionsViewData ?: return

        itemView.sectionPickerView.bindView(data, presenter)
    }
}

class SendItemImageViewHolder(itemView: View) : SendItemViewHolder(itemView) {
    override fun bind(position: Int,
                      item: SendItemListItem,
                      presenter: ISendItemPresenter) {

        if (item.type != SendItemListItemType.Image)
            return

        val imageItem = item as SendItemListItemImage
        Glide.with(itemView)
                .load(imageItem.image.localFilePath)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(itemView.sendItemPhotoImageView)

        itemView.sendItemPhotoDeleteView.setOnClickListener {
            presenter.onImageDeleteClick(imageItem, position)
        }
    }
}

class SendItemButtonsViewHolder(itemView: View) : SendItemViewHolder(itemView) {

    override fun bind(position: Int, item: SendItemListItem, presenter: ISendItemPresenter) {

        if (item.type != SendItemListItemType.Buttons)
            return

        val sendButtons = item as SendItemListItemButtons
        val imagesVisibility = if(sendButtons.supportsImages) View.VISIBLE else View.GONE
        itemView.sendItemGalleryBtn.visibility = imagesVisibility
        itemView.sendItemCameraBtn.visibility = imagesVisibility

        if(sendButtons.supportsImages) {
            itemView.sendItemGalleryBtn?.setOnClickListener { presenter.onAddFromGalleryClick() }
            itemView.sendItemCameraBtn?.setOnClickListener { presenter.onAddFromCameraClick() }
        }
        else {
            itemView.sendItemGalleryBtn?.setOnClickListener(null)
            itemView.sendItemCameraBtn?.setOnClickListener(null)
        }

        itemView.sendItemContinueBtn?.setOnClickListener { presenter.onSend() }
    }
}

class SendItemSendSuccessViewHolder(itemView: View) : SendItemViewHolder(itemView) {

    override fun bind(position: Int, item: SendItemListItem, presenter: ISendItemPresenter) {

        if (item.type != SendItemListItemType.SendSuccess)
            return

        val sendSuccess = item as SendItemListItemSendSuccess

        itemView.itemSendText?.text = sendSuccess.messageText
        itemView.sendItemOkButton?.setOnClickListener { presenter.onSuccessOkClick() }
    }
}

class SendItemMessageViewHolder(itemView: View) : SendItemViewHolder(itemView) {

    override fun bind(position: Int, item: SendItemListItem, presenter: ISendItemPresenter) {

        if (item.type != SendItemListItemType.Message)
            return

        val messageItem = item as SendItemListItemMessage

        itemView.messageLabelTextView.text = R.string.violation_description
                .getSpannableStringRedWarnStar(itemView.context)

        itemView.messageTextView?.setText(messageItem.messageText)

        itemView.messageTextView?.addTextChangedListener(object:TextWatcher{

            override fun afterTextChanged(s: Editable?) {
                s?.toString()?.let{ presenter.onMessageChanged(it) }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }
}