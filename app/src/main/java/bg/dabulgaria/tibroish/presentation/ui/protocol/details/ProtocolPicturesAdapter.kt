package bg.dabulgaria.tibroish.presentation.ui.protocol.details

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import bg.dabulgaria.tibroish.R
import bg.dabulgaria.tibroish.domain.protocol.ProtocolRemote
import bg.dabulgaria.tibroish.presentation.ui.common.IStatusColorUtil
import bg.dabulgaria.tibroish.presentation.ui.common.ItemsWithHeaderAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import javax.inject.Inject

class ProtocolPictureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val image: ImageView = itemView.findViewById(R.id.image)
}

class ProtocolHeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val status: TextView = itemView.findViewById(R.id.status)
    val protocolId: TextView = itemView.findViewById(R.id.protocol_id)
    val sectionId: TextView = itemView.findViewById(R.id.section_id)
    val location: TextView = itemView.findViewById(R.id.location)
    val messageContainer:View = itemView.findViewById(R.id.message_container)
    val message: TextView = itemView.findViewById(R.id.message)
}

class ProtocolPicturesAdapter @Inject constructor(private val statusColorUtil: IStatusColorUtil)
    : ItemsWithHeaderAdapter<ProtocolHeaderViewHolder, ProtocolPictureViewHolder, ProtocolRemote>() {

    override fun onBindHeader(viewHolder: ProtocolHeaderViewHolder) {
        viewHolder.status.text = item.statusLocalized
        viewHolder.status.setTextColor(
            statusColorUtil.getColorForStatus(item.status.stringValue))
        viewHolder.protocolId.text = item.id
        viewHolder.sectionId.text = item.section.id
        viewHolder.location.text = item.section.place

        viewHolder.message.text = item.errorMessage?:""
        viewHolder.messageContainer.visibility = if(item.errorMessage.isNullOrEmpty())
            View.GONE
        else
            View.VISIBLE
    }

    override fun onBindItem(holder: ProtocolPictureViewHolder, itemIndex: Int) {
        val picture = item.pictures[itemIndex]
        Glide.with(holder.itemView)
            .load(picture.url)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(holder.image)
    }

    override fun createHeaderViewHolder(listItem: View): ProtocolHeaderViewHolder {
        return ProtocolHeaderViewHolder(listItem)
    }

    override fun createItemViewHolder(listItem: View): ProtocolPictureViewHolder {
        return ProtocolPictureViewHolder(listItem)
    }

    override fun getHeaderListItemLayoutRes(): Int {
        return R.layout.protocol_picture_list_item_header
    }

    override fun getItemListItemLayoutRes(): Int {
        return R.layout.protocol_picture_list_item_picture
    }

    override fun getItemsCount(item: ProtocolRemote): Int {
        return item.pictures.size
    }
}