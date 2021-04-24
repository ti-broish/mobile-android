package bg.dabulgaria.tibroish.presentation.navigation

import android.os.Build
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import bg.dabulgaria.tibroish.R
import kotlinx.android.synthetic.main.nav_item_layout.view.*

class NavItemsAdapter constructor(private val items: List<NavItem>,
                                  var clickListener:OnMenuClickListener? )
    :RecyclerView.Adapter<NavItemsAdapter.ViewHolder>() {

    class ViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView){

    }

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.nav_item_layout, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val navItem = items[position]
        holder.itemView.navItemUnderline.visibility = if( position == 0 ) View.GONE else  View.VISIBLE

        if( navItem.postfixRedText?:-1 <= 0 ) {

            holder.itemView.navItemText.setText(navItem.labelResId)
        }
        else{
            val context = holder.itemView.context
            val firstString = context.getString(navItem.labelResId)
            val secondString = context.getString(navItem.postfixRedText!!)

            val color = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                context.resources.getColor(R.color.textRed, null)
            } else {
                context.resources.getColor(R.color.textRed)
            }

            val spannableString = SpannableString( "${firstString} ${secondString}" )
            spannableString.setSpan(ForegroundColorSpan(color),
                    firstString.length + 1,
                    firstString.length + secondString.length + 1,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            holder.itemView.navItemText.text = spannableString
        }
        holder.itemView.setOnClickListener { clickListener?.onItemClicked(navItem.action) }
    }
}