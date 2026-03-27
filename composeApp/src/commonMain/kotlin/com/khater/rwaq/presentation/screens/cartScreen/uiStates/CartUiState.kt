package com.khater.rwaq.presentation.screens.cartScreen.uiStates

import androidx.compose.ui.graphics.Color
import com.khater.rwaq.domain.entities.order.Order
import com.khater.rwaq.presentation.model.SnackBarState
import com.khater.rwaq.presentation.screens.branchScreen.uiState.AddCarStep
import com.khater.rwaq.presentation.screens.branchScreen.uiState.BranchUiState
import com.khater.rwaq.presentation.screens.branchScreen.uiState.BranchWorkTimeUiState
import com.khater.rwaq.presentation.screens.branchScreen.uiState.CarBrandUiState
import com.khater.rwaq.presentation.screens.branchScreen.uiState.CarUiState
import com.khater.rwaq.presentation.screens.branchScreen.uiState.LocationUiState
import kotlinx.serialization.Serializable

data class CartUiState(
    val isLoading: Boolean = false,
    val isSendingOrderLoading: Boolean = false,
    val sendingOrderError: String? = null,
    val orders: List<Order> = emptyList(),
    val cartTotal: Double = 0.0,
    val orderNotes: String = "",
    val promoCode: String = "",
    val selectedPaymentMethod: PaymentMethod = PaymentMethod.ONLINE,
    val subTotal: Double = 0.0,        // Gross amount (sum of all items)
    val rewardDiscount: Double = 0.0,
    val orderLocation: OrderLocation = OrderLocation(),
    val errorMessage: String? = null,
    val snackBar: SnackBarState = SnackBarState(),
    val isWorkTimeOverlayVisible: Boolean = false,
    val selectedLocation: LocationUiState = LocationUiState(),
    val selectedWorkTime: List<BranchWorkTimeUiState> = emptyList(),
    val isGuest: Boolean = false,
    val showGuestDialog: Boolean = false,

    // --- NEW FIELDS FOR BRANCH/CAR SELECTION ---
    val isCheckoutSheetVisible: Boolean = false, // To open the Branch Selection Sheet
    val isCarSheetVisible: Boolean = false,      // To open the Car Selection Sheet (Drive Thru)

    // --- RECEIVING METHOD STATE ---
    // Removed isCheckoutBottomSheetVisible
    val isDriveThru: Boolean = false, // false = Pickup, true = Drive Thru
    val branches: List<BranchUiState> = emptyList(),
    val selectedBranch: BranchUiState? = null,

    val allBranches: List<BranchUiState> = emptyList(), // For Pickup
    val driveThruBranches: List<BranchUiState> = emptyList(), // For
    val selectedPickupBranch: BranchUiState? = null,
    val selectedDriveThruBranch: BranchUiState? = null,
    // --- CAR SELECTION STATE ---
    val isSelectCarBottomSheetVisible: Boolean = false,
    val cars: List<CarUiState> = emptyList(),
    val selectedCar: CarUiState = CarUiState(),

    // --- ADD CAR WIZARD STATE ---
    val isCarDetailsBottomSheetVisible: Boolean = false,
    val addCarStep: AddCarStep = AddCarStep.SELECT_BRAND,
    val newCarNumber: String = "",
    val selectedCarBrand: CarBrandUiState? = null,
    val selectedCarColor: Color? = null,
    val selectedCarColorName: String? = null,
    val isBranchesLoading: Boolean = false,
    val branchesErrorMessage: String? = null,
    // Static Data (Same as BranchScreen)
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
//        CarBrandUiState("24" , "Mercury", "https://raw.githubusercontent.com/khaatteerr/super-survey-images/refs/heads/main/rwaq_images/mercury.png"),
//        CarBrandUiState("25" , "RAM", "https://raw.githubusercontent.com/khaatteerr/super-survey-images/refs/heads/main/rwaq_images/ram.jpeg"),
//        CarBrandUiState("26" , "Volvo", "https://raw.githubusercontent.com/khaatteerr/super-survey-images/refs/heads/main/rwaq_images/volvo.png"),



    ),
    val carColors: List<CarColor> = listOf(

        CarColor("أزرق", Color(0xFF0066FF).copy(alpha = 0.7f)),
        CarColor("أبيض", Color.White),
        CarColor("ذهبي", Color(0xFFD4AF37).copy(alpha = 0.7f)),
        CarColor("أسود", Color.Black),
        CarColor("سماوي", Color(0xFF00CCFF).copy(alpha = 0.7f)),
        CarColor("أخضر", Color(0xFF8BC34A).copy(alpha = 0.7f)),
        CarColor("بني", Color(0xFF795548).copy(alpha = 0.7f)),
        CarColor("رمادي", Color.Gray),
        CarColor("رمادي فاتح", Color.LightGray),
        CarColor("أحمر", Color.Red.copy(alpha = 0.7f)),
        CarColor("وردي", Color(0xFFFFC0CB).copy(alpha = 0.7f)),
        CarColor("برتقالي", Color(0xFFFF9800).copy(alpha = 0.7f)),
        CarColor("أصفر", Color.Yellow.copy(alpha = 0.7f))

    ),
)
data class CarColor(
    val name: String,
    val color: Color
)

enum class PaymentMethod {
    ONLINE, CASH
}

@Serializable
data class OrderLocation(
    val latitude: Double = 0.0,
    val longitude: Double= 0.0
)