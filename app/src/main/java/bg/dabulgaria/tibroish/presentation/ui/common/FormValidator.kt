package bg.dabulgaria.tibroish.presentation.ui.common

import android.util.Patterns
import bg.dabulgaria.tibroish.R

class FormValidator {
    fun processRequiredField(text: String, errorCallback: (Int) -> Unit): Boolean {
        return processConditionalField(
                        text,
                        predicate = {
                            it.isNotBlank()
                        },
                        errorCallback = errorCallback,
                        errorResponse = R.string.required_field)
    }

    fun processEmailField(
            email: String,
            errorCallback: (Int) -> Unit): Boolean {
        return processConditionalField(
                        email,
                        predicate = {
                            Patterns.EMAIL_ADDRESS.matcher(it).matches()
                        },
                        errorCallback = errorCallback,
                        errorResponse = R.string.invalid_email)
    }

    fun processPhoneNumberField(
            telephone: String,
            errorCallback: (Int) -> Unit): Boolean {
        return processConditionalField(
                telephone,
                predicate = {
                    Patterns.PHONE.matcher(it).matches()
                },
                errorCallback = errorCallback,
                errorResponse = R.string.invalid_phone_number)
    }

    fun processConditionalField(
            text: String,
            predicate: (String) -> Boolean,
            errorCallback: (Int) -> Unit,
            errorResponse: Int): Boolean {
        if (!predicate.invoke(text)) {
            errorCallback(errorResponse)
            return false
        }
        return true
    }
}