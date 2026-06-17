package com.khater.rwaq.presentation.screens.profileScreen

import androidx.lifecycle.viewModelScope
import com.khater.rwaq.domain.repository.authentication.AuthenticationRepository
import com.khater.rwaq.domain.repository.settings.SettingsRepository
import com.khater.rwaq.domain.useCases.GetUserUseCase
import com.khater.rwaq.domain.useCases.auth.DeleteAccountUseCase
import com.khater.rwaq.domain.useCases.auth.LogoutUseCase
import com.khater.rwaq.domain.util.AppLanguage
import com.khater.rwaq.presentation.base.BaseViewModel
import com.khater.rwaq.presentation.mapper.handleAuthException
import com.khater.rwaq.presentation.mapper.handleDefaultException
import com.khater.rwaq.presentation.mapper.mapAuthenticationErrorToMessage
import com.khater.rwaq.presentation.mapper.mapDefaultErrorToMessage
import com.khater.rwaq.presentation.model.DialogState
import com.khater.rwaq.presentation.model.SnackBarState
import com.khater.rwaq.presentation.screens.profileScreen.uiState.LanguageDialogUiState
import com.khater.rwaq.presentation.screens.profileScreen.uiState.ProfileDialogType
import com.khater.rwaq.presentation.screens.profileScreen.uiState.ProfileInteractionListener
import com.khater.rwaq.presentation.screens.profileScreen.uiState.ProfileOption
import com.khater.rwaq.presentation.screens.profileScreen.uiState.ProfileUiEffect
import com.khater.rwaq.presentation.screens.profileScreen.uiState.ProfileUiEffect.NavigateToBranches
import com.khater.rwaq.presentation.screens.profileScreen.uiState.ProfileUiState
import com.khater.rwaq.presentation.util.LoginConstants.SNACK_BAR_DELAY
import com.khater.rwaq.presentation.util.isLoginRequiredError
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.coroutines.FlowSettings
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString
import rwaq.composeapp.generated.resources.Res
import rwaq.composeapp.generated.resources.error
import rwaq.composeapp.generated.resources.please_login_first

@OptIn(ExperimentalSettingsApi::class)
class ProfileViewModel(
    private val settingsRepository: SettingsRepository,
    val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val logoutUseCase: LogoutUseCase,
    private val deleteAccountUseCase: DeleteAccountUseCase,
    private val getUserUseCase:GetUserUseCase,
    private val authenticationRepository: AuthenticationRepository,
    private val settings: FlowSettings,

    ) :
    BaseViewModel<ProfileUiState, ProfileUiEffect>(ProfileUiState()), ProfileInteractionListener {

    init {
        checkAuthentication()
        getAppSettings()
    }

    fun checkAuthentication() {
        viewModelScope.launch {
            val isLoggedIn = authenticationRepository.isUserLoggedIn().first()
            if (isLoggedIn) {
                getUser()
                updateState { it.copy(isGuest = false, showGuestDialog = false) }
            } else {
                updateState {
                    it.copy(
                        isGuest = true,
                        showGuestDialog = true,
                        isLoading = false,
                        userName = "",
                        phoneNumber = "",
                        points = 0,
                        userId = ""
                    )
                }
            }
        }
    }

    private fun getUser() {
        tryToExecute(
            callee = {getUserUseCase()},
            onSuccess = {user ->
                updateState {
                    it.copy(
                        userName = user.username,
                        phoneNumber = user.phone,
                        isVipUser = user.isVipUser,
                        points = user.points,
                        userId = user.id,
                        referCode = user.referCode,
                        isGuest = false
                    )
                }
            },
            onError = { error ->
                if (error.isLoginRequiredError()) {
                    updateState {
                        it.copy(
                            isGuest = true,
                            showGuestDialog = true,
                            isLoading = false,
                            userName = "",
                            phoneNumber = "",
                            points = 0,
                            userId = ""
                        )
                    }
                    showSnackBar(
                        title = getString(Res.string.error),
                        message = getString(Res.string.please_login_first),
                        isSuccess = false
                    )
                } else {
                    showSnackBar(
                        title = getString(Res.string.error),
                        message = getString(mapErrorMessage(error)),
                        isSuccess = false
                    )
                }
            }
        )
    }

    private fun getAppSettings() {
        val currentAppLanguage = settingsRepository.getCurrentAppLanguage()
        updateState {
            state.value.copy(
                languageDialogUiState = LanguageDialogUiState(
                    selectedAppLanguage = currentAppLanguage,
                ),
                currentLanguage = currentAppLanguage
            )
        }
    }

    override fun onLanguageClicked() =
        updateState { it.copy(languageDialogUiState = it.languageDialogUiState.copy(isVisible = true)) }

    override fun onDismissLanguageDialog() {
        updateState {
            it.copy(
                languageDialogUiState = it.languageDialogUiState.copy(
                    isVisible = false,
                    selectedAppLanguage = state.value.currentLanguage
                )
            )
        }
    }

    override fun onConfirmLanguageSelection() {
        tryToExecute(
            callee = { settingsRepository.applyLanguage(state.value.languageDialogUiState.selectedAppLanguage) },
            onSuccess = { onLanguageConfirmationSuccess() },
            onError = ::onApplyLanguageError,
            dispatcher = dispatcher
        )
    }

    private suspend fun onApplyLanguageError(throwable: Throwable) {
        updateState { it.copy(isLoading = false) }
        showSnackBar(
            title = getString(Res.string.error),
            message = getString(mapErrorMessage(throwable)),
            isSuccess = false
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

    private fun mapErrorMessage(throwable: Throwable): StringResource {
        val errorState = handleDefaultException(throwable)
        return mapDefaultErrorToMessage(errorState)
    }

    private fun onLanguageConfirmationSuccess() {
        updateState {
            it.copy(
                languageDialogUiState = it.languageDialogUiState.copy(
                    isVisible = false,
                ),
                currentLanguage = state.value.languageDialogUiState.selectedAppLanguage
            )
        }
    }

    override fun onSelectLanguage(appLanguage: AppLanguage) {
        updateState {
            it.copy(
                languageDialogUiState = it.languageDialogUiState.copy(
                    selectedAppLanguage = appLanguage
                )
            )
        }
    }




    override fun onOptionSelected(option: ProfileOption) {
        if (state.value.isGuest && option != ProfileOption.LANGUAGE && option != ProfileOption.PRIVACY && option != ProfileOption.CONTACT && option != ProfileOption.SHARE_APP && option != ProfileOption.BRANCHES) {
            updateState { it.copy(showGuestDialog = true) }
            return
        }
        when (option) {
            ProfileOption.WALLET -> {}
            ProfileOption.POINTS -> { /* Navigate to Points History */
            }

            ProfileOption.ORDERS -> { /* Navigate to Orders */
                sendNewEffect(ProfileUiEffect.NavigateToOrders)
            }

            ProfileOption.LANGUAGE -> {
                 onLanguageClicked()
            }

            ProfileOption.AS_USUAL -> {}
            ProfileOption.BRANCHES -> {
                sendNewEffect(NavigateToBranches)
            }

            ProfileOption.CONTACT -> {
                sendNewEffect(ProfileUiEffect.NavigateToContactUs)
            }

            ProfileOption.PRIVACY -> {
                sendNewEffect(ProfileUiEffect.NavigateToPrivacyPolicy)
            }

            ProfileOption.UPDATE_USER -> {
                sendNewEffect(ProfileUiEffect.NavigateToUpdateUser)

            }

            ProfileOption.SHARE_APP -> {
                sendNewEffect(ProfileUiEffect.ShareApp)
            }
        }
    }

    override fun onLogout() {
        tryToExecute(
            callee = logoutUseCase::logout,
            onStart = { updateState { it.copy(isLoading = true,errorMessage = null) }},
            onSuccess = {
                updateState { it.copy(isLoading = false,errorMessage = null, isDialogVisible = false) }
                sendNewEffect(ProfileUiEffect.NavigateToSplash)
            },
            onError = ::onLogoutError
        )

    }
    override fun onDeleteAccount() {
        tryToExecute(
            callee = deleteAccountUseCase::deleteAccount, // Your actual use case method
            onStart = { updateState { it.copy(isLoading = true) }},
            onSuccess = {
                updateState { it.copy(isLoading = false, isDialogVisible = false) }
                sendNewEffect(ProfileUiEffect.NavigateToSplash)
            },
            onError = ::onLogoutError
        )
    }

    override fun toggleDialog(type: ProfileDialogType) {
        updateState { it.copy(activeDialog = type, isDialogVisible = type != ProfileDialogType.NONE) }
    }
    private fun onLogoutError(throwable: Throwable){
        updateState { it.copy(isDialogVisible = false, isLoading = false) }
        mapLogoutErrorMessage(throwable)
    }
    private fun mapLogoutErrorMessage(throwable: Throwable): StringResource {
        val errorState = handleAuthException(throwable)
        return mapAuthenticationErrorToMessage(errorState)
    }

    private fun showDialog(
        title: String? = null,
        message: String,
        durationMillis: Long = SNACK_BAR_DELAY,
    ) {
        updateState { oldState ->
            oldState.copy(
                dialogState = DialogState(
                    isVisible = true,
                    title = title,
                    message = message,
                )
            )
        }
    }

    private fun hideDialog() {
        updateState { oldState ->
            oldState.copy(dialogState = oldState.dialogState.copy(isVisible = false))
        }
    }
    override fun onBack() {
       sendNewEffect(ProfileUiEffect.NavigateBack)
    }

    override fun onClickLogin() {
        updateState { it.copy(showGuestDialog = false) }
        sendNewEffect(ProfileUiEffect.NavigateToLogin)
    }

    override fun onDismissGuestDialog() {
        updateState { it.copy(showGuestDialog = false) }
        sendNewEffect(ProfileUiEffect.NavigateBack)
    }

    override fun onOpenQRCodeBottomSheet() {
        updateState { it.copy(isQRCodeBottomSheetVisible = true) }

    }

    override fun onDismissQRCodeBottomSheet() {
        updateState { it.copy(isQRCodeBottomSheetVisible = false) }
    }
}
