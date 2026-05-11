package com.khater.rwaq.presentation.screens.branchScreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.khater.rwaq.designSystem.component.indicator.DotsProgressIndicator
import com.khater.rwaq.designSystem.component.scaffold.HomeScaffold
import com.khater.rwaq.designSystem.theme.theme.Theme
import com.khater.rwaq.presentation.composables.EmptyOrErrorContent
import com.khater.rwaq.presentation.composables.EventHandler
import com.khater.rwaq.presentation.composables.MapEmbedView
import com.khater.rwaq.presentation.composables.RwaqBackButton
import com.khater.rwaq.presentation.composables.RwaqTopBar
import com.khater.rwaq.presentation.navigation.Screen
import com.khater.rwaq.presentation.screens.branchScreen.components.AddCarWizardBottomSheet
import com.khater.rwaq.presentation.screens.branchScreen.components.AttachmentsBottomSheet
import com.khater.rwaq.presentation.screens.branchScreen.components.SelectCarBottomSheet
import com.khater.rwaq.presentation.screens.branchScreen.components.SelectedBranchSection
import com.khater.rwaq.presentation.screens.branchScreen.uiState.BranchScreenInteractionListener
import com.khater.rwaq.presentation.screens.branchScreen.uiState.BranchScreenUiEffect
import com.khater.rwaq.presentation.screens.branchScreen.uiState.BranchScreenUiState
import com.khater.rwaq.presentation.screens.cartScreen.uiStates.CarColor
import com.khater.rwaq.presentation.util.MapsUrl
import dev.jordond.compass.Coordinates
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import rwaq.composeapp.generated.resources.Res
import rwaq.composeapp.generated.resources.branches
import rwaq.composeapp.generated.resources.empty_car
import rwaq.composeapp.generated.resources.no_branches_to_display
import rwaq.composeapp.generated.resources.something_went_wrong

@Composable
fun BranchScreen(branchViewModel: BranchViewModel = koinViewModel()) {


    val state = branchViewModel.state.collectAsStateWithLifecycle().value

    val urlHandler = LocalUriHandler.current
    EventHandler(branchViewModel.effect) { effect, controller ->
        when (effect) {
            is BranchScreenUiEffect.NavigateToProductsScreen -> {
                controller.navigate(Screen.ProductScreen(
                    branchId = effect.branchId,
                    isPickupFromBranch = effect.isPickupFromBranch,
                    carName = effect.selectedCar?.name ?: "",
                    carNumber = effect.selectedCar?.carNumber ?: "",
                    branchName = effect.branchName,
                    carColor = effect.selectedCar?.color?.toArgb() ?: 0
                ))
            }

            is BranchScreenUiEffect.NavigateToExternalMap -> {
                urlHandler.openUri("${String.MapsUrl}${effect.location.latitude},${effect.location.longitude}")
            }

            BranchScreenUiEffect.NavigateBack -> {
                controller.navigateUp()
            }

        }
    }


    BranchContent(
        state = state,
        interactionListener = branchViewModel as BranchScreenInteractionListener
    )


}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BranchContent(
    state: BranchScreenUiState,
    interactionListener: BranchScreenInteractionListener,
) {

    BackHandler(
        enabled = state.isWorkTimeOverlayVisible || state.isSelectCarBottomSheetVisible ||
                state.isCarDetailsBottomSheetVisible
    ) {
        when {
            state.isSelectCarBottomSheetVisible -> interactionListener.onCloseSelectCar()
            state.isWorkTimeOverlayVisible -> interactionListener.onCloseWorkTimeBottomSheet()
            state.isCarDetailsBottomSheetVisible -> interactionListener.onCloseCarDetails()
        }
    }

    LaunchedEffect(state.selectedBranch.workTime, state.isSelectCarBottomSheetVisible) {
        if (state.isWorkTimeOverlayVisible) {
            interactionListener.onCloseWorkTimeBottomSheet()
        }
    }

    val animatedLat by animateFloatAsState(
        targetValue = state.selectedBranch.branchLocation.latitude.toFloat(),
        animationSpec = tween(durationMillis = 1000), // 1 second animation
        label = "LatitudeAnimation"
    )

    val animatedLng by animateFloatAsState(
        targetValue = state.selectedBranch.branchLocation.longitude.toFloat(),
        animationSpec = tween(durationMillis = 1000),
        label = "LongitudeAnimation"
    )

    // 2. Create the dynamic CameraPosition
//    // We use the animated values here so the map updates every frame
//    val animatedCameraPosition = CameraPosition(
//        coordinates = Coordinates(
//            latitude = animatedLat.toDouble(),
//            longitude = animatedLng.toDouble()
//        ),
//        zoom = 16f
//    )
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
                        RwaqBackButton(onClick = interactionListener::onBack, hasDropShadow = true, tint = Theme.colorScheme.shadePrimary)
                    },
                    middleContent = {
                        com.khater.rwaq.designSystem.component.text.Text(
                            text = stringResource(Res.string.branches),
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
//                     leadingContent = {
//                        RwaqBackButton(
//                            onClick = interactionListener::onBack,
//                            hasDropShadow = false,
//                            tint = Theme.colorScheme.brand.onBrand
//
//                        )
//                    },
//                    middleContent = {
//                        Text(
//                            text = stringResource(Res.string.choose_branch),
//                            color = Theme.colorScheme.brand.onBrand,
//                            style = Theme.typography.headline.medium,
//                            modifier = Modifier.offset(x = (-22).dp)
//                        )
//                    },
//                    isCenterAligned = true,
//                 )
//            }
//        ) {

            Column(modifier = Modifier.fillMaxSize())
            {

                MapEmbedView(latitude = animatedLat.toDouble(), longitude = animatedLng.toDouble())
//                Map(
//                    modifier = Modifier.fillMaxWidth()
//                        .fillMaxHeight(0.4f),
//                    properties = MapProperties(
//                        isMyLocationEnabled = true,
//                        mapType = MapType.NORMAL,
//                        mapTheme = MapTheme.LIGHT
//                    ),
//                    uiSettings = MapUISettings(
//                        myLocationButtonEnabled = true,
//                        compassEnabled = true
//                    ),
//                    cameraPosition = animatedCameraPosition,
//                    markers = state.markers,
//                    onMarkerClick = { marker ->
//                        println("Marker clicked: ${marker.title}")
//                    },
//                    onMapClick = { coordinates ->
//                        println("Map clicked at: ${coordinates.latitude}, ${coordinates.longitude}")
//                    }
//                )

                Column(
                    modifier = Modifier.fillMaxHeight()
                        .offset(y = (-6).dp)
                        .dropShadow(
                            shape = RoundedCornerShape(
                                topStart = Theme.spacing._12,
                                topEnd = Theme.spacing._12
                            ),
                            shadow = androidx.compose.ui.graphics.shadow.Shadow(
                                radius = 3.dp,
                                offset = _root_ide_package_.androidx.compose.ui.unit.DpOffset(
                                    y = (-3).dp,
                                    x = 0.dp
                                ),
                                color = Color.Black.copy(0.15f)
                            )
                        )
                        .background(Theme.colorScheme.brand.onBrand,RoundedCornerShape(
                            topStart = Theme.spacing._12,
                            topEnd = Theme.spacing._12
                        ))
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.Top
                ) {
                    when {
                        state.isLoading -> {
                            Box(
                                modifier = Modifier.fillMaxSize().weight(1f),
                                contentAlignment = Alignment.Center
                            ) {
                                DotsProgressIndicator(dotSize = Theme.spacing._8)
                            }
                        }

                        state.errorMessage != null -> {
                            EmptyOrErrorContent(
                                painter = painterResource(Res.drawable.something_went_wrong),
                                message = state.errorMessage,
                                isError = true,
                                onRetry = interactionListener::onRetry,
                                modifier = Modifier.padding(Theme.spacing._49)
                            )
                        }
                        state.branches.isEmpty() && !state.isLoading ->{
                            EmptyOrErrorContent(
                                painter = painterResource(Res.drawable.empty_car),
                                message = stringResource(Res.string.no_branches_to_display),
                                modifier = Modifier.padding(Theme.spacing._49)
                            )
                        }

                        state.branches.isNotEmpty() -> {

                            SelectedBranchSection(
                                options = state.branches,
                                isDriveThru = state.isDriveThru,
                                selectedOption = state.selectedBranch,
                                selectedCar = state.selectedCar,
                                onSelectCar = interactionListener::onOpenSelectCar,
                                onOptionSelected = {
                                    interactionListener.onSelectBranch(it.id)
                                },
                                onClickTime = interactionListener::onClickWorkTimeButton,
                                onClickLocation = interactionListener::onClickLocationButton
                            )


                        }

                    }

//                    Spacer(modifier = Modifier.weight(1f))
//                    PrimaryButton(
//                        text = stringResource(Res.string.order_from_this_branch),
//                        onClick = { interactionListener.onClickPickUpFromBranch(state.selectedBranch.id) },
//                        isEnabled = state.branches.isNotEmpty(),
//                        modifier = Modifier.fillMaxWidth()
//                            .padding(
//                                bottom = Theme.spacing._16,
//                                start = Theme.spacing._16,
//                                end = Theme.spacing._16
//                            ).align(Alignment.CenterHorizontally),
//                    )
                }


            }


        }




        AnimatedVisibility(
            visible = state.isWorkTimeOverlayVisible,
            enter = slideInVertically(initialOffsetY = { it }),
            exit = slideOutVertically(targetOffsetY = { it }),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            AttachmentsBottomSheet(
                onCancel = interactionListener::onCloseWorkTimeBottomSheet,
                workTimeUiState = state.selectedWorkTime
            )
        }

        AnimatedVisibility(
            visible = state.isSelectCarBottomSheetVisible,
            enter = slideInVertically(initialOffsetY = { it }),
            exit = slideOutVertically(targetOffsetY = { it }),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            SelectCarBottomSheet(
                onCancel = interactionListener::onCloseSelectCar,
                onCarSelected = interactionListener::onCarSelected,
                selectedCar = state.selectedCar,
                onDeleteCar = interactionListener::onDeleteCar,
                onAddCar = interactionListener::onAddCar,
                onSaveChanges = interactionListener::onSaveChanges,
                cars = state.cars
            )
        }
        AnimatedVisibility(
            visible = state.isCarDetailsBottomSheetVisible,
            enter = slideInVertically(initialOffsetY = { it }),
            exit = slideOutVertically(targetOffsetY = { it }),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            AddCarWizardBottomSheet(
                step = state.addCarStep, // Pass the current step
                onCancel = interactionListener::onCloseCarDetails,
                onNext = interactionListener::onAddCarNextStep, // Single handler for Next/Save

                // Data & State
                carBrands = state.carBrands,
                carColors = state.carColors.map { CarColor("",it) },
                selectedBrand = state.selectedCarBrand,
                selectedColor = state.selectedCarColor,
                carNumber = state.newCarNumber,

                // Selection Events
                onBrandSelect = interactionListener::onCarBrandSelected,
                onColorSelect = interactionListener::onCarColorSelected,
                onCarNumberChange = interactionListener::onCarNumberChanged
            )
        }

    }
}