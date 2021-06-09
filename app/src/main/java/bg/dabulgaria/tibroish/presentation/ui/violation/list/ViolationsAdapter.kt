package bg.dabulgaria.tibroish.presentation.ui.violation.list

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import bg.dabulgaria.tibroish.R
import bg.dabulgaria.tibroish.domain.violation.VoteViolationRemote
import bg.dabulgaria.tibroish.presentation.ui.protocol.list.IViolationsListPresenter
import kotlinx.android.synthetic.main.violations_list_item_layout.view.*

class ViolationsAdapter(val items: MutableList<VoteViolationRemote>, val presenter: IViolationsListPresenter)
    : RecyclerView.Adapter<ViolationsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.violations_list_item_layout, parent, false)
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
        holder.itemView.violationStatus.setTextColor(presenter.getStatusColor(violationSignal.status))

        holder.itemView.setOnClickListener { presenter.onItemClick(violationSignal) }
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {


    }
}