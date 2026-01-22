package bg.dabulgaria.tibroish.presentation.ui.common.preview.images

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import bg.dabulgaria.tibroish.databinding.PreviewImageItemLayoutBinding
import bg.dabulgaria.tibroish.databinding.PreviewImageItemLayoutLandBinding

interface PreviewImageDeleteListener {

    fun onDelete(position: Int, image: PreviewImage)
}

interface PreviewImageCheckListener {

    fun onCheckClick(position: Int, image: PreviewImage)
}

class PreviewImagesAdapter constructor(
    private val deleteListener: PreviewImageDeleteListener?,
    private val checkListener: PreviewImageCheckListener?,
    val landscape: Boolean
) : RecyclerView.Adapter<PreviewImagesAdapter.PreviewImagesViewHolder>() {

    sealed class PreviewImagesViewHolder constructor(
        private val binding: ConstraintLayout
    ) : RecyclerView.ViewHolder(binding) {
        abstract val photoImageView: AppCompatImageView
        abstract val photoDeleteView: ImageView
        abstract val photoCheckBox: CheckBox
        abstract val photoBoxView: View

        class Portrait(
            val binding: PreviewImageItemLayoutBinding
        ) : PreviewImagesViewHolder(binding.root) {
            override val photoImageView: AppCompatImageView
                get() = binding.photoImageView

            override val photoDeleteView: ImageView
                get() = binding.photoDeleteView

            override val photoCheckBox: CheckBox
                get() = binding.photoCheckBox

            override val photoBoxView: View
                get() = binding.photoBoxView
        }

        class Landscape(
            val binding: PreviewImageItemLayoutLandBinding
        ) : PreviewImagesViewHolder(binding.root) {

            override val photoImageView: AppCompatImageView
                get() = binding.photoImageView

            override val photoDeleteView: ImageView
                get() = binding.photoDeleteView

            override val photoCheckBox: CheckBox
                get() = binding.photoCheckBox

            override val photoBoxView: View
                get() = binding.photoBoxView
        }
    }

    val list = mutableListOf<PreviewImage>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PreviewImagesViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        if (landscape) {
            val binding = PreviewImageItemLayoutLandBinding.inflate(inflater, parent, false)

            return PreviewImagesViewHolder.Landscape(binding)
        } else {
            val binding = PreviewImageItemLayoutBinding.inflate(inflater, parent, false)

            return PreviewImagesViewHolder.Portrait(binding)
        }
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: PreviewImagesViewHolder, position: Int) {

        val item = getItem(position)

        Glide.with(holder.itemView)
            .load(item.photoFilePath)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(holder.photoImageView)

        holder.photoDeleteView.visibility =
            if (deleteListener == null) View.GONE else View.VISIBLE
        holder.photoDeleteView.setOnClickListener {
            deleteListener?.onDelete(
                position,
                item
            )
        }

        holder.photoCheckBox.visibility =
            if (checkListener == null) View.GONE else View.VISIBLE
        holder.photoCheckBox.setOnClickListener {
            checkListener?.onCheckClick(position, item)
        }
        holder.photoCheckBox.isChecked = item.photoSelected

        holder.photoImageView.setOnClickListener {
            checkListener?.onCheckClick(position, item)
        }

        if (item.photoPreviouslySelected) {

            holder.photoBoxView.visibility = View.VISIBLE
            holder.photoBoxView.alpha = 1f
        } else {

            holder.photoBoxView.visibility = View.GONE
            holder.photoBoxView.alpha = 0f
        }
    }

    private fun getItem(position: Int): PreviewImage = list[position]
}