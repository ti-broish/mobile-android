package bg.dabulgaria.tibroish.presentation.ui.photopicker.camera//package bg.dabulgaria.tibroish.presentation.ui.protocol.list

import android.media.Image
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import bg.dabulgaria.tibroish.R
import javax.inject.Inject


class CameraPickerAdapter @Inject constructor(val presenter: ICameraPickerPresenter)
    : RecyclerView.Adapter<CameraPickerAdapter.ComicViewHolder>() {

    val list = mutableListOf<Image>()

    class ComicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal val titleTextView: AppCompatTextView = itemView.findViewById(R.id.titleTextView)
        internal val comicImageView: AppCompatImageView = itemView.findViewById(R.id.comicImageView)

        internal fun bind(regionS: Image?) {

            if (regionS == null)
                return

//            Glide.with(this.itemView)
//                    .load(regionS.thumbUlr)
//                    .transition(DrawableTransitionOptions.withCrossFade())
//                    .into(comicImageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComicViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.adapter_list_item, parent, false)
        val viewHodler = ComicViewHolder(view)

        viewHodler.itemView.setOnClickListener {

            val position = viewHodler.getAdapterPosition()
            if (position != RecyclerView.NO_POSITION) {

                val comic = list.get(position)
                //presenter.onComicClick(ComicDetailsViewData(comic))
            }
        }

        return viewHodler
    }

    override fun getItemCount(): Int {

        return list.size
    }

    override fun onBindViewHolder(holder: ComicViewHolder, position: Int) {

        if (list.size <= position)
            holder.bind(null)
        else {

            holder.bind(list[position])
        }
    }

    fun updateList( locationsS: Image) {

        list.clear()
//        if ( !locationsS.regions.isNullOrEmpty() )
//            list.addAll(locationsS.regions)

        notifyDataSetChanged()
    }

    companion object {
         val TAG = CameraPickerAdapter::class.simpleName
    }
}
