package com.khater.rwaq.presentation.screens.forgetPasswordScreen


import com.khater.rwaq.domain.useCases.auth.OtpUseCase
import com.khater.rwaq.domain.useCases.auth.RequestOtpType
import com.khater.rwaq.presentation.base.BaseViewModel
import com.khater.rwaq.presentation.mapper.handleAuthException
import com.khater.rwaq.presentation.mapper.mapAuthenticationErrorToMessage
import com.khater.rwaq.presentation.model.SnackBarState
import com.khater.rwaq.presentation.screens.forgetPasswordScreen.uiState.ForgetPasswordInteractionListener
import com.khater.rwaq.presentation.screens.forgetPasswordScreen.uiState.ForgetPasswordUiEffect
import com.khater.rwaq.presentation.screens.forgetPasswordScreen.uiState.ForgetPasswordUiState
import com.khater.rwaq.presentation.util.Etisalat
import com.khater.rwaq.presentation.util.LoginConstants.PHONE_NUMBER_LENGTH
import com.khater.rwaq.presentation.util.LoginConstants.SNACK_BAR_DELAY
import com.khater.rwaq.presentation.util.Orange
import com.khater.rwaq.presentation.util.Vodafone
import com.khater.rwaq.presentation.util.We

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString
import rwaq.composeapp.generated.resources.Res
import rwaq.composeapp.generated.resources.enter_valid_phone_number

class ForgetPasswordViewModel(
    private val otpUseCase: OtpUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : BaseViewModel<ForgetPasswordUiState, ForgetPasswordUiEffect>(ForgetPasswordUiState()),
    ForgetPasswordInteractionListener {
    override fun onClickBack() {
        sendNewEffect(newEffect = ForgetPasswordUiEffect.NavigateBack)
    }

    override fun onPhoneNumberChanged(phoneNumber: String) {
        phoneNumber.filter { char -> char.isDigit() }
            .takeIf { it.length <= PHONE_NUMBER_LENGTH }
            ?.let { newPhoneNumber ->
                updateState { it.copy(phoneNumber = newPhoneNumber, phoneNumberError = null) }
            }
    }

    override fun onSendOtpCode() {

        if (validateInputs()){
           tryToExecute(
               onStart = { updateState { it.copy(isLoginButtonLoading = true) } },
               callee = { requestNewOTP() },
               onSuccess = { onRequestNewOtpSuccess() },
               onError = ::onRequestNewOtpError,
               dispatcher = dispatcher
           )
       }
    }
    private suspend fun requestNewOTP() {
        otpUseCase.requestOtp(
            phoneNumber = currentState.phoneNumber,
            otpType = RequestOtpType.ForgetPassword
        )
    }
    private fun onRequestNewOtpSuccess() {
        updateState { it.copy(isLoginButtonLoading = false) }
        sendNewEffect(newEffect = ForgetPasswordUiEffect.NavigateToOtpScreen(currentState.phoneNumber))
    }
    private suspend fun onRequestNewOtpError(throwable: Throwable) {
        updateState { it.copy(isLoginButtonLoading = false) }
        showSnackBar(message = getString(mapErrorMessage(throwable)), isSuccess = false)
    }
    private fun mapErrorMessage(throwable: Throwable): StringResource {
        val errorState = handleAuthException(throwable)
        return mapAuthenticationErrorToMessage(errorState)
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
    private fun validateInputs(): Boolean {
        val phone = currentState.phoneNumber

        val validPhoneNumberPrefix = phone.startsWith(String.Vodafone) ||
                phone.startsWith(String.Etisalat) ||
                phone.startsWith(String.Orange) ||
                phone.startsWith(String.We)


        val isPhoneValid = validPhoneNumberPrefix && phone.length == PHONE_NUMBER_LENGTH

        updateState {
            it.copy(
                phoneNumberError = if (!isPhoneValid) Res.string.enter_valid_phone_number else null,
             )
        }

        return isPhoneValid
    }
}