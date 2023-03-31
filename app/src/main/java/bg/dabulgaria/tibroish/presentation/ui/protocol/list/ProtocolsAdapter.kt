package bg.dabulgaria.tibroish.presentation.ui.protocol.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import bg.dabulgaria.tibroish.R
import bg.dabulgaria.tibroish.domain.protocol.ProtocolRemote
import bg.dabulgaria.tibroish.presentation.ui.common.IStatusColorUtil
import javax.inject.Inject

class ProtocolsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val sectionId: TextView = itemView.findViewById(R.id.sectionId)
    val sectionLocation: TextView = itemView.findViewById(R.id.sectionLocation)
    val sectionStatus: TextView = itemView.findViewById(R.id.sectionStatus)
}

class ProtocolsAdapter @Inject constructor(private val statusColorUtil: IStatusColorUtil)
    : RecyclerView.Adapter<ProtocolsViewHolder>() {

    val list = mutableListOf<ProtocolRemote>()

    lateinit var onItemClickListener: View.OnClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProtocolsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val listItem = inflater.inflate(
            R.layout.protocols_list_item,
            parent,
            /* attachToRoot= */ false)
        listItem.setOnClickListener(onItemClickListener)
        return ProtocolsViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: ProtocolsViewHolder, position: Int) {
        val protocol = list[position]
        holder.sectionId.text = holder.itemView.context.getString(
            R.string.protocols_list_section_id_unformatted,
            position+1,
            protocol.section?.id ?: "")
        holder.sectionLocation.text = protocol.section?.place ?: ""
        holder.sectionStatus.text = protocol.statusLocalized
        holder.sectionStatus.setTextColor(
            statusColorUtil.getColorForStatus(protocol.status.stringValue))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun updateList(newList: List<ProtocolRemote>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }
}