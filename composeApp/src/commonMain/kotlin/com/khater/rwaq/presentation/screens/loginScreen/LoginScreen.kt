package com.khater.rwaq.presentation.screens.loginScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.khater.rwaq.designSystem.component.button.PrimaryButton
import com.khater.rwaq.designSystem.component.button.TextButton
import com.khater.rwaq.designSystem.component.scaffold.AuthScaffold
import com.khater.rwaq.designSystem.theme.theme.Theme
import com.khater.rwaq.presentation.composables.AuthHeader
import com.khater.rwaq.presentation.composables.EventHandler
import com.khater.rwaq.presentation.composables.PhoneNumberInputField
import com.khater.rwaq.presentation.composables.RwaqBackButton
import com.khater.rwaq.presentation.composables.RwaqTopBar
import com.khater.rwaq.presentation.navigation.Screen

import com.khater.rwaq.presentation.screens.loginScreen.uiState.LoginInteractionListener
import com.khater.rwaq.presentation.screens.loginScreen.uiState.LoginScreenState
import com.khater.rwaq.presentation.screens.loginScreen.uiState.LoginUiEffect
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import rwaq.composeapp.generated.resources.Res
import rwaq.composeapp.generated.resources.coffee_no_background
import rwaq.composeapp.generated.resources.forget_password
import rwaq.composeapp.generated.resources.login
import rwaq.composeapp.generated.resources.rwaq_logo
import rwaq.composeapp.generated.resources.welcome
import rwaq.composeapp.generated.resources.continue_as_guest

@OptIn(KoinExperimentalAPI::class)
@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel = koinViewModel(),
) {
    val state = loginViewModel.state.collectAsStateWithLifecycle().value

    EventHandler(loginViewModel.effect) { effect, navController ->

        when (effect) {
            is LoginUiEffect.NavigateToOtpScreen -> {
                navController.navigate(Screen.OTPScreen(phoneNumber = effect.phoneNumber,isRegister = true))
            }

            LoginUiEffect.NavigateToForgotPassword -> {
                navController.navigate(Screen.ForgetPasswordScreen)
            }

            LoginUiEffect.NavigateBack -> {
                navController.navigateUp()
            }

            LoginUiEffect.NavigateToHomeScreen -> {
                navController.navigate(Screen.HomeScreen) {
                    popUpTo(Screen.LoginScreen) { inclusive = true }
                }
            }
        }
    }

    LoginScreenContent(
        state = state,
        interactionListener = loginViewModel as LoginInteractionListener
    )
}

@Composable
fun LoginScreenContent(
    state: LoginScreenState,
    interactionListener: LoginInteractionListener,
) {
    Box(modifier = Modifier.fillMaxSize().background(Theme.colorScheme.brand.onBrand)){
        Image(
            painter = painterResource(Res.drawable.coffee_no_background),
            contentDescription = "RWAQ LOGO",
            modifier = Modifier.fillMaxWidth().height(200.dp),
            alignment = Alignment.TopCenter,
            contentScale = ContentScale.Crop
        )
        Image(
            painter = painterResource(Res.drawable.rwaq_logo),
            contentDescription = "RWAQ LOGO",
            modifier = Modifier.statusBarsPadding().padding(end = 16.dp) .size(100.dp).align(Alignment.TopEnd),
            alignment = Alignment.TopEnd,
            contentScale = ContentScale.Inside
        )
        AuthScaffold(
            snackBarState = state.snackBar,
            modifier = Modifier.padding(horizontal = Theme.spacing._16),
            topBar = {
                RwaqTopBar(
                    modifier = Modifier.padding(bottom = Theme.spacing._8),
                    leadingContent = {
                        RwaqBackButton(
                            onClick = interactionListener::onClickBack
                        )
                    }
                )

            }) {

            AuthHeader(
                title = stringResource(Res.string.login),
                body = stringResource(Res.string.welcome),
                modifier = Modifier.padding(bottom = Theme.spacing._24)
            )
            PhoneNumberInputField(
                phoneNumber = state.phoneNumber,
                onPhoneNumberChange = interactionListener::onPhoneNumberChanged,
                isError = state.phoneNumberError != null, errorMessage = state.phoneNumberError?.let {
                    stringResource(it)
                }
            )
//            PasswordInputField(
//                modifier = Modifier.padding(top = Theme.spacing._12),
//                password = state.password,
//                isPasswordVisible = state.isPasswordVisible,
//                onPasswordVisibilityToggled = interactionListener::onPasswordVisibilityToggled,
//                onValueChange = interactionListener::onPasswordChanged,
//                isError = state.passwordError != null,
//                errorMessage = state.passwordError?.let {
//                    stringResource(it)
//                }
//            )
//            ForgetPasswordText(
//                onClick = interactionListener::onClickForgetPassword
//            )

            Spacer(modifier = Modifier.weight(1f))
            
            TextButton(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = stringResource(Res.string.continue_as_guest),
                onClick = interactionListener::onClickGuestMode,
                style = Theme.typography.title.medium,
                textDecoration = TextDecoration.Underline,
            )

            PrimaryButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = Theme.spacing._16).imePadding(),
                text = stringResource(Res.string.login),
                onClick = interactionListener::onClickLogin,
                isLoading = state.isLoginButtonLoading,
                isEnabled = state.isLoginButtonEnabled,
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 13.dp)
            )
        }

    }

}

@Composable
fun ForgetPasswordText(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = Theme.spacing._4),
        contentAlignment = Alignment.BottomStart
    ) {
        TextButton(
            text = stringResource(Res.string.forget_password),
            isEnabled = true,
            onClick = onClick,
            style = Theme.typography.title.medium,
            textDecoration = TextDecoration.Underline,
        )
    }
}
