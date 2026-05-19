package com.khater.rwaq.presentation

import co.touchlab.kermit.Logger
import com.khater.rwaq.domain.entities.appVersion.AppVersionCheck
import com.khater.rwaq.domain.useCases.appVersion.CheckAppVersionUseCase
import com.khater.rwaq.presentation.base.BaseViewModel
import org.jetbrains.compose.resources.getString
import rwaq.composeapp.generated.resources.Res
import rwaq.composeapp.generated.resources.app_update_default_message
import rwaq.composeapp.generated.resources.app_update_default_title

class AppViewModel(
    private val checkAppVersionUseCase: CheckAppVersionUseCase
) : BaseViewModel<AppUiState, AppUiEffect>(AppUiState()) {

    init {
        checkForAppUpdate()
    }

    private fun checkForAppUpdate() {
        tryToExecute(
            callee = { checkAppVersionUseCase() },
            onSuccess = { update ->
                update ?: return@tryToExecute
                Logger.i { "App update check success: $update" }

                if (!update.shouldShowUpdateSheet) return@tryToExecute
                val updateUiState = update.toUiState()

                Logger.i { "App update check success: $updateUiState" }
                updateState {
                    it.copy(appUpdateSheet = updateUiState)
                }
            },
            onError = { error ->
                Logger.i { "App update check failed: $error" }
            }
        )
    }

    private suspend fun AppVersionCheck.toUiState(): AppUpdateUiState =
        AppUpdateUiState(
            title = title.ifBlank { getString(Res.string.app_update_default_title) },
            message = message.ifBlank { getString(Res.string.app_update_default_message) },
            releaseNotes = releaseNotes,
            latestVersion = latestVersion,
            updateUrl = updateUrl,
            isForceUpdate = forceUpdate || isUpdateRequired
        )

    fun onDismissAppUpdate() {
        if (state.value.appUpdateSheet?.isForceUpdate == true) return
        updateState { it.copy(appUpdateSheet = null) }
    }

    fun onUpdateAppClicked() {
        val updateUrl = state.value.appUpdateSheet?.updateUrl.orEmpty()
        if (updateUrl.isNotBlank()) {
            sendNewEffect(AppUiEffect.OpenAppUpdateUrl(updateUrl))
        }
    }
}

data class AppUiState(
    val appUpdateSheet: AppUpdateUiState? = null
)

data class AppUpdateUiState(
    val title: String,
    val message: String,
    val releaseNotes: String,
    val latestVersion: String,
    val updateUrl: String,
    val isForceUpdate: Boolean
)

sealed interface AppUiEffect {
    data class OpenAppUpdateUrl(val url: String) : AppUiEffect
}
