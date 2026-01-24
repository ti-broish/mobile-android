package bg.dabulgaria.tibroish.presentation.ui.violation.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import bg.dabulgaria.tibroish.R
import bg.dabulgaria.tibroish.databinding.PreviewImageItemLayoutLandBinding
import bg.dabulgaria.tibroish.domain.violation.VoteViolationRemote
import bg.dabulgaria.tibroish.presentation.ui.common.IStatusColorUtil
import bg.dabulgaria.tibroish.databinding.ViolationsListItemLayoutBinding
import bg.dabulgaria.tibroish.presentation.ui.common.preview.images.PreviewImagesAdapter.PreviewImagesViewHolder
import javax.inject.Inject

class ViolationsAdapter @Inject constructor(private val statusColorUtil: IStatusColorUtil)
    : RecyclerView.Adapter<ViolationsAdapter.ViewHolder>() {

    val items = mutableListOf<VoteViolationRemote>()

    lateinit var onItemClickListener: View.OnClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val view = inflater.inflate(R.layout.violations_list_item_layout, parent, false)
        view.setOnClickListener(onItemClickListener)

        val binding = ViolationsListItemLayoutBinding.inflate(inflater, parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val context = holder.itemView.context

        var number = "${position+1}. "

        val violationSignal = items[position]

        holder.binding.violationSection.visibility = if(!violationSignal.section?.id.isNullOrEmpty())
            View.VISIBLE
        else
            View.GONE

        violationSignal.section?.id?.let {
            val section  = "${number}${context.getString(R.string.section_label)} $it"
            holder.binding.violationSection.text = section
            number=""
        }

        holder.binding.violationPlace.visibility = if(!violationSignal.town?.name.isNullOrEmpty())
            View.VISIBLE
        else
            View.GONE

        violationSignal.town?.name?.let {
            val place = "${number}${context.getString(R.string.location_label)} $it"
            holder.binding.violationPlace.text =  place
            number=""
        }

        val description = "${number}${context.getString(R.string.description)}: ${violationSignal.description}"
        holder.binding.violationDescription.text = description

        holder.binding.violationStatus.text = violationSignal.statusLocalized
        holder.binding.violationStatus.setTextColor(
            statusColorUtil.getColorForStatus(violationSignal.status.stringValue))
    }

    fun updateList(newList: List<VoteViolationRemote>) {
        items.clear()
        items.addAll(newList)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(
        val binding: ViolationsListItemLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root) {


    }
}