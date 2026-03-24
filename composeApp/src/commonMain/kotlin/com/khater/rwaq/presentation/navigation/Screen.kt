package com.khater.rwaq.presentation.navigation

 import com.khater.rwaq.presentation.screens.branchScreen.uiState.CarUiState
 import kotlinx.serialization.Serializable


sealed class Screen {
    @Serializable
    data object SplashScreen : Screen()

    @Serializable
    data class ProductScreen(
        val branchId: String,val isPickupFromBranch: Boolean, val carName: String, val carNumber: String,val branchName: String,val carColor: Int
    ) : Screen()
    @Serializable
    data object LoginScreen : Screen()
    @Serializable
    data object BranchesScreen : Screen()
    @Serializable
    data object ContactUsScreen : Screen()
    @Serializable
    data object RegisterScreen : Screen()

    @Serializable
    data class BranchScreen(val isPickupFormBranch: Boolean) : Screen()

    @Serializable
    data class OTPScreen(val phoneNumber: String, val isRegister: Boolean) : Screen()

    @Serializable
    data object ForgetPasswordScreen : Screen()

    @Serializable
    data object HomeScreen : Screen()
    @Serializable
    data object UpdateUserScreen : Screen()


    @Serializable
    data object PrivacyPolicyScreen : Screen()
    @Serializable
    data object CartScreen : Screen()

    @Serializable
    data object InvoiceScreen : Screen()
    @Serializable
    data object PaymentMethodScreen : Screen()

    @Serializable
    data class MakeOrderScreen(
        val productId: String,
        val productName: String,
        val productImageUrl: String,
        val productPrice: Int,
        val productOrigin: String,
        val productProductionCapacity: String,
        val productPowerSupply:Int,
        val productWeight: Double
    ) : Screen()

    @Serializable
    data object NotificationScreen : Screen()

    @Serializable
    data class ResetPasswordScreen(val phoneNumber: String,val otp:String) : Screen()

    @Serializable
    data object OnBoardingScreen : Screen()

    @Serializable
    data object ServicesScreen : Screen()

    @Serializable
    data object ProfileScreen : Screen()
    @Serializable
    data object RewardScreen : Screen()

    @Serializable
    data object MoreScreen : Screen()

    @Serializable
    data object MyOrderScreen : Screen()

    @Serializable
    data object SavedLocationsScreen : Screen()

    @Serializable
    data object AddServiceScreen : Screen()

    @Serializable
    data class AddLocationsScreen(
        val locationId: String? = null,
        val currentLocation: String? = null,
    ) : Screen()

    @Serializable
    data class OrderDetailsScreen(val orderId: String) : Screen()

    @Serializable
    data class ServiceDetailsScreen(val serviceId: String) : Screen()

    @Serializable
    data class ProductDetailsScreen(val productId: String) : Screen()

    @Serializable
    data class ProductFeatureScreen(val productId: String) : Screen()

    @Serializable
    data class InfoScreen(val infoType: String) : Screen()
    @Serializable
    data object MyDeviceScreen : Screen()
}