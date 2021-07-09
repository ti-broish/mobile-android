package bg.dabulgaria.tibroish.presentation.ui.common.preview.images

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import bg.dabulgaria.tibroish.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import kotlinx.android.synthetic.main.preview_image_item_layout.view.*


interface PreviewImageDeleteListener{

    fun onDelete(position: Int, image: PreviewImage)
}

interface PreviewImageCheckListener{

    fun onCheckClick(position: Int, image: PreviewImage)
}

class PreviewImagesAdapter constructor(private val deleteListener: PreviewImageDeleteListener?,
                                       private val checkListener: PreviewImageCheckListener?,
                                       val landscape: Boolean)
    : RecyclerView.Adapter<PreviewImagesAdapter.PreviewImagesViewHolder>() {

class PreviewImagesViewHolder constructor(view: View) :RecyclerView.ViewHolder(view)

    val list = mutableListOf<PreviewImage>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PreviewImagesViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val viewId = if(landscape) R.layout.preview_image_item_layout_land else R.layout.preview_image_item_layout

        val view = inflater.inflate(viewId, parent, false)

        return PreviewImagesViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: PreviewImagesViewHolder, position: Int){

        val item = getItem(position)

        Glide.with(holder.itemView)
                .load(item.photoFilePath)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.itemView.photoImageView)

        holder.itemView.photoDeleteView.visibility = if(deleteListener == null) View.GONE else View.VISIBLE
        holder.itemView.photoDeleteView.setOnClickListener { deleteListener?.onDelete(position, item) }

        holder.itemView.photoCheckBox.visibility = if(checkListener == null) View.GONE else View.VISIBLE
        holder.itemView.photoCheckBox.setOnClickListener {
            checkListener?.onCheckClick(position, item)
        }
        holder.itemView.photoCheckBox.isChecked = item.photoSelected

        holder.itemView.photoImageView.setOnClickListener {
            checkListener?.onCheckClick(position, item)
        }

        if(item.photoPreviouslySelected) {

            holder.itemView.photoBoxView.visibility = View.VISIBLE
            holder.itemView.photoBoxView.alpha = 1f
        }
        else{

            holder.itemView.photoBoxView.visibility = View.GONE
            holder.itemView.photoBoxView.alpha = 0f
        }
    }

    private fun getItem(position: Int): PreviewImage = list[position]
}