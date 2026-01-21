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

    val list = mutableListOf<SendItemListItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SendItemViewHolder<*> {

        val inflater = LayoutInflater.from(parent.context)

        val view = inflater.inflate(when(viewType){

            SendItemListItemType.Header.ordinal -> R.layout.send_item_header_layout
            SendItemListItemType.Section.ordinal -> R.layout.send_item_section_layout
            SendItemListItemType.SectionManual.ordinal -> R.layout.send_item_section_manual_layout
            SendItemListItemType.Message.ordinal -> R.layout.send_item_message_layout
            SendItemListItemType.Image.ordinal -> R.layout.send_item_photo_layout
            SendItemListItemType.Buttons.ordinal -> R.layout.send_item_buttons_layout
            SendItemListItemType.SendSuccess.ordinal -> R.layout.send_item_success_layout
            SendItemListItemType.InfoText.ordinal -> R.layout.send_item_info_text_layout
            else-> throw NotImplementedError( "SendItemListItemType( $viewType ) ViewHolder layout not implemented")
        },
                parent, false)

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

    override fun getItemCount(): Int = list.size

    override fun getItemViewType(position: Int): Int = getItem(position).type.ordinal

    override fun onBindViewHolder(holder: SendItemViewHolder<*>, position: Int)
            = holder.bind(getItem(position), presenter)

    private fun getItem(position: Int): SendItemListItem = list[position]

    companion object {
         val TAG = SendItemAdapter::class.simpleName
    }
}
