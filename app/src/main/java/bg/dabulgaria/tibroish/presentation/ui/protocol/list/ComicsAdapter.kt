package bg.dabulgaria.tibroish.presentation.ui.protocol.list

import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import bg.dabulgaria.tibroish.R
import bg.dabulgaria.tibroish.domain.Locations.LocationsS
import bg.dabulgaria.tibroish.domain.Locations.RegionS
import javax.inject.Inject


class ComicsAdapter
@Inject
constructor( val presenter: IComicListPresenter) : RecyclerView.Adapter<ComicsAdapter.ComicViewHolder>() {

    val list = mutableListOf<RegionS>()

    class ComicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal val titleTextView: AppCompatTextView = itemView.findViewById(R.id.titleTextView)
        internal val comicImageView: AppCompatImageView = itemView.findViewById(R.id.comicImageView)

        internal fun bind(regionS: RegionS?) {

            if (regionS == null)
                return

            titleTextView.text = regionS.name
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

    fun updateList( locationsS: LocationsS) {

        list.clear()
        if ( !locationsS.regions.isNullOrEmpty() )
            list.addAll(locationsS.regions)

        notifyDataSetChanged()
    }

    companion object {
         val TAG = ComicsAdapter::class.simpleName
    }
}
