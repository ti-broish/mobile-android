package bg.dabulgaria.tibroish.presentation.ui.common

import android.util.Patterns
import bg.dabulgaria.tibroish.R
import bg.dabulgaria.tibroish.domain.organisation.Organization
import bg.dabulgaria.tibroish.domain.providers.ILogger
import bg.dabulgaria.tibroish.presentation.ui.profile.ProfilePresenter
import javax.inject.Inject

class FormValidator @Inject constructor(
    private val logger: ILogger,
    private val organizationsManager: OrganizationsManager){
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
        if (!processRequiredField(telephone, errorCallback)) {
            return false
        }
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

    fun processEgnLastFourDigits(egn: String, callback: (Int) -> Unit): Boolean {
        if (!processRequiredField(egn, callback)) {
            return false
        }
        if (!processConditionalField(
                egn,
                predicate = {
                    it.length == 4
                },
                errorCallback = callback,
                errorResponse = R.string.egn_four_digits_required
            )
        ) {
            return false
        }
        return true
    }

    fun processOrganization(
        organizationName: String,
        organizationsData: MutableList<Organization>?,
        callback: (Int) -> Unit
    ): Boolean {
        if (organizationsData == null) {
            logger.d(ProfilePresenter.TAG, "organizationsData is null")
            callback(R.string.invalid_organization)
            return false
        }
        val organization =
            organizationsManager.getOrganizationWithName(organizationName, organizationsData)
        if (organization == null) {
            logger.d(ProfilePresenter.TAG, "organization name $organizationName$ does not exist")
            callback(R.string.invalid_organization)
            return false
        }
        return true
    }
}