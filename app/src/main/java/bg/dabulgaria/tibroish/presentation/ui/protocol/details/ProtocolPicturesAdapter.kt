package bg.dabulgaria.tibroish.presentation.ui.protocol.details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import bg.dabulgaria.tibroish.R
import bg.dabulgaria.tibroish.domain.protocol.ProtocolRemote
import bg.dabulgaria.tibroish.presentation.ui.common.IStatusColorUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import java.lang.IllegalStateException
import javax.inject.Inject

class ProtocolPictureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val image: ImageView = itemView.findViewById(R.id.image)
}

class ProtocolHeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val status: TextView = itemView.findViewById(R.id.status)
    val protocolId: TextView = itemView.findViewById(R.id.protocol_id)
    val sectionId: TextView = itemView.findViewById(R.id.section_id)
    val location: TextView = itemView.findViewById(R.id.location)
}

class ProtocolPicturesAdapter @Inject constructor(private val statusColorUtil: IStatusColorUtil)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_HEADER = 1
        const val VIEW_TYPE_IMAGE = 2
    }

    lateinit var protocol: ProtocolRemote

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        when (viewType) {
            VIEW_TYPE_IMAGE -> {
                val listItem = inflater.inflate(
                    R.layout.protocol_picture_list_item_picture,
                    parent,
                    /* attachToRoot= */ false
                )
                return ProtocolPictureViewHolder(listItem)
            }
            VIEW_TYPE_HEADER -> {
                val listItem = inflater.inflate(
                    R.layout.protocol_picture_list_item_header,
                    parent,
                    /* attachToRoot= */ false
                )
                return ProtocolHeaderViewHolder(listItem)
            }
            else -> throw IllegalStateException("No view holder found")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == VIEW_TYPE_IMAGE) {
            val picture = protocol.pictures[position - 1]
            val localHolder = holder as ProtocolPictureViewHolder
            Glide.with(holder.itemView)
                .load(picture.url)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(localHolder.image)
        } else if (getItemViewType(position) == VIEW_TYPE_HEADER) {
            val localHolder = holder as ProtocolHeaderViewHolder
            localHolder.status.text = protocol.statusLocalized
            localHolder.status.setTextColor(
                statusColorUtil.getColorForStatus(protocol.status.stringValue))
            localHolder.protocolId.text = protocol.id
            localHolder.sectionId.text = protocol.section.id
            localHolder.location.text = protocol.section.place
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
        return /* headerCount= */ 1 + protocol.pictures.size
    }
}