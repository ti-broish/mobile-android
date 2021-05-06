package bg.dabulgaria.tibroish.presentation.ui.photopicker.gallery//package bg.dabulgaria.tibroish.presentation.ui.protocol.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import bg.dabulgaria.tibroish.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import kotlinx.android.synthetic.main.gallery_picker_photo_item.view.*
import javax.inject.Inject


class GridPickerAdapter @Inject constructor(private val presenter: IPhotoPickerPresenter)
    : RecyclerView.Adapter<GridPickerAdapter.PickerImageViewHolder>() {

    val list = mutableListOf<PhotoItem>()

    class PickerImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PickerImageViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.gallery_picker_photo_item, parent, false)
        return PickerImageViewHolder(view)
    }

    override fun getItemCount(): Int {

        return list.size
    }

    override fun onBindViewHolder(holder: PickerImageViewHolder, position: Int) {

        val item = list[position]

        Glide.with(holder.itemView)
                .load(item.imageFilePath)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.itemView.photoImageView)

        holder.itemView.photoCheckBox.isChecked = item.isSelected

        if(item.previouslySelected) {

            holder.itemView.photoBoxView.visibility = View.VISIBLE
            holder.itemView.photoBoxView.alpha = 1f
            holder.itemView.photoImageView.setOnClickListener(null)
            holder.itemView.photoCheckBox.setOnClickListener(null)
        }
        else {

            holder.itemView.photoBoxView.visibility = View.GONE
            holder.itemView.photoBoxView.alpha = 0f
            holder.itemView.photoImageView.setOnClickListener { presenter.onImageClick(item, position) }
            holder.itemView.photoCheckBox.setOnClickListener { presenter.onImageClick(item, position) }
        }
    }

    fun updateList(newItemsList:List<PhotoItem>) {

        list.clear()
        list.addAll(newItemsList)

        notifyDataSetChanged()
    }

    fun updateItem(photoItem:PhotoItem, index:Int) {

        list[index]=photoItem
        notifyItemChanged(index)
    }

    companion object {
         val TAG = GridPickerAdapter::class.simpleName
    }
}
