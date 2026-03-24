package com.khater.rwaq.presentation.screens.updateUserScreen

import androidx.lifecycle.viewModelScope
import com.khater.rwaq.data.util.email
import com.khater.rwaq.data.util.phoneNumber
import com.khater.rwaq.data.util.username
import com.khater.rwaq.domain.useCases.SendContactMessageUseCase
import com.khater.rwaq.domain.useCases.auth.UpdateUserUseCase
import com.khater.rwaq.presentation.base.BaseViewModel
import com.khater.rwaq.presentation.model.SnackBarState
import com.khater.rwaq.presentation.screens.contactUsScreen.uiState.ContactUsInteractionListener
import com.khater.rwaq.presentation.screens.contactUsScreen.uiState.ContactUsUiEffect
import com.khater.rwaq.presentation.screens.contactUsScreen.uiState.ContactUsUiState
import com.khater.rwaq.presentation.screens.updateUserScreen.uiState.UpdateUserInteractionListener
import com.khater.rwaq.presentation.screens.updateUserScreen.uiState.UpdateUserUiEffect
import com.khater.rwaq.presentation.screens.updateUserScreen.uiState.UpdateUserUiState
import com.khater.rwaq.presentation.util.LoginConstants.SNACK_BAR_DELAY
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.coroutines.FlowSettings
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import rwaq.composeapp.generated.resources.Res
import rwaq.composeapp.generated.resources.done
import rwaq.composeapp.generated.resources.email_invalid
import rwaq.composeapp.generated.resources.email_required
import rwaq.composeapp.generated.resources.error
import rwaq.composeapp.generated.resources.message_required
import rwaq.composeapp.generated.resources.message_sent_failed
import rwaq.composeapp.generated.resources.message_sent_success
import rwaq.composeapp.generated.resources.success
import rwaq.composeapp.generated.resources.username_required
@OptIn(ExperimentalSettingsApi::class)
class UpdateUserViewModel constructor(
    private val settings: FlowSettings,
    private val updateUserUseCase: UpdateUserUseCase,
) : BaseViewModel<UpdateUserUiState, UpdateUserUiEffect>(UpdateUserUiState()),
    UpdateUserInteractionListener {

     init {
        getUser()
     }


     private fun getUser() {
        viewModelScope.launch {
            val username = settings.username.first()
            val email = settings.email.first()
            updateState { it.copy(username = username, email = email) }
        }
    }


    override fun onEmailChanged(email: String) {
        // Clear error when user types
        updateState { it.copy(email = email, emailError = null) }
    }

    override fun onUsernameChanged(username: String) {
        updateState { it.copy(username = username, usernameError = null) }
    }

    override fun onSubmitClicked() {
        if (validateInput()) {
            sendMessage()
        }
    }

    override fun onBackClicked() {
        sendNewEffect(UpdateUserUiEffect.NavigateBack)
    }

    private fun validateInput(): Boolean {
        val currentEmail = state.value.email
        val currentUsername = state.value.username
         var isValid = true

        // 1. Validate Email
        if (currentEmail.isBlank()) {
            updateState { it.copy(emailError = Res.string.email_required) }
            isValid = false
        } else if (!isValidEmail(currentEmail)) {
            updateState { it.copy(emailError = Res.string.email_invalid) }
            isValid = false
        }

        if (currentUsername.isBlank()){
            updateState { it.copy(usernameError = Res.string.username_required) }
            isValid = false
        }



        return isValid
    }

    private fun sendMessage() {

        tryToExecute(
            onStart = { updateState { it.copy(isLoading = true,errorMessage = null) } },
            callee = {
                updateUserUseCase(
                    username = currentState.username,
                    email = currentState.email,
                )
            },
            onSuccess = {
                updateState { it.copy(isLoading = false,errorMessage = null) }
                showSnackBar(
                    title = getString(Res.string.success),
                    message = getString(Res.string.done),
                    isSuccess = true
                )
                 sendNewEffect(UpdateUserUiEffect.NavigateBack)
            },
            onError = { error ->
                showSnackBar(
                    title = getString(Res.string.error),
                    message = getString(Res.string.error),
                    isSuccess = false
                )
                updateState { it.copy(isLoading = false, errorMessage = error.message) }
             }
        )
    }

    private suspend fun showSnackBar(
        title: String? = null,
        message: String,
        isSuccess: Boolean,
        durationMillis: Long = SNACK_BAR_DELAY,
    ) {
        updateState { oldState ->
            oldState.copy(
                snackBar = SnackBarState(
                    isVisible = true,
                    title = title,
                    message = message,
                    isSuccess = isSuccess
                )
            )
        }

        delay(durationMillis)

        hideSnackBar()
    }

    private fun hideSnackBar() {
        updateState { oldState ->
            oldState.copy(snackBar = oldState.snackBar.copy(isVisible = false))
        }
    }

    private fun isValidEmail(email: String): Boolean {
        val emailRegex =
            "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
        return email.matches(emailRegex.toRegex())
    }

}