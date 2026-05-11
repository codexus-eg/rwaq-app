package com.khater.rwaq.presentation.screens.loginScreen


import com.khater.rwaq.domain.useCases.auth.LoginUseCase
import com.khater.rwaq.presentation.base.BaseViewModel
import com.khater.rwaq.presentation.mapper.handleAuthException
import com.khater.rwaq.presentation.mapper.mapAuthenticationErrorToMessage
import com.khater.rwaq.presentation.model.SnackBarState
import com.khater.rwaq.presentation.screens.loginScreen.uiState.LoginInteractionListener
import com.khater.rwaq.presentation.screens.loginScreen.uiState.LoginScreenState
import com.khater.rwaq.presentation.screens.loginScreen.uiState.LoginUiEffect
import com.khater.rwaq.presentation.util.LoginConstants.PASSWORD_MAX_LENGTH
import com.khater.rwaq.presentation.util.LoginConstants.PHONE_NUMBER_LENGTH
import com.khater.rwaq.presentation.util.LoginConstants.SNACK_BAR_DELAY
import com.khater.rwaq.presentation.util.Vodafone

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString
import rwaq.composeapp.generated.resources.Res
import rwaq.composeapp.generated.resources.enter_valid_phone_number

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : BaseViewModel<LoginScreenState, LoginUiEffect>(LoginScreenState()), LoginInteractionListener {

    override fun onPhoneNumberChanged(phoneNumber: String) {
        phoneNumber.filter { char -> char.isDigit() }
            .takeIf { it.length <= PHONE_NUMBER_LENGTH }
            ?.let { newPhoneNumber ->
                updateState { it.copy(phoneNumber = newPhoneNumber, phoneNumberError = null) }
            }
    }

    override fun onPasswordChanged(password: String) {
        password.takeIf { it.length < PASSWORD_MAX_LENGTH }
            ?.let { newPassword ->
                updateState { it.copy(password = newPassword, passwordError = null) }
            }
    }

    override fun onPasswordVisibilityToggled() {
        updateState { it.copy(isPasswordVisible = !state.value.isPasswordVisible) }
    }



    override fun onClickForgetPassword() {
        sendNewEffect(LoginUiEffect.NavigateToForgotPassword)
    }

    override fun onClickBack() {
        sendNewEffect(LoginUiEffect.NavigateBack)
    }

    override fun onClickGuestMode() {
        sendNewEffect(LoginUiEffect.NavigateToHomeScreen)
    }

    override fun onClickLogin() {
        if (validateInputs()) {
            tryToExecute(
                callee = ::onLoginClicked,
                onStart = { updateState { it.copy(isLoginButtonLoading = true) } },
                onSuccess = { onLoginSuccess() },
                onError = ::onLoginError,
                onFinish = { updateState { it.copy(isLoginButtonLoading = false) } },
                dispatcher = dispatcher
            )
        }
    }
    private suspend fun onLoginClicked() {
        loginUseCase.login(phoneNumber = "+966" + currentState.phoneNumber)
    }

    private fun onLoginSuccess() {
        sendNewEffect(LoginUiEffect.NavigateToOtpScreen(currentState.phoneNumber))
    }

    private suspend fun onLoginError(throwable: Throwable) {
        updateState { it.copy(isLoginButtonLoading = false) }
        showSnackBar(message = getString(mapErrorMessage(throwable)), isSuccess = false)
    }
    private fun validateInputs(): Boolean {
        val phone = currentState.phoneNumber
       // val password = currentState.password

//        val validPhoneNumberPrefix = phone.startsWith(String.Vodafone) ||
//                phone.startsWith(String.Etisalat) ||
//                phone.startsWith(String.Orange) ||
//                phone.startsWith(String.We)

        val validPhoneNumberPrefix = phone.startsWith("5") || phone.startsWith("0")
//        val isPasswordContainsDigits = password.any { it.isDigit() }
//        val isPasswordContainsChars = password.any { it.isLetter() }

        val isPhoneValid = validPhoneNumberPrefix && phone.length == PHONE_NUMBER_LENGTH
//        val isPasswordComplex =
//            isPasswordContainsChars && isPasswordContainsDigits && password.length >= PASSWORD_LENGTH

        updateState {
            it.copy(
                phoneNumberError = if (!isPhoneValid) Res.string.enter_valid_phone_number else null,
//                passwordError = if (!isPasswordComplex) Res.string.enter_valid_password else null
            )
        }

        return isPhoneValid
    }

    private fun mapErrorMessage(throwable: Throwable): StringResource{
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

}


