package bg.dabulgaria.tibroish.presentation.ui.common.item.send//package bg.dabulgaria.tibroish.presentation.ui.protocol.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import bg.dabulgaria.tibroish.R
import javax.inject.Inject


class SendItemAdapter @Inject constructor(val presenter: ISendItemPresenter)
    : RecyclerView.Adapter<SendItemViewHolder>() {

    val list = mutableListOf<SendItemListItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SendItemViewHolder {

        val inflater = LayoutInflater.from(parent.context)

        val view = inflater.inflate(when(viewType){

            SendItemListItemType.Header.ordinal -> R.layout.send_item_header_layout
            SendItemListItemType.Section.ordinal -> R.layout.send_item_section_layout
            SendItemListItemType.SectionManual.ordinal -> R.layout.send_item_section_manual_layout
            SendItemListItemType.Message.ordinal -> R.layout.send_item_message_layout
            SendItemListItemType.Image.ordinal -> R.layout.send_item_photo_layout
            SendItemListItemType.Buttons.ordinal -> R.layout.send_item_buttons_layout
            SendItemListItemType.SendSuccess.ordinal -> R.layout.send_item_success_layout
            else-> throw NotImplementedError( "SendItemListItemType( $viewType ) ViewHolder layout not implemented")
        },
                parent, false)

        return when(viewType) {

            SendItemListItemType.Header.ordinal -> SendItemHeaderViewHolder(view)
            SendItemListItemType.Section.ordinal -> SendItemSectionViewHolder(view)
            SendItemListItemType.SectionManual.ordinal -> SendItemSectionManualViewHolder(view)
            SendItemListItemType.Message.ordinal -> SendItemMessageViewHolder(view)
            SendItemListItemType.Image.ordinal -> SendItemImageViewHolder(view)
            SendItemListItemType.Buttons.ordinal -> SendItemButtonsViewHolder(view)
            SendItemListItemType.SendSuccess.ordinal -> SendItemSendSuccessViewHolder(view)
            else -> throw NotImplementedError("SendItemListItemType( $viewType ) View holder class not implemented")
        }
    }

    override fun getItemCount(): Int = list.size

    override fun getItemViewType(position: Int): Int = getItem(position).type.ordinal

    override fun onBindViewHolder(holder: SendItemViewHolder, position: Int)
            = holder.bind(position, getItem(position), presenter)

    private fun getItem(position: Int): SendItemListItem = list[position]

    companion object {
         val TAG = SendItemAdapter::class.simpleName
    }
}
