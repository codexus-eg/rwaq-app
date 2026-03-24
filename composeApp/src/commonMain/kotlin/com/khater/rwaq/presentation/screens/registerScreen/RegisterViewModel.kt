package com.khater.rwaq.presentation.screens.registerScreen

import com.khater.rwaq.domain.useCases.auth.RegisterUseCase
import com.khater.rwaq.presentation.base.BaseViewModel
import com.khater.rwaq.presentation.mapper.handleAuthException
import com.khater.rwaq.presentation.mapper.mapAuthenticationErrorToMessage
import com.khater.rwaq.presentation.model.SnackBarState
import com.khater.rwaq.presentation.screens.registerScreen.uiState.RegisterInteractionListener
import com.khater.rwaq.presentation.screens.registerScreen.uiState.RegisterUiEffect
import com.khater.rwaq.presentation.screens.registerScreen.uiState.RegisterUiState
import com.khater.rwaq.presentation.util.Etisalat
import com.khater.rwaq.presentation.util.LoginConstants.FULL_NAME_SPACES
import com.khater.rwaq.presentation.util.LoginConstants.PASSWORD_LENGTH
import com.khater.rwaq.presentation.util.LoginConstants.PASSWORD_MAX_LENGTH
import com.khater.rwaq.presentation.util.LoginConstants.PHONE_NUMBER_LENGTH
import com.khater.rwaq.presentation.util.LoginConstants.SNACK_BAR_DELAY
import com.khater.rwaq.presentation.util.LoginConstants.USERNAME_MAX_LENGTH
import com.khater.rwaq.presentation.util.Orange
import com.khater.rwaq.presentation.util.Vodafone
import com.khater.rwaq.presentation.util.We
import com.khater.rwaq.presentation.util.whiteSpace

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString
import rwaq.composeapp.generated.resources.Res
import rwaq.composeapp.generated.resources.enter_valid_password
import rwaq.composeapp.generated.resources.enter_valid_phone_number
import rwaq.composeapp.generated.resources.passwords_do_not_match
import rwaq.composeapp.generated.resources.please_enter_full_name


class RegisterViewModel(
    private val registerUseCase:RegisterUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
): BaseViewModel<RegisterUiState, RegisterUiEffect>(RegisterUiState()),
    RegisterInteractionListener {
    override fun onClickBack() {
        sendNewEffect(newEffect = RegisterUiEffect.NavigateBack)
    }

    override fun onUsernameChanged(username: String) {
        username
            .takeIf { it.length < USERNAME_MAX_LENGTH }
            ?.let { newUsername ->
                updateState { it.copy(username = newUsername, usernameError = null) }
            }
    }

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

    override fun onConfirmPasswordChanged(password: String) {
        password.takeIf { it.length < PASSWORD_MAX_LENGTH }
            ?.let { newPassword ->
                updateState { it.copy(confirmPassword = newPassword, confirmPasswordError = null) }
            }
    }

    override fun onCheckIsAquaClient(isAquaClient: Boolean) {
     }

    override fun onPasswordVisibilityToggled() {
        updateState { it.copy(isPasswordVisible = !state.value.isPasswordVisible) }
    }

    override fun onConfirmPasswordVisibilityToggled() {
        updateState { it.copy(isConfirmPasswordVisible = !state.value.isConfirmPasswordVisible) }
    }

    override fun onClickNext(
        phoneNumber: String,
        username: String,
        password: String,
        isAquaClient: Boolean
    ) {
        if (validateInputs()){
            tryToExecute(
                callee = ::onClickRegister,
                onStart = { updateState { it.copy(isNextButtonLoading = true) } },
                onSuccess = { onRegisterSuccess() },
                onError = ::onRegisterError,
                onFinish = { updateState { it.copy(isNextButtonLoading = false) } },
                dispatcher = dispatcher
            )
        }
     }

    private suspend fun onClickRegister(){
        registerUseCase.register(
            phoneNumber = currentState.phoneNumber,
            username = currentState.username,
            password = currentState.password)
    }
    private fun onRegisterSuccess(){
        sendNewEffect(newEffect = RegisterUiEffect.NavigateToOTP(phoneNumber = currentState.phoneNumber))
    }

    private suspend fun onRegisterError(throwable: Throwable) {
        updateState { it.copy(isNextButtonLoading = false) }
        showSnackBar(message = getString(mapErrorMessage(throwable)), isSuccess = false)
    }
    private fun mapErrorMessage(throwable: Throwable): StringResource{
        val errorState = handleAuthException(throwable)
        return mapAuthenticationErrorToMessage(errorState)
//        return when(throwable){
//            is AuthenticationException -> mapAuthenticationErrorToMessage(handleAuthException(throwable))
//            else -> mapErrorToMessage(ErrorState.GenericError(throwable))
//        }
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
        val password = currentState.password
        val confirmPassword = currentState.confirmPassword
        val username = currentState.username.trim()


         val validPhoneNumberPrefix = phone.startsWith(String.Vodafone) ||
                phone.startsWith(String.Etisalat) ||
                phone.startsWith(String.Orange) ||
                phone.startsWith(String.We)
        val isPhoneValid = validPhoneNumberPrefix && phone.length == PHONE_NUMBER_LENGTH

        val nameParts = username.split(String.whiteSpace).filter { it.isNotBlank() }
        val isUsernameValid = nameParts.size >= FULL_NAME_SPACES

        val isPasswordContainsDigits = password.any { it.isDigit() }
        val isPasswordContainsChars = password.any { it.isLetter() }
        val isPasswordComplex = isPasswordContainsChars && isPasswordContainsDigits && password.length >= PASSWORD_LENGTH

        val doPasswordsMatch = password == confirmPassword

        updateState {
            it.copy(
                phoneNumberError = if (!isPhoneValid) Res.string.enter_valid_phone_number else null,
                usernameError = if (!isUsernameValid) Res.string.please_enter_full_name else null,
                passwordError = if (!isPasswordComplex) Res.string.enter_valid_password else null,
                confirmPasswordError = if (!doPasswordsMatch) Res.string.passwords_do_not_match else null
            )
        }

        return isPhoneValid && isUsernameValid && isPasswordComplex && doPasswordsMatch
    }



}