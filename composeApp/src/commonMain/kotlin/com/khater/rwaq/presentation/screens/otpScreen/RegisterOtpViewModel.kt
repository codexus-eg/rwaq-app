package com.khater.rwaq.presentation.screens.otpScreen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.khater.rwaq.domain.useCases.auth.OtpUseCase
import com.khater.rwaq.domain.useCases.auth.RequestOtpType
import com.khater.rwaq.presentation.base.BaseViewModel
import com.khater.rwaq.presentation.mapper.handleAuthException
import com.khater.rwaq.presentation.mapper.mapAuthenticationErrorToMessage
import com.khater.rwaq.presentation.model.SnackBarState
import com.khater.rwaq.presentation.navigation.Screen
import com.khater.rwaq.presentation.screens.otpScreen.uiState.RegisterOtpInteractionListener
import com.khater.rwaq.presentation.screens.otpScreen.uiState.RegisterOtpScreenState
import com.khater.rwaq.presentation.screens.otpScreen.uiState.RegisterOtpUiEffect
import com.khater.rwaq.presentation.util.LoginConstants.SNACK_BAR_DELAY
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString

class RegisterOtpViewModel(
    savedStateHandle: SavedStateHandle,
    private val otpUseCase: OtpUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : BaseViewModel<RegisterOtpScreenState, RegisterOtpUiEffect>(
    RegisterOtpScreenState()
), RegisterOtpInteractionListener {
    val args = savedStateHandle.toRoute<Screen.OTPScreen>()

    init {
        updateState {
            it.copy(phoneNumber = args.phoneNumber)
        }
        startTimer()
    }

    override fun onChangeOtp(otp: String) {
        val filteredOtp = otp.filter { it.isDigit() }.take(OTP_LENGTH)
        if (filteredOtp == otp) {
            updateState {
                it.copy(
                    otpValue = otp,
                    isVerifyEnabled = filteredOtp.length == OTP_LENGTH
                )
            }
        }
    }

    override fun onClickBack() {
        sendNewEffect(RegisterOtpUiEffect.NavigateBack)
    }

    override fun onClickVerify() {
        tryToExecute(
            callee = {  verifyOTPCode()  },
            onStart = { updateState { it.copy(isLoading = true) } },
            onSuccess = { onOTPVerificationSuccess() },
            onError = ::onOTPVerificationError,
            dispatcher = dispatcher
        )
    }

    private suspend fun verifyOTPCode() {
        if (args.isRegister){
            otpUseCase.verifyOtp(
                otpCode = currentState.otpValue,
                phoneNumber = args.phoneNumber

            )
        }else{
            otpUseCase.verifyOtpForForgetPassword(
                otpCode = currentState.otpValue,
                phoneNumber = args.phoneNumber
            )
        }

    }

    private fun onOTPVerificationSuccess() {
        updateState { it.copy(isLoading = false) }
       if (args.isRegister) sendNewEffect(RegisterOtpUiEffect.NavigateToHome)
       else sendNewEffect(RegisterOtpUiEffect.NavigateToResetPassword(phoneNumber = args.phoneNumber,otp = currentState.otpValue))
    }

    private suspend fun onOTPVerificationError(throwable: Throwable) {
        updateState { it.copy(isLoading = false) }
        showSnackBar(message = getString(mapErrorMessage(throwable)), isSuccess = false)
    }

    override fun onClickResend() {
        tryToExecute(
            callee = { requestNewOTP() },
            onSuccess = { onResendOTPSuccess() },
            onError = ::onResendOTPError,
            dispatcher = dispatcher
        )
    }

    private fun onResendOTPSuccess() {
        startTimer()
    }

    private suspend fun onResendOTPError(throwable: Throwable) {
        showSnackBar(message = getString(mapErrorMessage(throwable)), isSuccess = false)
    }


    private suspend fun requestNewOTP() {
        otpUseCase.requestOtp(
            phoneNumber = args.phoneNumber,
            otpType = if (args.isRegister) RequestOtpType.Register else RequestOtpType.ForgetPassword
        )
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

    private fun startTimer() {
        viewModelScope.launch {
            updateState { it.copy(isResendEnabled = false) }
            for (time in OTP_RESEND_TIMER_SECONDS downTo 0) {
                updateState { it.copy(timer = time.toString()) }
                delay(1000)
            }
            updateState { it.copy(isResendEnabled = true) }
        }
    }


    companion object {
        private const val OTP_LENGTH = 6
        private const val OTP_RESEND_TIMER_SECONDS = 60
    }
}