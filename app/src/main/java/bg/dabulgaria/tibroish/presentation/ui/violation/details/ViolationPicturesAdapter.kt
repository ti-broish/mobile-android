package bg.dabulgaria.tibroish.presentation.ui.violation.details

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import bg.dabulgaria.tibroish.R
import bg.dabulgaria.tibroish.domain.violation.VoteViolationRemote
import bg.dabulgaria.tibroish.presentation.ui.common.IStatusColorUtil
import bg.dabulgaria.tibroish.presentation.ui.common.ItemsWithHeaderAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import javax.inject.Inject

class ViolationPictureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val image: ImageView = itemView.findViewById(R.id.image)
}

class ViolationHeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val status: TextView = itemView.findViewById(R.id.status)
    val violationId: TextView = itemView.findViewById(R.id.violation_id)
    val sectionId: TextView = itemView.findViewById(R.id.section_id)
    val location: TextView = itemView.findViewById(R.id.location)
}

class ViolationPicturesAdapter @Inject constructor(private val statusColorUtil: IStatusColorUtil) :
    ItemsWithHeaderAdapter<ViolationHeaderViewHolder, ViolationPictureViewHolder, VoteViolationRemote>() {

    override fun getItemsCount(item: VoteViolationRemote): Int {
        return item.pictures.size
    }

    override fun onBindHeader(viewHolder: ViolationHeaderViewHolder) {
        viewHolder.status.text = item.statusLocalized
        viewHolder.status.setTextColor(
            statusColorUtil.getColorForStatus(item.status.stringValue)
        )
        viewHolder.violationId.text = item.id
        viewHolder.sectionId.text = item.section?.id
        viewHolder.location.text = item.section?.place
    }

    override fun onBindItem(holder: ViolationPictureViewHolder, itemIndex: Int) {
        val picture = item.pictures[itemIndex]
        Glide.with(holder.itemView)
            .load(picture.url)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(holder.image)
    }

    override fun createHeaderViewHolder(listItem: View): ViolationHeaderViewHolder {
        return ViolationHeaderViewHolder(listItem)
    }

    override fun createItemViewHolder(listItem: View): ViolationPictureViewHolder {
        return ViolationPictureViewHolder(listItem)
    }

    override fun getHeaderListItemLayoutRes(): Int {
        return R.layout.violation_picture_list_item_header
    }

    override fun getItemListItemLayoutRes(): Int {
        return R.layout.violation_picture_list_item_picture
    }
}