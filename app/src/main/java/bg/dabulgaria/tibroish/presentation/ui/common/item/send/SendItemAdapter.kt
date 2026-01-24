package bg.dabulgaria.tibroish.presentation.ui.common.item.send//package bg.dabulgaria.tibroish.presentation.ui.protocol.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import bg.dabulgaria.tibroish.R
import bg.dabulgaria.tibroish.databinding.SendItemHeaderLayoutBinding
import bg.dabulgaria.tibroish.databinding.SendItemSectionLayoutBinding
import bg.dabulgaria.tibroish.databinding.SendItemSectionManualLayoutBinding
import bg.dabulgaria.tibroish.databinding.SendItemPhotoLayoutBinding
import bg.dabulgaria.tibroish.databinding.SendItemButtonsLayoutBinding
import bg.dabulgaria.tibroish.databinding.SendItemMessageLayoutBinding
import bg.dabulgaria.tibroish.databinding.SendItemSuccessLayoutBinding
import bg.dabulgaria.tibroish.databinding.SendItemInfoTextLayoutBinding
import javax.inject.Inject


class SendItemAdapter @Inject constructor(
    val presenter: ISendItemPresenter
) : RecyclerView.Adapter<SendItemViewHolder<*>>() {

    val listItems = mutableListOf<SendItemListItem>()

    override fun getItemCount(): Int = listItems.size

    override fun getItemViewType(position: Int): Int {
        return listItems[position].type.ordinal
    }

    override fun onBindViewHolder(holder: SendItemViewHolder<*>, position: Int) {
        holder.bind(listItems[position], presenter)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SendItemViewHolder<*> {

        val inflater = LayoutInflater.from(parent.context)

        return when(viewType) {
            SendItemListItemType.Header.ordinal -> SendItemHeaderViewHolder(
                SendItemHeaderLayoutBinding.inflate(inflater, parent, false)
            )

            SendItemListItemType.Section.ordinal -> SendItemSectionViewHolder(
                SendItemSectionLayoutBinding.inflate(inflater, parent, false)
            )

            SendItemListItemType.SectionManual.ordinal -> SendItemSectionManualViewHolder(
                SendItemSectionManualLayoutBinding.inflate(inflater, parent, false)
            )

            SendItemListItemType.Message.ordinal -> SendItemMessageViewHolder(
                SendItemMessageLayoutBinding.inflate(inflater, parent, false)
            )

            SendItemListItemType.Image.ordinal -> SendItemImageViewHolder(
                SendItemPhotoLayoutBinding.inflate(inflater, parent, false)
            )

            SendItemListItemType.Buttons.ordinal -> SendItemButtonsViewHolder(
                SendItemButtonsLayoutBinding.inflate(inflater, parent, false)
            )

            SendItemListItemType.SendSuccess.ordinal -> SendItemSendSuccessViewHolder(
                SendItemSuccessLayoutBinding.inflate(inflater, parent, false)
            )

            SendItemListItemType.InfoText.ordinal -> SendItemInfoTextViewHolder(
                SendItemInfoTextLayoutBinding.inflate(inflater, parent, false)
            )
            else -> throw NotImplementedError("SendItemListItemType( $viewType ) View holder class not implemented")
        }
    }

    companion object {
        val TAG = SendItemAdapter::class.simpleName
    }
}
