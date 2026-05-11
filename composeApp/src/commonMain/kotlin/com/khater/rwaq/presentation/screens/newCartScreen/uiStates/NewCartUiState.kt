package com.khater.rwaq.presentation.screens.newCartScreen.uiStates

import com.khater.rwaq.domain.entities.branch.Branch
import com.khater.rwaq.domain.entities.car.Car
import com.khater.rwaq.domain.entities.cart.Cart
import com.khater.rwaq.presentation.model.SnackBarState
import com.khater.rwaq.presentation.screens.branchScreen.uiState.BranchWorkTimeUiState
import com.khater.rwaq.presentation.screens.branchScreen.uiState.CarBrandUiState
import com.khater.rwaq.presentation.screens.cartScreen.uiStates.CarColor
import com.khater.rwaq.presentation.screens.cartScreen.uiStates.OrderLocation

data class NewCartUiState(
    val cart: Cart? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isCheckoutLoading: Boolean = false,

    val snackBar: SnackBarState = SnackBarState(),
    val orderLocation: OrderLocation = OrderLocation(),

    // Branches & Selection
    val allBranches: List<Branch> = emptyList(),
    val driveThruBranches: List<Branch> = emptyList(),
    val branchesErrorMessage: String? = null,
    val isLocationEnabled: Boolean = false,
    val isLocationObtained: Boolean = false,
    // Receipt Method
    val isDriveThru: Boolean = false,
    val selectedPickupBranch: Branch? = null,
    val selectedDriveThruBranch: Branch? = null,

    // Cars
    val cars: List<Car> = emptyList(),
    val selectedCar: Car = Car("", "", "", androidx.compose.ui.graphics.Color.Transparent, "", ""),

    // Bottom Sheets & Overlays
    val isSelectCarBottomSheetVisible: Boolean = false,
    val isWorkTimeOverlayVisible: Boolean = false,
    val isCarDetailsBottomSheetVisible: Boolean = false,
    val selectedWorkTime: List<BranchWorkTimeUiState> = emptyList(),

    // Add Car Wizard
    val addCarStep: Int = 1, // 1: Brand, 2: Color, 3: Number
    val carBrands: List<CarBrandUiState> = listOf(
        CarBrandUiState("1", "Audi", "https://raw.githubusercontent.com/khaatteerr/super-survey-images/refs/heads/main/rwaq_images/audi.png"),
        CarBrandUiState("2", "BMW", "https://raw.githubusercontent.com/khaatteerr/super-survey-images/refs/heads/main/rwaq_images/bmw.png"),
        CarBrandUiState("3", "BYD", "https://raw.githubusercontent.com/khaatteerr/super-survey-images/refs/heads/main/rwaq_images/BYD.png"),
        CarBrandUiState("4", "Mercedes", "https://raw.githubusercontent.com/khaatteerr/super-survey-images/refs/heads/main/rwaq_images/mercedes.png"),
        CarBrandUiState("5", "Ford", "https://raw.githubusercontent.com/khaatteerr/super-survey-images/refs/heads/main/rwaq_images/ford.png"),
        CarBrandUiState("6", "Honda", "https://raw.githubusercontent.com/khaatteerr/super-survey-images/refs/heads/main/rwaq_images/honda.png"),
        CarBrandUiState("7", "Chevrolet", "https://raw.githubusercontent.com/khaatteerr/super-survey-images/refs/heads/main/rwaq_images/chevrolet.png"),
        CarBrandUiState("8", "Nissan", "https://raw.githubusercontent.com/khaatteerr/super-survey-images/refs/heads/main/rwaq_images/nissan.png"),
        CarBrandUiState("9", "Ferrari", "https://raw.githubusercontent.com/khaatteerr/super-survey-images/refs/heads/main/rwaq_images/ferrari.png"),
        CarBrandUiState("10", "MG", "https://raw.githubusercontent.com/khaatteerr/super-survey-images/refs/heads/main/rwaq_images/mg.png"),
        CarBrandUiState("11", "Tesla", "https://raw.githubusercontent.com/khaatteerr/super-survey-images/refs/heads/main/rwaq_images/tesla.png"),
        CarBrandUiState("12", "Toyota", "https://raw.githubusercontent.com/khaatteerr/super-survey-images/refs/heads/main/rwaq_images/toyota.png"),
        CarBrandUiState("13", "Volkswagen", "https://raw.githubusercontent.com/khaatteerr/super-survey-images/refs/heads/main/rwaq_images/volkswagen.png"),
        CarBrandUiState("14", "Genesis", "https://raw.githubusercontent.com/khaatteerr/super-survey-images/refs/heads/main/rwaq_images/Genesis.png"),
        CarBrandUiState("15", "JAC", "https://raw.githubusercontent.com/khaatteerr/super-survey-images/refs/heads/main/rwaq_images/JAC.jpg"),
        CarBrandUiState("16", "Mitsubishi", "https://raw.githubusercontent.com/khaatteerr/super-survey-images/refs/heads/main/rwaq_images/Mitsubishi.png"),
        CarBrandUiState("17", "Bentley", "https://raw.githubusercontent.com/khaatteerr/super-survey-images/refs/heads/main/rwaq_images/bentley.png"),
        CarBrandUiState("18", "Daihatsu", "https://raw.githubusercontent.com/khaatteerr/super-survey-images/refs/heads/main/rwaq_images/daihatsu.png"),
        CarBrandUiState("19", "GAC", "https://raw.githubusercontent.com/khaatteerr/super-survey-images/refs/heads/main/rwaq_images/gac.png"),
        CarBrandUiState("20", "Geely", "https://raw.githubusercontent.com/khaatteerr/super-survey-images/refs/heads/main/rwaq_images/geely.png"),
        CarBrandUiState("21", "Hyundai", "https://raw.githubusercontent.com/khaatteerr/super-survey-images/refs/heads/main/rwaq_images/hyundai.png"),
        CarBrandUiState("22", "Jetour", "https://raw.githubusercontent.com/khaatteerr/super-survey-images/refs/heads/main/rwaq_images/jetour.png"),
        CarBrandUiState("22", "kia", "https://raw.githubusercontent.com/khaatteerr/super-survey-images/refs/heads/main/rwaq_images/kia.png"),
        CarBrandUiState("23", "Lucid", "https://raw.githubusercontent.com/khaatteerr/super-survey-images/refs/heads/main/rwaq_images/lucid.png"),
    ),
    val carColors: List<CarColor> = listOf(
        CarColor("أزرق", androidx.compose.ui.graphics.Color(0xFF0066FF).copy(alpha = 0.7f)),
        CarColor("أبيض", androidx.compose.ui.graphics.Color.White),
        CarColor("ذهبي", androidx.compose.ui.graphics.Color(0xFFD4AF37).copy(alpha = 0.7f)),
        CarColor("أسود", androidx.compose.ui.graphics.Color.Black),
        CarColor("سماوي", androidx.compose.ui.graphics.Color(0xFF00CCFF).copy(alpha = 0.7f)),
        CarColor("أخضر", androidx.compose.ui.graphics.Color(0xFF8BC34A).copy(alpha = 0.7f)),
        CarColor("بني", androidx.compose.ui.graphics.Color(0xFF795548).copy(alpha = 0.7f)),
        CarColor("رمادي", androidx.compose.ui.graphics.Color.Gray),
        CarColor("رمادي فاتح", androidx.compose.ui.graphics.Color.LightGray),
        CarColor("أحمر", androidx.compose.ui.graphics.Color.Red.copy(alpha = 0.7f)),
        CarColor("وردي", androidx.compose.ui.graphics.Color(0xFFFFC0CB).copy(alpha = 0.7f)),
        CarColor("برتقالي", androidx.compose.ui.graphics.Color(0xFFFF9800).copy(alpha = 0.7f)),
        CarColor("أصفر", androidx.compose.ui.graphics.Color.Yellow.copy(alpha = 0.7f))
    ),
    val selectedCarBrand: CarBrandUiState? = null,
    val selectedCarColor: CarColor? = null,
    val newCarNumber: String = "",

    // Notes & Payment
    val orderNotes: String = "",
    val selectedPaymentMethod: String = "ONLINE",
    val showApplePayOption: Boolean = false,
    val isApplePaySupported: Boolean = false,
    val isApplePaySelected: Boolean = false,

    // Pricing
    val subTotal: Double = 0.0,
    val rewardDiscount: Double = 0.0,
    val cartTotal: Double = 0.0,
    val userPoints: Int? = null,

    // Loading states for actions
    val isUpdatingMetadata: Boolean = false,
    val isSendingOrderLoading: Boolean = false,
    val updatingItemId: String? = null,
    val isGuest: Boolean = false,
    val showGuestDialog: Boolean = false,
    val isPaymentSuccessDialogVisible: Boolean = false
)
