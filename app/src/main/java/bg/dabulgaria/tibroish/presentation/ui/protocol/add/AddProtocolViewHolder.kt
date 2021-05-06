package bg.dabulgaria.tibroish.presentation.ui.protocol.add

import android.content.Context
import android.os.Build
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import bg.dabulgaria.tibroish.R
import bg.dabulgaria.tibroish.domain.protocol.image.UploadStatus
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import kotlinx.android.synthetic.main.add_protocol_buttons_item.view.*
import kotlinx.android.synthetic.main.add_protocol_photo_item.view.*
import kotlinx.android.synthetic.main.add_protocol_section_item.view.*

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

        val context = itemView.context

        itemView.sectionInText.text = getSpannableStringRedWarnStar(context, R.string.section_in)

        itemView.mirLabelTextView.text = getSpannableStringRedWarnStar(context, R.string.mir)
        itemView.municLabelTextView.text = getSpannableStringRedWarnStar(context, R.string.municipality)
        itemView.townLabelTextView.text = getSpannableStringRedWarnStar(context, R.string.town)

        itemView.regionLabelTextView.text = getSpannableStringRedWarnStar(context, R.string.region)
        itemView.sectionLabelTextView.text = getSpannableStringRedWarnStar(context, R.string.section_number)

        itemView.uniqueSectionLabelTextView.text = getSpannableStringRedWarnStar(context, R.string.unique_section_number)
    }

    private fun getSpannableStringRedWarnStar( context:Context, @StringRes stringRes:Int):SpannableString{

        val sectionInText = context.getString(stringRes)

        val color:Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context.resources.getColor(R.color.textRed, null)
        } else {
            context.resources.getColor(R.color.textRed)
        }

        val spannableString = SpannableString( "${sectionInText} *" )
        spannableString.setSpan(ForegroundColorSpan(color),
                sectionInText.length + 1,
                sectionInText.length + 2,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return spannableString
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