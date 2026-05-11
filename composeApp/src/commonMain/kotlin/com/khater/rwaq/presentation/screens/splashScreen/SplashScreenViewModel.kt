package com.khater.rwaq.presentation.screens.splashScreen

import co.touchlab.kermit.Logger
import com.khater.rwaq.domain.repository.authentication.AuthenticationRepository
import com.khater.rwaq.presentation.base.BaseViewModel
import com.khater.rwaq.presentation.mapper.handleAuthException
import com.khater.rwaq.presentation.mapper.mapAuthenticationErrorToMessage
import com.khater.rwaq.presentation.model.SnackBarState
import com.khater.rwaq.presentation.screens.splashScreen.uiState.SplashUiEffect
import com.khater.rwaq.presentation.screens.splashScreen.uiState.SplashUiState
import com.khater.rwaq.presentation.util.LoginConstants.SNACK_BAR_DELAY
import com.khater.rwaq.presentation.util.ReferralManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString

class SplashScreenViewModel(
    private val authenticationRepository: AuthenticationRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val referralManager: ReferralManager,

    ) : BaseViewModel<SplashUiState, SplashUiEffect>(SplashUiState()) {

    init {
        runAnimationDelay()
        checkAuthenticationStatus()
        checkReferral()
    }

    private fun checkReferral() {
        tryToExecute(
            callee = { referralManager.checkAndSubmitReferral() },
            onSuccess = {userID ->
                updateState { it.copy(userId = userID) }
                Logger.i { "onSuccess" }
            },
            onError = {throwable ->
                updateState { it.copy(userId =throwable.message ) }

                Logger.i { "${throwable.message}" }

            },
            dispatcher = dispatcher
        )
    }

    private fun checkAuthenticationStatus() {
        tryToExecute(
            callee = authenticationRepository::isUserLoggedIn,
            onSuccess = ::onSuccessCheckedAuthentication,
            onError = ::onFailureCheckedAuthentication,
            dispatcher = dispatcher
        )
    }


    private suspend fun onSuccessCheckedAuthentication(token: Flow<Boolean>) {
        token.collect { isUserLoggedIn ->
            updateState {
                it.copy(
                    isLoggedIn = isUserLoggedIn,
                    authFinished = true
                )
            }
            tryNavigate()
        }
    }

    private suspend fun onFailureCheckedAuthentication(throwable: Throwable) {
        showSnackBar(message = getString(mapErrorMessage(throwable)), isSuccess = false)

        updateState {
            it.copy(
                isLoggedIn = false,
                authFinished = true
            )
        }
        tryNavigate()
    }


    private fun mapErrorMessage(throwable: Throwable): StringResource {
        val errorState = handleAuthException(throwable)
        return mapAuthenticationErrorToMessage(errorState)
    }

    private fun runAnimationDelay() {
        tryToExecute(
            callee = {
                delay(500)
            },
            onSuccess = {
                updateState { it.copy(startAnimation = true) }
            },
            onFinish = {
                delay(3500)
                updateState { it.copy(animationFinished = true) }
                tryNavigate()
            },
            onError = {}
        )
    }

    private fun tryNavigate() {
        val state = currentState

        if (state.animationFinished && state.authFinished) {
            if (state.isLoggedIn == true) {
                sendNewEffect(SplashUiEffect.NavigateToHome)
            } else {
                sendNewEffect(SplashUiEffect.NavigateToOnBoarding)
            }
        }
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