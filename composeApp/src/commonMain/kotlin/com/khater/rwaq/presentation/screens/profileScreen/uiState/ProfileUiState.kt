package com.khater.rwaq.presentation.screens.profileScreen.uiState

import com.khater.rwaq.domain.util.AppLanguage
import com.khater.rwaq.presentation.model.DialogState
import com.khater.rwaq.presentation.model.SnackBarState
import org.jetbrains.compose.resources.StringResource
import rwaq.composeapp.generated.resources.Res
import rwaq.composeapp.generated.resources.as_usual
import rwaq.composeapp.generated.resources.branches
import rwaq.composeapp.generated.resources.contact_us
import rwaq.composeapp.generated.resources.language
import rwaq.composeapp.generated.resources.my_orders
import rwaq.composeapp.generated.resources.privacy_policy
import rwaq.composeapp.generated.resources.share_app
import rwaq.composeapp.generated.resources.update_user

// 1. Enum to identify clicks for every item in your lists
enum class ProfileOption {
    WALLET, POINTS, // Dynamic API items
    ORDERS, AS_USUAL, BRANCHES, CONTACT, LANGUAGE, PRIVACY ,UPDATE_USER,SHARE_APP// Static items
}

// 2. Update your Data Class to include the click option
data class SettingItem(
    val titleRes: StringResource, // <--- Using Res here for Name
    val settingSubName: String? = null,
    val option: ProfileOption,
)

// 3. The UI State holding the API data
data class ProfileUiState(
    val isLoading: Boolean = false,
    val userName: String = "",
    val phoneNumber: String = "",
    val userId: String = "",
    // The user's own referral code, used to build the shareable invite link/QR.
    val referCode: String = "",
    val isVipUser: Boolean = false,
    val snackBar: SnackBarState = SnackBarState(),
    val error: String? = null,
    val errorMessage: String? = null,
    val activeDialog: ProfileDialogType = ProfileDialogType.NONE,
    val dialogState: DialogState = DialogState(),
    val isDialogVisible: Boolean = false,
    val balance: Double = 0.00, // Using Double for currency (e.g., 10.50)
    val points: Int = 0,
    val isGuest: Boolean = false,
    val showGuestDialog: Boolean = false,
    // Menu Section defaults (Using Res.string.orders, etc.)
    val menuSection: List<SettingItem> = listOf(
        SettingItem(Res.string.my_orders, option = ProfileOption.ORDERS),
        SettingItem(Res.string.branches, option = ProfileOption.BRANCHES),
        SettingItem(Res.string.contact_us, option = ProfileOption.CONTACT),
        SettingItem(Res.string.share_app, option = ProfileOption.SHARE_APP),
        SettingItem(Res.string.language, option = ProfileOption.LANGUAGE),
        SettingItem(Res.string.privacy_policy, option = ProfileOption.PRIVACY),
        SettingItem(Res.string.update_user, option = ProfileOption.UPDATE_USER),


    ),
    val currentLanguage: AppLanguage = AppLanguage.DEFAULT,
    val languageDialogUiState: LanguageDialogUiState = LanguageDialogUiState(),
    val isQRCodeBottomSheetVisible: Boolean = false
)

data class LanguageDialogUiState(
    val isVisible: Boolean = false,
    val selectedAppLanguage: AppLanguage = AppLanguage.DEFAULT,
    val options: List<AppLanguage> = AppLanguage.entries.filterNot { it == AppLanguage.DEFAULT },
)

enum class ProfileDialogType {
    LOGOUT, DELETE_ACCOUNT, NONE
}