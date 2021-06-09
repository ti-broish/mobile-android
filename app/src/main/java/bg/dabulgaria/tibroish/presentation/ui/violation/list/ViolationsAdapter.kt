package bg.dabulgaria.tibroish.presentation.ui.violation.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import bg.dabulgaria.tibroish.R
import bg.dabulgaria.tibroish.domain.violation.VoteViolationRemote
import bg.dabulgaria.tibroish.presentation.ui.common.IStatusColorUtil
import kotlinx.android.synthetic.main.violations_list_item_layout.view.*
import javax.inject.Inject

class ViolationsAdapter @Inject constructor(private val statusColorUtil: IStatusColorUtil)
    : RecyclerView.Adapter<ViolationsAdapter.ViewHolder>() {

    val items = mutableListOf<VoteViolationRemote>()

    lateinit var onItemClickListener: View.OnClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.violations_list_item_layout, parent, false)
        view.setOnClickListener(onItemClickListener)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val context = holder.itemView.context

        var number = "${position+1}. "

        val violationSignal = items[position]

        holder.itemView.violationSection.visibility = if(!violationSignal.section?.id.isNullOrEmpty())
            View.VISIBLE
        else
            View.GONE

        violationSignal.section?.id?.let {
            val section  = "${number}${context.getString(R.string.section_label)} $it"
            holder.itemView.violationSection.text = section
            number=""
        }

        holder.itemView.violationPlace.visibility = if(!violationSignal.section?.place.isNullOrEmpty())
            View.VISIBLE
        else
            View.GONE

        violationSignal.section?.place?.let {
            val place = "${number}${context.getString(R.string.location_label)} $it"
            holder.itemView.violationPlace.text =  place
            number=""
        }

        val description = "${number}${context.getString(R.string.description)}: ${violationSignal.description}"
        holder.itemView.violationDescription.text = description

        holder.itemView.violationStatus.text = violationSignal.statusLocalized
        holder.itemView.violationStatus.setTextColor(
            statusColorUtil.getColorForStatus(violationSignal.status.stringValue))
    }

    fun updateList(newList: List<VoteViolationRemote>) {
        items.clear()
        items.addAll(newList)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {


    }
}