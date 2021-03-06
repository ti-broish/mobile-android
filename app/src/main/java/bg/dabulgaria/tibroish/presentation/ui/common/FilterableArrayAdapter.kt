package bg.dabulgaria.tibroish.presentation.ui.common

import android.app.ActionBar
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.TextView
import bg.dabulgaria.tibroish.R

abstract class FilterableArrayAdapter<T>(context: Context, objects: MutableList<T>)
    : ArrayAdapter<T>(context, /* resource= */ 0, objects) {

    private var layoutInflater: LayoutInflater
    private val initialObjects: List<T>
    private val filter: Filter

    init {
        this.initialObjects = ArrayList(objects)
        this.layoutInflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        this.filter = ItemFilter(initialObjects)
    }

    protected abstract fun getFormattedTextForItem(item: T): String
    protected abstract fun doesItemMatchConstraint(item: T, constraint: CharSequence): Boolean

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val rootView: View = convertView
                ?: layoutInflater.inflate(R.layout.filter_adapter_item, parent, false)

        val textView = rootView.findViewById<TextView>(R.id.filterItemText)
        textView.text = getFormattedTextForItem(getItem(position)!!)

        return rootView
    }

    override fun getFilter(): Filter {
        return filter
    }

    protected fun getInitialObjects(): List<T> {
        return initialObjects
    }

    inner class ItemFilter(private val initialObjects: List<T>) : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults? {
            if (constraint == null) {
                return null
            }
            val filterResults = FilterResults()
            val resultsList = initialObjects
                    .filter { item -> doesItemMatchConstraint(item, constraint) }
                    .toList()
            filterResults.values = resultsList
            filterResults.count = resultsList.size
            return filterResults
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            clear()
            if (results == null) {
                addAll(initialObjects)
                return
            }
            addAll(results.values as List<T>)
        }
    }
}