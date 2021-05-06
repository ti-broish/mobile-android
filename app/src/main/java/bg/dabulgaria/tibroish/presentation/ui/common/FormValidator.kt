package bg.dabulgaria.tibroish.presentation.ui.common

import android.util.Patterns
import bg.dabulgaria.tibroish.R

class FormValidator {


    fun processRequiredField(text: String, callback: (Int) -> Unit): Boolean {
        return processConditionalField(
                        text,
                        predicate = {
                            it.isNotBlank()
                        },
                        callback = callback,
                        errorResponse = R.string.required_field)
    }

    fun processEmailField(
            email: String,
            callback: (Int) -> Unit): Boolean {
        return processConditionalField(
                        email,
                        predicate = {
                            Patterns.EMAIL_ADDRESS.matcher(it).matches()
                        },
                        callback = callback,
                        errorResponse = R.string.invalid_email)
    }

    fun processPhoneNumberField(
            telephone: String,
            callback: (Int) -> Unit): Boolean {
        return processConditionalField(
                telephone,
                predicate = {
                    Patterns.PHONE.matcher(it).matches()
                },
                callback = callback,
                errorResponse = R.string.invalid_phone_number)
    }

    fun processConditionalField(
            text: String,
            predicate: (String) -> Boolean,
            callback: (Int) -> Unit,
            errorResponse: Int): Boolean {
        if (!predicate.invoke(text)) {
            callback(errorResponse)
            return false
        }
        return true
    }
}