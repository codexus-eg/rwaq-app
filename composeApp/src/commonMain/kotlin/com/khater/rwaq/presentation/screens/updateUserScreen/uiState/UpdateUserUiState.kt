package com.khater.rwaq.presentation.screens.updateUserScreen.uiState

import com.khater.rwaq.presentation.model.SnackBarState
import org.jetbrains.compose.resources.StringResource

data class UpdateUserUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val snackBar: SnackBarState = SnackBarState(),
    val username: String = "",
    val email: String = "",
    val emailError: StringResource? = null,
    val usernameError: StringResource? = null,
)
