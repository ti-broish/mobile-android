package bg.dabulgaria.tibroish.presentation.ui.common.preview.images

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import bg.dabulgaria.tibroish.R
import kotlinx.android.synthetic.main.layout_preview_images.view.*

interface CloseListener{

    fun onClose(lastPosition: Int)
}

class PreviewImagesView : ConstraintLayout {

    //region construction
    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private var adapter: PreviewImagesAdapter? =null
    private var layoutManager: LinearLayoutManager?=null

    init {

        View.inflate(context, R.layout.layout_preview_images, this)
    }
    //endregion construction

    fun bindView(imagesList: List<PreviewImage>,
                 initialPosition: Int,
                 closeListener: CloseListener,
                 deleteListener: PreviewImageDeleteListener?,
                 checkListener: PreviewImageCheckListener?) {

        val metrics = context.resources.displayMetrics
        val landscape = metrics.widthPixels > metrics.heightPixels

        if (adapter == null || !landscape.equals(adapter?.landscape)) {
            adapter = PreviewImagesAdapter(deleteListener, checkListener, landscape)
            previewImagesRecyclerView.adapter = adapter
        }

        adapter?.list?.clear()
        adapter?.list?.addAll(imagesList)

        adapter?.notifyDataSetChanged()

        if (layoutManager == null) {

            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            previewImagesRecyclerView.layoutManager = layoutManager
            val snapHelper = PagerSnapHelper()
            snapHelper.attachToRecyclerView(previewImagesRecyclerView)
        }

        if(initialPosition>-1)
            previewImagesRecyclerView.scrollToPosition(initialPosition)

        previewImagesCloseImageView.setOnClickListener {
            val position = layoutManager?.findFirstVisibleItemPosition() ?: 0
            closeListener.onClose(position)
        }
    }

    fun updateItem(image: PreviewImage, index:Int) {

        adapter?.list?.let { it[index]=image }
        adapter?.notifyItemChanged(index)
    }

    fun getPosition(): Int = layoutManager?.findFirstVisibleItemPosition() ?: 0
}