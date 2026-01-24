package bg.dabulgaria.tibroish.presentation.ui.photopicker.gallery//package bg.dabulgaria.tibroish.presentation.ui.protocol.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import bg.dabulgaria.tibroish.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import bg.dabulgaria.tibroish.databinding.GalleryPickerPhotoItemBinding
import javax.inject.Inject


class GridPickerAdapter @Inject constructor(private val presenter: IPhotoPickerPresenter)
    : RecyclerView.Adapter<GridPickerAdapter.PickerImageViewHolder>() {

    val list = mutableListOf<PhotoItem>()

    class PickerImageViewHolder(
        val binding: GalleryPickerPhotoItemBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PickerImageViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val binding = GalleryPickerPhotoItemBinding.inflate(inflater, parent, false)

        return PickerImageViewHolder(binding)
    }

    override fun getItemCount(): Int {

        return list.size
    }

    override fun onBindViewHolder(holder: PickerImageViewHolder, position: Int) {

        val item = list[position]

        Glide.with(holder.itemView)
                .load(item.photoFilePath)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.binding.photoImageView)

        holder.binding.photoCheckBox.isChecked = item.isSelected

        if(item.previouslySelected) {

            holder.binding.photoBoxView.visibility = View.VISIBLE
            holder.binding.photoBoxView.alpha = 1f
            holder.binding.photoImageView.setOnClickListener(null)
            holder.binding.photoCheckBox.setOnClickListener(null)
        }
        else {

            holder.binding.photoBoxView.visibility = View.GONE
            holder.binding.photoBoxView.alpha = 0f
            holder.binding.photoImageView.setOnClickListener { presenter.onImageClick(position) }
            holder.binding.photoCheckBox.setOnClickListener { presenter.onImageClick(position) }
        }

        holder.binding.photoZoom.setOnClickListener { presenter.onPreviewImageClick(position) }
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
