package bg.dabulgaria.tibroish.presentation.ui.violation.details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import bg.dabulgaria.tibroish.R
import bg.dabulgaria.tibroish.domain.protocol.ProtocolRemote
import bg.dabulgaria.tibroish.domain.violation.VoteViolationRemote
import bg.dabulgaria.tibroish.presentation.ui.common.IStatusColorUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import java.lang.IllegalStateException
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

class ViolationPicturesAdapter @Inject constructor(private val statusColorUtil: IStatusColorUtil)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_HEADER = 1
        const val VIEW_TYPE_IMAGE = 2
    }

    lateinit var violation: VoteViolationRemote

    lateinit var onPictureClickListener: View.OnClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        when (viewType) {
            VIEW_TYPE_IMAGE -> {
                val listItem = inflater.inflate(
                    R.layout.violation_picture_list_item_picture,
                    parent,
                    /* attachToRoot= */ false
                )
                listItem.setOnClickListener(onPictureClickListener)
                return ViolationPictureViewHolder(listItem)
            }
            VIEW_TYPE_HEADER -> {
                val listItem = inflater.inflate(
                    R.layout.violation_picture_list_item_header,
                    parent,
                    /* attachToRoot= */ false
                )
                return ViolationHeaderViewHolder(listItem)
            }
            else -> throw IllegalStateException("No view holder found")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == VIEW_TYPE_IMAGE) {
            val picture = violation.pictures[position - 1]
            val localHolder = holder as ViolationPictureViewHolder
            Glide.with(holder.itemView)
                .load(picture.url)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(localHolder.image)
        } else if (getItemViewType(position) == VIEW_TYPE_HEADER) {
            val localHolder = holder as ViolationHeaderViewHolder
            localHolder.status.text = violation.statusLocalized
            localHolder.status.setTextColor(
                statusColorUtil.getColorForStatus(violation.status.stringValue))
            localHolder.violationId.text = violation.id
            localHolder.sectionId.text = violation.section?.id
            localHolder.location.text = violation.section?.place
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            VIEW_TYPE_HEADER
        } else {
            VIEW_TYPE_IMAGE
        }
    }

    override fun getItemCount(): Int {
        return /* headerCount= */ 1 + violation.pictures.size
    }
}