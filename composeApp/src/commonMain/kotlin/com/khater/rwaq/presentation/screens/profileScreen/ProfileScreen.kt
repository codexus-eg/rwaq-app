package com.khater.rwaq.presentation.screens.profileScreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.adamglin.composeshadow.dropShadow
import com.khater.rwaq.designSystem.component.button.PrimaryButton
import com.khater.rwaq.designSystem.component.dialog.BasicDialog
import com.khater.rwaq.designSystem.component.dialog.Dialog
import com.khater.rwaq.designSystem.component.scaffold.RwaqScaffold
import com.khater.rwaq.designSystem.theme.theme.Theme
import com.khater.rwaq.presentation.composables.EventHandler
import com.khater.rwaq.presentation.composables.RwaqBackButton
import com.khater.rwaq.presentation.composables.RwaqTopBar
import com.khater.rwaq.presentation.navigation.Screen
import com.khater.rwaq.presentation.navigation.Screen.*
import com.khater.rwaq.presentation.screens.profileScreen.composables.CaptureComposable
import com.khater.rwaq.presentation.screens.profileScreen.composables.LanguageBottomSheet
import com.khater.rwaq.presentation.screens.profileScreen.composables.MyGigstersPromoScreen
import com.khater.rwaq.presentation.screens.profileScreen.composables.ProfileSection
import com.khater.rwaq.presentation.screens.profileScreen.composables.SettingItemCard
import com.khater.rwaq.presentation.screens.profileScreen.composables.SettingsGroup
import com.khater.rwaq.presentation.screens.profileScreen.composables.ShareWithQRCode
import com.khater.rwaq.presentation.screens.profileScreen.composables.WalletSection
import com.khater.rwaq.presentation.screens.profileScreen.composables.confirmationDialog
import com.khater.rwaq.presentation.screens.profileScreen.uiState.ProfileDialogType
import com.khater.rwaq.presentation.screens.profileScreen.uiState.ProfileInteractionListener
import com.khater.rwaq.presentation.screens.profileScreen.uiState.ProfileOption
import com.khater.rwaq.presentation.screens.profileScreen.uiState.ProfileUiEffect
import com.khater.rwaq.presentation.screens.profileScreen.uiState.ProfileUiState
import com.khater.rwaq.presentation.util.AppStoreManager
import com.khater.rwaq.presentation.util.Dimensions.BOTTOM_NAV_HEIGHT
import com.khater.rwaq.presentation.util.Dimensions.PADDING_BOTTOM_WITH_NAV_VISIBLE
import com.khater.rwaq.presentation.util.generateReferralLink
import com.khater.rwaq.presentation.util.shareImageBitmap
import com.khater.rwaq.presentation.util.storesAppLink
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

import org.koin.compose.viewmodel.koinViewModel
import rwaq.composeapp.generated.resources.Res
import rwaq.composeapp.generated.resources.are_you_sure_delete_account
import rwaq.composeapp.generated.resources.are_you_sure_you_want_to_logout
import rwaq.composeapp.generated.resources.delete
import rwaq.composeapp.generated.resources.delete_account
import rwaq.composeapp.generated.resources.logout
import rwaq.composeapp.generated.resources.profile_image
import rwaq.composeapp.generated.resources.profile_screen
import rwaq.composeapp.generated.resources.welcome
import rwaq.composeapp.generated.resources.login
import rwaq.composeapp.generated.resources.rwaq_logo

@Composable
fun ProfileScreen(appStoreManager: AppStoreManager = koinInject(),viewModel: ProfileViewModel = koinViewModel()) {
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.checkAuthentication()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
    val state = viewModel.state.collectAsStateWithLifecycle().value
    EventHandler(viewModel.effect) { effect, controller ->
        when (effect) {
            ProfileUiEffect.NavigateToSplash -> {
                controller.navigate(SplashScreen) {
                    popUpTo(0)
                }
            }

            ProfileUiEffect.NavigateBack -> {
                controller.navigateUp()
            }

            ProfileUiEffect.NavigateToBranches -> {
                controller.navigate(BranchScreen(true))
            }

            ProfileUiEffect.NavigateToContactUs -> {
                controller.navigate(ContactUsScreen)
            }

            ProfileUiEffect.NavigateToPrivacyPolicy -> {
                controller.navigate(PrivacyPolicyScreen)
            }

            ProfileUiEffect.NavigateToOrders -> {
                controller.navigate(MyOrderScreen)
            }

            ProfileUiEffect.NavigateToUpdateUser ->{
                controller.navigate(UpdateUserScreen)
            }

            ProfileUiEffect.ShareApp -> {
               viewModel.onOpenQRCodeBottomSheet()
            }

            ProfileUiEffect.NavigateToLogin -> {
                controller.navigate(Screen.LoginScreen)
            }
        }
    }

    ProfileContent(
        state = state,
        interaction = viewModel as ProfileInteractionListener
    )

}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ProfileContent(state: ProfileUiState, interaction: ProfileInteractionListener) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        RwaqScaffold(
            hasStatusBarColor = true,

            topBar = {
                RwaqTopBar(
                    containerColor = Color(0xFFFBF7F0),
                    leadingContent = {
                        RwaqBackButton(
                            onClick = interaction::onBack,
                            hasDropShadow = true,
                            tint = Theme.colorScheme.shadePrimary
                        )
                    },
                    middleContent = {
                        com.khater.rwaq.designSystem.component.text.Text(
                            text = stringResource(Res.string.profile_screen),
                            color = Theme.colorScheme.shadePrimary,
                            style = Theme.typography.headline.medium,
                            modifier = Modifier.offset(x = (-22).dp)
                        )
                    },
                    isCenterAligned = true,
                )
            },

            overlays = {
                val dialogTitle = when (state.activeDialog) {
                    ProfileDialogType.DELETE_ACCOUNT ->  Res.string.are_you_sure_delete_account  // Add to strings
                    else ->  Res.string.are_you_sure_you_want_to_logout
                }

                val confirmText = when (state.activeDialog) {
                    ProfileDialogType.DELETE_ACCOUNT ->  Res.string.delete_account
                    else -> Res.string.logout
                }

                confirmationDialog(
                    isVisible = state.isDialogVisible,
                    isLoading = state.isLoading,
                    title = dialogTitle,
                    confirmButtonText = confirmText,
                    onConfirmClick = {
                        if (state.activeDialog == ProfileDialogType.DELETE_ACCOUNT) interaction.onDeleteAccount()
                        else interaction.onLogout()
                    },
                    onDismissDialog = { interaction.toggleDialog(ProfileDialogType.NONE) }
                )
                dialog(state.showGuestDialog) {
                    BasicDialog(
                        isVisible = state.showGuestDialog,
                        onDismiss = { },
                        onCancelClick = { },
                        hasDismissButton = false,
                        dismissOnBackPress = false,
                        dismissOnClickOutside = false,
                        actionButtons = {
                            PrimaryButton(
                                text = stringResource(Res.string.login),
                                onClick = interaction::onClickLogin,
                                modifier = Modifier.fillMaxWidth().padding(top = Theme.spacing._16)
                            )
                        }
                    ) {
                        Dialog(
                            title = stringResource(Res.string.login),
                            message = stringResource(Res.string.welcome),
                            icon = painterResource(Res.drawable.rwaq_logo)
                        )
                    }
                }
//                logoutDialog(
//                    showLogoutDialog = state.isDialogVisible,
//                    isLoading = state.isLoading,
//                    onDismissDialog = interaction::toggleDialog,
//                    onLogoutClick = interaction::onLogout,
//                )
            }
        ) {



            Column(modifier = Modifier.padding(all = 12.dp).verticalScroll(rememberScrollState())) {

                ProfileSection(
                    painter = painterResource(Res.drawable.profile_image),
                    username = if (state.isGuest) stringResource(Res.string.welcome) else state.userName,
                    phoneNumber = state.phoneNumber,
                    isVipUser = state.isVipUser,
                    modifier = Modifier.padding(vertical = Theme.spacing._12)
                )

                if (!state.isGuest) {
                    WalletSection(
                        modifier = Modifier.fillMaxWidth(),
                        balance = state.balance,
                        points = state.points,
                        onWalletClick = { interaction.onOptionSelected(ProfileOption.WALLET) },
                        onPointsClick = { interaction.onOptionSelected(ProfileOption.POINTS) }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Menu Section
                SettingsGroup(
                    items = state.menuSection,
                    onItemClick = { interaction.onOptionSelected(it.option) }
                )
                Spacer(modifier = Modifier.height(16.dp))
//                SettingItemCard(
//                    settingName = stringResource(Res.string.logout),
//                    textColor = Theme.colorScheme.error,
//                    isLogout = true,
//                    settingSubName = null,
//                    modifier = Modifier
//                        .dropShadow(
//                            shape = RoundedCornerShape(16.dp),
//                            color = Color(0xFF001E14).copy(0.04f),
//                            blur = 20.dp,
//                            offsetY = 2.dp,
//                            offsetX = 0.dp
//                        ),
//                    onClick = interaction::toggleDialog
//                )
                if (!state.isGuest) {
                    SettingItemCard(
                        settingName = stringResource(Res.string.logout),
                        textColor = Theme.colorScheme.error,
                        isLogout = true,
                        settingSubName = null,
                        modifier = Modifier
                            .dropShadow(
                                shape = RoundedCornerShape(16.dp),
                                color = Color(0xFF001E14).copy(0.04f),
                                blur = 20.dp,
                                offsetY = 2.dp,
                                offsetX = 0.dp
                            ),
                        onClick = { interaction.toggleDialog(ProfileDialogType.LOGOUT) }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    SettingItemCard(
                        settingName = stringResource(Res.string.delete_account), // "Delete Account"
                        textColor = Theme.colorScheme.error.copy(alpha = 0.7f),
                        isLogout = true,
                        modifier = Modifier.padding(bottom = PADDING_BOTTOM_WITH_NAV_VISIBLE.dp)
                            .dropShadow(
                                shape = RoundedCornerShape(16.dp),
                                color = Color(0xFF001E14).copy(0.04f),
                                blur = 20.dp,
                                offsetY = 2.dp,
                                offsetX = 0.dp
                            ),
                        settingSubName = null,
                        icon = Res.drawable.delete,
                        onClick = { interaction.toggleDialog(ProfileDialogType.DELETE_ACCOUNT) }
                    )
                } else {
                    PrimaryButton(
                        text = stringResource(Res.string.login),
                        onClick = interaction::onClickLogin,
                        modifier = Modifier.fillMaxWidth().padding(bottom = PADDING_BOTTOM_WITH_NAV_VISIBLE.dp)
                    )
                }
               // here i need to add delete account
            }
        }

        BackHandler(
            enabled = state.languageDialogUiState.isVisible || state.isQRCodeBottomSheetVisible
        ) {
            when {
                state.languageDialogUiState.isVisible -> interaction.onDismissLanguageDialog()
                state.isQRCodeBottomSheetVisible -> interaction.onDismissQRCodeBottomSheet()
            }
        }

        AnimatedVisibility(
            visible = state.isQRCodeBottomSheetVisible,
            enter = slideInVertically(initialOffsetY = { it }),
            exit = slideOutVertically(targetOffsetY = { it }),
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = BOTTOM_NAV_HEIGHT.dp)
        ){
            val referralLink = generateReferralLink(storesAppLink, state.referCode)
            CaptureComposable(
                onCaptured = { bitmap ->

                    shareImageBitmap(bitmap,"The best coffee app ever! Smarter shop. Better mode $referralLink")

                }
            ) {
                MyGigstersPromoScreen(referralLink,interaction::onDismissQRCodeBottomSheet)
            }
        }
        AnimatedVisibility(
            visible = state.languageDialogUiState.isVisible,
            enter = slideInVertically(initialOffsetY = { it }),
            exit = slideOutVertically(targetOffsetY = { it }),
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = BOTTOM_NAV_HEIGHT.dp)
        ) {
            LanguageBottomSheet(
                appLanguages = state.languageDialogUiState.options,
                currentAppLanguage = state.currentLanguage,
                selectedAppLanguage = state.languageDialogUiState.selectedAppLanguage,
                onDismissRequest = interaction::onDismissLanguageDialog,
                onConfirmLanguageSelection = interaction::onConfirmLanguageSelection,
                onLanguageChanged = interaction::onSelectLanguage
            )
        }
    }
}