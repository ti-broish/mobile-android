package bg.dabulgaria.tibroish.presentation.ui.common.item.send

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import bg.dabulgaria.tibroish.R
import bg.dabulgaria.tibroish.presentation.providers.getSpannableStringRedWarnStar
import bg.dabulgaria.tibroish.presentation.ui.registration.CountryCodesArrayAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

import bg.dabulgaria.tibroish.databinding.SendItemHeaderLayoutBinding
import bg.dabulgaria.tibroish.databinding.SendItemSectionLayoutBinding
import bg.dabulgaria.tibroish.databinding.SendItemSectionManualLayoutBinding
import bg.dabulgaria.tibroish.databinding.SendItemPhotoLayoutBinding
import bg.dabulgaria.tibroish.databinding.SendItemButtonsLayoutBinding
import bg.dabulgaria.tibroish.databinding.SendItemMessageLayoutBinding
import bg.dabulgaria.tibroish.databinding.SendItemSuccessLayoutBinding
import bg.dabulgaria.tibroish.databinding.SendItemInfoTextLayoutBinding

sealed class SendItemViewHolder<VB : ViewBinding>(
    protected val binding: VB
) : RecyclerView.ViewHolder(binding.root) {
    abstract fun bind(
        item: SendItemListItem,
        presenter: ISendItemPresenter
    )
}

class SendItemHeaderViewHolder(
    binding: SendItemHeaderLayoutBinding
) : SendItemViewHolder<SendItemHeaderLayoutBinding>(binding) {
    override fun bind(
        item: SendItemListItem,
        presenter: ISendItemPresenter
    ) {
        if (item.type != SendItemListItemType.Header)
            return

        val headerItem = item as SendItemListItemHeader

        binding.sendItemTitle.text = headerItem.titleText
    }
}

class SendItemSectionViewHolder(
    binding: SendItemSectionLayoutBinding
) : SendItemViewHolder<SendItemSectionLayoutBinding>(binding) {

    override fun bind(
        item: SendItemListItem,
        presenter: ISendItemPresenter
    ) {
        if (item.type != SendItemListItemType.Section)
            return

        val sectionItem = item as SendItemListItemSection

        val data = sectionItem.sectionsViewData ?: return

        binding.sectionPickerView.bindView(data, presenter)
    }
}

class SendItemSectionManualViewHolder(
    binding: SendItemSectionManualLayoutBinding
) : SendItemViewHolder<SendItemSectionManualLayoutBinding>(binding) {

    override fun bind(
        item: SendItemListItem,
        presenter: ISendItemPresenter
    ) {
        if (item.type != SendItemListItemType.SectionManual)
            return

        val sectionItem = item as SendItemListItemSectionManual

        binding.uniqueSectionValueTextView.setText(sectionItem.sectionId)

        binding.uniqueSectionValueTextView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                presenter.onManualSectionChanged(s.toString())
            }

        })
    }
}

class SendItemImageViewHolder(
    binding: SendItemPhotoLayoutBinding
) : SendItemViewHolder<SendItemPhotoLayoutBinding>(binding) {
    override fun bind(
        item: SendItemListItem,
        presenter: ISendItemPresenter
    ) {
        if (item.type != SendItemListItemType.Image)
            return

        val imageItem = item as SendItemListItemImage
        Glide.with(itemView)
                .load(imageItem.image.localFilePath)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.sendItemPhotoImageView)

        binding.sendItemPhotoDeleteView.setOnClickListener {
            presenter.onImageDeleteClick(imageItem, position)
        }

        binding.sendItemPhotoImageView.setOnClickListener {
            presenter.onImagePreviewClick(position)
        }
    }
}

class SendItemButtonsViewHolder(
    binding: SendItemButtonsLayoutBinding
) : SendItemViewHolder<SendItemButtonsLayoutBinding>(binding) {

    override fun bind(
        item: SendItemListItem,
        presenter: ISendItemPresenter
    ) {
        if (item.type != SendItemListItemType.Buttons)
            return

        val sendButtons = item as SendItemListItemButtons
        val imagesVisibility = if(sendButtons.supportsImages) View.VISIBLE else View.GONE
        binding.sendItemGalleryBtn.visibility = imagesVisibility
        binding.sendItemCameraBtn.visibility = imagesVisibility

        if(sendButtons.supportsImages) {
            binding.sendItemGalleryBtn.setOnClickListener { presenter.onAddFromGalleryClick() }
            binding.sendItemCameraBtn.setOnClickListener { presenter.onAddFromCameraClick() }
        }
        else {
            binding.sendItemGalleryBtn.setOnClickListener(null)
            binding.sendItemCameraBtn.setOnClickListener(null)
        }

        binding.sendItemContinueBtn.setOnClickListener { presenter.onSend() }
    }
}

class SendItemSendSuccessViewHolder(
    binding: SendItemSuccessLayoutBinding
) : SendItemViewHolder<SendItemSuccessLayoutBinding>(binding) {
    override fun bind(
        item: SendItemListItem,
        presenter: ISendItemPresenter
    ) {
        if (item.type != SendItemListItemType.SendSuccess)
            return

        val sendSuccess = item as SendItemListItemSendSuccess

        binding.itemSendText.text = sendSuccess.messageText
        binding.sendItemOkButton.setOnClickListener { presenter.onSuccessOkClick() }
    }
}

class SendItemMessageViewHolder(
    binding: SendItemMessageLayoutBinding
) : SendItemViewHolder<SendItemMessageLayoutBinding>(binding) {
    override fun bind(
        item: SendItemListItem,
        presenter: ISendItemPresenter
    ) {
        val context = itemView.context

        if (item.type != SendItemListItemType.Message)
            return

        val messageItem = item as SendItemListItemMessage

        binding.messageLabelTextView.text = R.string.violation_description
                .getSpannableStringRedWarnStar(itemView.context)

        binding.messageTextView.setText(messageItem.messageText)

        binding.messageTextView.addTextChangedListener(object:TextWatcher{

            override fun afterTextChanged(s: Editable?) {
                s?.toString()?.let{ presenter.onMessageChanged(it) }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.namesEditText.setText(messageItem.names)
        binding.inputNames.hint = R.string.first_middle_last_name.getSpannableStringRedWarnStar(context)
        binding.namesEditText.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                s?.toString()?.let{ presenter.onNamesChanged(it) }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.emailEditText.setText(messageItem.email)
        binding.inputEmail.hint = R.string.email.getSpannableStringRedWarnStar(context)
        binding.emailEditText.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                s?.toString()?.let{ presenter.onEmailChanged(it) }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.inputPhoneNumber.hint = R.string.telephone_number.getSpannableStringRedWarnStar(context)
        binding.phoneEditText.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                onPhoneChanged(presenter)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        messageItem.countryCodes?.let { countryCodes->
            val adapter = CountryCodesArrayAdapter(itemView.context, countryCodes)
            val dropdown = binding.areaCodeDropdown
            dropdown.setAdapter(adapter)
            dropdown.setText(adapter.getDefaultSelectedItem().code, /* filter= */ false)
            dropdown.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
                dropdown.setText(adapter.getItem(position)?.code, /* filter= */ false)
                adapter.filter.filter(null)
                onPhoneChanged(presenter)
            }

            if(messageItem.phone.isNotEmpty()) {
                val countryCode = countryCodes.find { messageItem.phone.startsWith(it.code ?: "WER") }

                countryCode?.code?.let {
                    val phone = messageItem.phone.replace(it, "")
                    dropdown.setText(it)
                    binding.phoneEditText.setText(phone)
                }
            }
        }

    }

    private fun onPhoneChanged(presenter: ISendItemPresenter){

        val areaCode = binding.areaCodeDropdown.text.toString()
        val localPhone = binding.phoneEditText.text.toString()
        presenter.onPhoneChanged( areaCode + localPhone)
    }
}

class SendItemInfoTextViewHolder(
    binding: SendItemInfoTextLayoutBinding
) : SendItemViewHolder<SendItemInfoTextLayoutBinding>(binding) {
    override fun bind(
        item: SendItemListItem,
        presenter: ISendItemPresenter
    ) {
        // implement if needed
    }
}