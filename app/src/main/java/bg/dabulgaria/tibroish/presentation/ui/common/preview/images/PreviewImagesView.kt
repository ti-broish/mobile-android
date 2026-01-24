package bg.dabulgaria.tibroish.presentation.ui.common.preview.images

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import bg.dabulgaria.tibroish.R
import bg.dabulgaria.tibroish.databinding.LayoutPreviewImagesBinding

interface CloseListener{

    fun onClose(lastPosition: Int)
}

class PreviewImagesView : ConstraintLayout {

    //region construction
    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        binding = LayoutPreviewImagesBinding.inflate(LayoutInflater.from(context), this, true)
    }

    private var adapter: PreviewImagesAdapter? =null
    private var layoutManager: LinearLayoutManager?=null
    private lateinit var binding: LayoutPreviewImagesBinding

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
            binding.previewImagesRecyclerView.adapter = adapter
        }

        adapter?.list?.clear()
        adapter?.list?.addAll(imagesList)

        adapter?.notifyDataSetChanged()

        if (layoutManager == null) {

            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            binding.previewImagesRecyclerView.layoutManager = layoutManager
            val snapHelper = PagerSnapHelper()
            snapHelper.attachToRecyclerView(binding.previewImagesRecyclerView)
        }

        if(initialPosition>-1)
            binding.previewImagesRecyclerView.scrollToPosition(initialPosition)

        binding.previewImagesCloseImageView.setOnClickListener {
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