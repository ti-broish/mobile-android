package bg.dabulgaria.tibroish.presentation.ui.protocol.add//package bg.dabulgaria.tibroish.presentation.ui.protocol.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import bg.dabulgaria.tibroish.R
import javax.inject.Inject


class AddProtocolAdapter @Inject constructor(val presenter: IAddProtocolPresenter)
    : RecyclerView.Adapter<AddProtocolViewHolder>() {

    val list = mutableListOf<AddProtocolListItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddProtocolViewHolder {

        val inflater = LayoutInflater.from(parent.context)

        val view = inflater.inflate(when(viewType){

            AddProtocolListItemType.Header.ordinal -> R.layout.add_protocol_header_item
            AddProtocolListItemType.Section.ordinal -> R.layout.add_protocol_section_item
            AddProtocolListItemType.Image.ordinal -> R.layout.add_protocol_photo_item
            AddProtocolListItemType.Buttons.ordinal -> R.layout.add_protocol_buttons_item
            AddProtocolListItemType.SendSuccess.ordinal -> R.layout.add_protocol_send_success_item
            else-> throw NotImplementedError( "AddProtocolListItemType( $viewType ) ViewHolder layout not implemented")
        },
                parent, false)

        return when(viewType) {

            AddProtocolListItemType.Header.ordinal -> AddProtocolHeaderViewHolder(view)
            AddProtocolListItemType.Section.ordinal -> AddProtocolSectionViewHolder(view)
            AddProtocolListItemType.Image.ordinal -> AddProtocolImageViewHolder(view)
            AddProtocolListItemType.Buttons.ordinal -> AddProtocolButtonsViewHolder(view)
            AddProtocolListItemType.SendSuccess.ordinal -> AddProtocolSendSuccessViewHolder(view)
            else -> throw NotImplementedError("AddProtocolListItemType( $viewType ) View holder class not implemented")
        }
    }

    override fun getItemCount(): Int = list.size

    override fun getItemViewType(position: Int): Int = getItem(position).type.ordinal

    override fun onBindViewHolder(holder: AddProtocolViewHolder, position: Int)
            = holder.bind(position, getItem(position), presenter)

    private fun getItem(position: Int): AddProtocolListItem = list[position]

    companion object {
         val TAG = AddProtocolAdapter::class.simpleName
    }
}
