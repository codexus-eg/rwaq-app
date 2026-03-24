package com.khater.rwaq.presentation.screens.contactUsScreen

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key.Companion.R
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.adamglin.composeshadow.dropShadow
import com.khater.rwaq.designSystem.component.button.PrimaryButton
import com.khater.rwaq.designSystem.component.scaffold.HomeScaffold
import com.khater.rwaq.designSystem.component.textField.MultiLineTextField
import com.khater.rwaq.designSystem.component.textField.TextField
import com.khater.rwaq.designSystem.theme.theme.Theme
import com.khater.rwaq.presentation.composables.EventHandler
import com.khater.rwaq.presentation.composables.RwaqBackButton
import com.khater.rwaq.presentation.composables.RwaqTopBar
import com.khater.rwaq.presentation.screens.contactUsScreen.uiState.ContactUsInteractionListener
import com.khater.rwaq.presentation.screens.contactUsScreen.uiState.ContactUsUiEffect
import com.khater.rwaq.presentation.screens.contactUsScreen.uiState.ContactUsUiState
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import rwaq.composeapp.generated.resources.Res
import rwaq.composeapp.generated.resources.branches
import rwaq.composeapp.generated.resources.cart
import rwaq.composeapp.generated.resources.contact_us
import rwaq.composeapp.generated.resources.contact_us_title
import rwaq.composeapp.generated.resources.email
import rwaq.composeapp.generated.resources.email_placeholder
import rwaq.composeapp.generated.resources.message
import rwaq.composeapp.generated.resources.message_placeholder
import rwaq.composeapp.generated.resources.name
import rwaq.composeapp.generated.resources.phone_number
import rwaq.composeapp.generated.resources.send
import rwaq.composeapp.generated.resources.username

@Composable
fun ContactUsScreen(viewModel: ContactUsViewModel = koinViewModel()) {
    val state = viewModel.state.collectAsStateWithLifecycle().value
    EventHandler(viewModel.effect) { effect, controller ->
        when (effect) {
            ContactUsUiEffect.NavigateBack -> controller.navigateUp()
        }
    }
    ContactUsContent(
        state = state,
        listener = viewModel
    )
}

@Composable
fun ContactUsContent(state: ContactUsUiState, listener: ContactUsInteractionListener) {
    Box(
        modifier = Modifier.fillMaxSize().imePadding(),
        contentAlignment = Alignment.Center
    ) {

        HomeScaffold(
            hasStatusBarColor = true,
            snackBarState = state.snackBar,
            topBar = {
                RwaqTopBar(
                    containerColor = Color(0xFFFBF7F0),
                    leadingContent = {
                        RwaqBackButton(
                            onClick = listener::onBackClicked,
                            hasDropShadow = true,
                            tint = Theme.colorScheme.shadePrimary
                        )
                    },
                    middleContent = {
                        com.khater.rwaq.designSystem.component.text.Text(
                            text = stringResource(Res.string.contact_us),
                            color = Theme.colorScheme.shadePrimary,
                            style = Theme.typography.headline.medium,
                            modifier = Modifier.offset(x = (-22).dp)
                        )
                    },
                    isCenterAligned = true,
                )
            },
        ) {
//        HomeScaffold(
//            hasStatusBarColor = true,
//            snackBarState = state.snackBar,
//            topBar = {
//                RwaqTopBar(
//                    containerColor = Theme.colorScheme.brand.brand,
//                    leadingContent = {
//                        RwaqBackButton(
//                            onClick = listener::onBackClicked,
//                            hasDropShadow = false,
//                            tint = Theme.colorScheme.brand.onBrand
//
//                        )
//                    },
//                    middleContent = {
//                        Text(
//                            text = stringResource(Res.string.contact_us),
//                            color = Theme.colorScheme.brand.onBrand,
//                            style = Theme.typography.headline.medium,
//                            modifier = Modifier.offset(x = (-22).dp)
//                        )
//                    },
//                    isCenterAligned = true,
//                )
//            }
//        ) {
            Column(
                modifier = Modifier.fillMaxSize().padding(12.dp)
                    .verticalScroll(rememberScrollState())
            ) {

                Text(
                    text = stringResource(Res.string.contact_us_title),
                    style = Theme.typography.title.medium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // 1. Username (Read Only)
                TextField(
                    value = state.username,
                    onValueChanged = listener::onUsernameChanged,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    isError = state.usernameError != null, errorMessage = state.usernameError?.let {
                        stringResource(it)
                    },
                    hint = stringResource(Res.string.username),

                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                        keyboardType = KeyboardType.Text
                    ),
                     textFieldHeight = 62.dp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 2. Phone Number (Read Only)
                TextField(
                    value = state.phoneNumber,
                    onValueChanged = {},
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    readOnly = true,
                    hint = stringResource(Res.string.phone_number),
                    textFieldHeight = 62.dp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 3. Email (Editable + Validated)
                TextField(
                    value = state.email,
                    onValueChanged = listener::onEmailChanged,
                    hint = stringResource(Res.string.email),
                    isError = state.emailError != null, errorMessage = state.emailError?.let {
                        stringResource(it)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                        keyboardType = KeyboardType.Email
                    ),
                    shape = RoundedCornerShape(12.dp),
                    textFieldHeight = 62.dp

                )

                Spacer(modifier = Modifier.height(16.dp))

                // 4. Message (Editable + Validated)
                MultiLineTextField(
                    value = state.message,
                    onValueChanged = listener::onMessageChanged,
                    hint = stringResource(Res.string.message_placeholder),

                    isError = state.messageError != null,
                    errorMessage = state.messageError?.let {
                        stringResource(it)
                    },

                    modifier = Modifier
                        .fillMaxWidth(),
                    textFieldHeight = 120.dp,
                    minLines = 1,
                    maxLines = 10
                )

                Spacer(modifier = Modifier.height(24.dp))

                Spacer(modifier = Modifier.weight(1f))
                PrimaryButton(
                    text = stringResource(Res.string.send),
                    onClick = listener::onSubmitClicked,
                    isLoading = state.isLoading,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}