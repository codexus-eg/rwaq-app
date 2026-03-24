package com.khater.rwaq.presentation.screens.branchScreen.uiState

import androidx.compose.ui.graphics.Color
import com.khater.rwaq.domain.entities.branch.BranchStatus
import com.khater.rwaq.domain.entities.car.Car
import com.khater.rwaq.presentation.model.SnackBarState
import com.swmansion.kmpmaps.core.Marker

data class BranchScreenUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val snackBar: SnackBarState = SnackBarState(),

    val isDriveThru: Boolean = false,
    val isSelectCarBottomSheetVisible: Boolean = false,
    val branches: List<BranchUiState> = emptyList(),
    val selectedBranch: BranchUiState = BranchUiState(),
    val markers: List<Marker> = emptyList(),
    val isWorkTimeOverlayVisible: Boolean = false,
    val selectedLocation: LocationUiState = LocationUiState(),
    val selectedWorkTime: List<BranchWorkTimeUiState> = emptyList(),
    val isCarDetailsBottomSheetVisible: Boolean = false,
    val selectedCar: CarUiState = CarUiState(),
    val cars: List<CarUiState> = emptyList(),
    val addCarStep: AddCarStep = AddCarStep.SELECT_BRAND,
    val newCarNumber: String = "",
    val selectedCarBrand: CarBrandUiState? = null,
    val selectedCarColor: Color? = null,

    // Dummy lists for the UI
    val carBrands: List<CarBrandUiState> = listOf(
        CarBrandUiState("1", "Audi", "https://i.ibb.co/VcG0vFKy/audi.png"),
        CarBrandUiState("2", "BMW", "https://i.ibb.co/W4tSr2ZJ/bmw.png"),
        CarBrandUiState("3", "BYD", "https://i.ibb.co/Vp2BRssV/byd.png"),
        CarBrandUiState("4", "Mercedes", "https://i.ibb.co/jZvRGmzk/Mercedes.png"),
        CarBrandUiState("5", "Ford", "https://i.ibb.co/v42s7NB6/ford.png"),
        CarBrandUiState("6", "Honda", "https://i.ibb.co/mFXHfJkt/Honda.png"),
        CarBrandUiState("7", "Chevrolet", "https://i.ibb.co/K4c4HMb/chevrolet.png"),
        CarBrandUiState("8", "Nissan", "https://i.ibb.co/bjGNqhqS/Nissan.png"),
        CarBrandUiState("9", "Ferrari", "https://i.ibb.co/gMfwpjR6/ferrari.png"),
        CarBrandUiState("10", "MG", "https://i.ibb.co/v4YkHNmk/mg.png"),
        CarBrandUiState("11", "Tesla", "https://i.ibb.co/hFnmP8pR/tesla.png"),
        CarBrandUiState("12", "Toyota", "https://i.ibb.co/rRBqPG37/toyota.png"),
    ),
    val carColors: List<Color> = listOf(
        Color(0xFF0066FF).copy(0.7f), Color.White, Color(0xFFD4AF37).copy(0.7f), Color.Black,
        Color(0xFF00CCFF).copy(0.7f), Color(0xFF8BC34A).copy(0.7f), Color(0xFF795548).copy(0.7f), Color.Gray,
        Color.LightGray, Color.Red.copy(0.7f), Color(0xFFFFC0CB).copy(0.7f), Color(0xFFFF9800).copy(0.7f),
        Color.Yellow.copy(0.7f)
    )

)

data class CarBrandUiState(
    val id: String,
    val name: String,
    val logoUrl: String
)
data class BranchUiState(
    val id: String = "",
    val branchName: String = "",
    val isBranchAvailableForDriveThru: Boolean = false,
    val branchStatus: BranchStatus = BranchStatus.OPEN,
    val distanceAwayFromYou: Double = 0.0,
    val branchLocation: LocationUiState = LocationUiState(),
    val workTime: List<BranchWorkTimeUiState> = emptyList()
)

data class BranchWorkTimeUiState(
    val day: String = "",
    val startTime: String = "",
    val endTime: String = "",
    val isAvailableAllDay: Boolean = false,
)

data class LocationUiState(
    val latitude: Double = 30.9595361,
    val longitude: Double = 40.9960087,
)


data class CarUiState(
    val id: String = "",
    val name: String = "",
    val imageUrl: String = "",
    val color: Color = Color.Unspecified,
    val colorName: String ="",
    val carNumber: String = "",
)

enum class AddCarStep {
    SELECT_BRAND,
    SELECT_COLOR,
    ENTER_NUMBER
}

// Add this inside BranchViewModel or your Mapper file
fun Car.toUiState(): CarUiState {
    return CarUiState(
        id = this.id,
        name = this.name, // Adjust property name based on your Domain Entity
        carNumber = this.carNumber,
        imageUrl = this.imageUrl,
        color = this.color,
        colorName = this.colorName
    )
}