package com.khater.rwaq.presentation.screens.contactUsScreen.uiState

import com.khater.rwaq.presentation.model.SnackBarState
import org.jetbrains.compose.resources.StringResource

data class ContactUsUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val snackBar: SnackBarState = SnackBarState(),
    val username: String = "",
    val phoneNumber: String = "",
    val email: String = "",
    val message: String = "",

    val emailError: StringResource? = null,
    val usernameError: StringResource? = null,
    val messageError: StringResource? = null
)
