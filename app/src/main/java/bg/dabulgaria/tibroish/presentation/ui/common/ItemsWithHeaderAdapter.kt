package bg.dabulgaria.tibroish.presentation.ui.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

abstract class ItemsWithHeaderAdapter<T, U, V>
    : RecyclerView.Adapter<RecyclerView.ViewHolder>()
        where T : RecyclerView.ViewHolder,
                                      U : RecyclerView.ViewHolder,
                                      V : Any {
    companion object {
        const val VIEW_TYPE_HEADER = 1
        const val VIEW_TYPE_ITEM = 2
    }

    /**
     * Data wrapper that contains information for the header and items.
     */
    lateinit var item: V

    /**
     * Callback to be called when a non-header item is clicked.
     */
    lateinit var onItemClickListener: View.OnClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        when (viewType) {
            VIEW_TYPE_ITEM -> {
                val listItem = inflater.inflate(
                    getItemListItemLayoutRes(),
                    parent,
                    /* attachToRoot= */ false
                )
                listItem.setOnClickListener(onItemClickListener)
                return createItemViewHolder(listItem)
            }
            VIEW_TYPE_HEADER -> {
                val listItem = inflater.inflate(
                    getHeaderListItemLayoutRes(),
                    parent,
                    /* attachToRoot= */ false
                )
                return createHeaderViewHolder(listItem)
            }
            else -> throw IllegalStateException("No view holder found")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == VIEW_TYPE_ITEM) {
            onBindItem(holder as U, position-1);
        } else if (getItemViewType(position) == VIEW_TYPE_HEADER) {
            onBindHeader(holder as T)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            VIEW_TYPE_HEADER
        } else {
            VIEW_TYPE_ITEM
        }
    }

    override fun getItemCount(): Int {
        return /* headerCount= */ 1 + getItemsCount(item);
    }

    /**
     * Count of the items without the header.
     */
    abstract fun getItemsCount(item: V): Int

    abstract fun onBindHeader(viewHolder: T)

    abstract fun onBindItem(holder: U, itemIndex: Int)

    abstract fun createHeaderViewHolder(listItem: View): T

    abstract fun createItemViewHolder(listItem: View): U

    @LayoutRes
    abstract fun getHeaderListItemLayoutRes(): Int

    @LayoutRes
    abstract fun getItemListItemLayoutRes(): Int
}