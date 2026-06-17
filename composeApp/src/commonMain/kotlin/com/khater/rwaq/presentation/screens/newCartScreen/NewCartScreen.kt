package com.khater.rwaq.presentation.screens.newCartScreen

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import com.khater.rwaq.designSystem.component.button.PrimaryButton
import com.khater.rwaq.designSystem.component.indicator.DotsProgressIndicator
import com.khater.rwaq.designSystem.theme.theme.Theme
import com.khater.rwaq.designSystem.component.scaffold.HomeScaffold
import com.khater.rwaq.presentation.composables.EventHandler
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.khater.rwaq.designSystem.component.dialog.BasicDialog
import com.khater.rwaq.designSystem.component.dialog.Dialog
import com.khater.rwaq.designSystem.component.textField.MultiLineTextField
import com.khater.rwaq.domain.location.NativeLocationSettings
import com.khater.rwaq.presentation.util.Dimensions.PADDING_BOTTOM_WITH_NAV_VISIBLE
import com.khater.rwaq.domain.entities.cart.CartItem
import com.khater.rwaq.domain.model.PickupType
import com.khater.rwaq.presentation.composables.EmptyOrErrorContent
import com.khater.rwaq.presentation.composables.RwaqBackButton
import com.khater.rwaq.presentation.composables.RwaqTopBar
import com.khater.rwaq.presentation.navigation.Screen
import com.khater.rwaq.presentation.screens.branchScreen.components.AddCarWizardBottomSheet
import com.khater.rwaq.presentation.screens.branchScreen.components.AttachmentsBottomSheet
import com.khater.rwaq.presentation.screens.branchScreen.components.SelectCarBottomSheet
import com.khater.rwaq.presentation.screens.cartScreen.components.InlineBranchList
import com.khater.rwaq.presentation.screens.cartScreen.components.SectionCard
import com.khater.rwaq.presentation.screens.newCartScreen.uiStates.NewCartInteractionListener
import com.khater.rwaq.presentation.screens.newCartScreen.uiStates.NewCartUIEffect
import com.khater.rwaq.presentation.screens.newCartScreen.uiStates.NewCartUiState
import com.khater.rwaq.presentation.util.Dimensions
import com.khater.rwaq.presentation.util.LocalNavigationProvider
import com.khater.rwaq.presentation.util.MapsUrl
import com.khater.rwaq.presentation.util.rippleClickable
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import rwaq.composeapp.generated.resources.*
import kotlin.math.ceil

@Composable
fun NewCartScreen(
    viewModel: NewCartViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val urlHandler = LocalUriHandler.current

    LaunchedEffect(Unit) {
        viewModel.onScreenOpened()
    }

    val paymentLauncher = com.khater.rwaq.presentation.payment.rememberPaymobLauncher(
        onSuccess = { viewModel.onPaymentSuccess() },
        onFailure = { message -> viewModel.onPaymentFailure(message) },
        onPending = { viewModel.onPaymentPending() },
    )

    EventHandler(viewModel.effect) { effect, controller ->
        when (effect) {
            is NewCartUIEffect.NavigateToCheckout -> {
                controller.navigateUp()
            }
            is NewCartUIEffect.ShowError -> {
                // Show snackbar
            }
            is NewCartUIEffect.NavigateToPayment -> {
                paymentLauncher.launch(
                    clientSecret = effect.clientSecret,
                    publicKey = effect.publicKey,
                )
            }
            NewCartUIEffect.NavigateBack -> {
                controller.navigateUp()
            }
            NewCartUIEffect.NavigateToOrders -> {
                controller.navigate(Screen.MyOrderScreen)
            }
            NewCartUIEffect.NavigateToLogin -> {
                controller.navigate(Screen.LoginScreen)
            }
            is NewCartUIEffect.NavigateToExternalMap ->{
                urlHandler.openUri("${String.MapsUrl}${effect.location.latitude},${effect.location.longitude}")

            }
            NewCartUIEffect.OpenLocationSettings -> {
                NativeLocationSettings.openSettings()
            }
        }
    }

    HomeScaffold(
        snackBarState = state.snackBar,
        hasStatusBarColor = true,
        topBar = {
            RwaqTopBar(
                containerColor = Color(0xFFFBF7F0),
                leadingContent = {
                    RwaqBackButton(
                        onClick = viewModel::onBack,
                        hasDropShadow = true,
                        tint = Theme.colorScheme.shadePrimary
                    )
                },
                middleContent = {
                    Text(
                        text = stringResource(Res.string.cart),
                        color = Theme.colorScheme.shadePrimary,
                        style = Theme.typography.headline.medium,
                        modifier = Modifier.offset(x = (-22).dp)
                    )
                },
                isCenterAligned = true,
            )
        },
        overlays = {
            dialog(state.showGuestDialog) {
                BasicDialog(
                    isVisible = state.showGuestDialog,
                    onDismiss = viewModel::onDismissGuestDialog,
                    onCancelClick = { },
                    hasDismissButton = false,
                    dismissOnBackPress = false,
                    dismissOnClickOutside = false,
                    actionButtons = {
                        PrimaryButton(
                            text = stringResource(Res.string.login),
                            onClick = viewModel::onClickLogin,
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
            dialog(state.isPaymentSuccessDialogVisible) {
                BasicDialog(
                    isVisible = state.isPaymentSuccessDialogVisible,
                    onDismiss = viewModel::onPaymentResultDialogAction,
                    onCancelClick = viewModel::onPaymentResultDialogAction,
                    hasDismissButton = false,
                    dismissOnBackPress = false,
                    dismissOnClickOutside = false,
                    actionButtons = {
                        PrimaryButton(
                            text = stringResource(Res.string.my_orders),
                            onClick = viewModel::onPaymentResultDialogAction,
                            modifier = Modifier.fillMaxWidth().padding(top = Theme.spacing._16)
                        )
                    }
                ) {
                    Dialog(
                        title = stringResource(Res.string.payment_success_title),
                        message = stringResource(Res.string.payment_success_message),
                        icon = painterResource(Res.drawable.rwaq_logo)
                    )
                }
            }
        },
        content = {
            NewCartContent(state = state, listener = viewModel)
        }
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun NewCartContent(
    state: NewCartUiState,
    listener: NewCartInteractionListener,
) {
    BackHandler(
        enabled = state.isWorkTimeOverlayVisible || state.isSelectCarBottomSheetVisible ||
                state.isCarDetailsBottomSheetVisible
    ) {
        when {
            state.isSelectCarBottomSheetVisible -> listener.onCloseSelectCar()
            state.isWorkTimeOverlayVisible -> listener.onCloseWorkTimeBottomSheet()
            state.isCarDetailsBottomSheetVisible -> listener.onCloseCarDetails()
        }
    }

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    val secondFieldFocus = remember { FocusRequester() }


    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (state.isLoading) {
            DotsProgressIndicator(dotSize = 8.dp)
        } else if (state.error != null) {
            EmptyOrErrorContent(
                painter = painterResource(Res.drawable.something_went_wrong),
                message = state.error,
                isError = true,
                onRetry = listener::onRetryGetCart,
                modifier = Modifier.padding(Theme.spacing._49)
            )
        } else if (state.cart == null || state.cart.items.isEmpty()) {
            EmptyOrErrorContent(
                painter = painterResource(Res.drawable.empty_cart),
                message = stringResource(Res.string.cart_empty),
                imageSize = 200.dp,
                modifier = Modifier.padding(Theme.spacing._49)
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                        Spacer(modifier = Modifier.height(12.dp))

                        // Closed-branch warning: selected pickup/drive-thru branch is not open now
                        AnimatedVisibility(
                            visible = state.isSelectedBranchClosed,
                            enter = fadeIn() + expandVertically(),
                            exit = fadeOut() + shrinkVertically()
                        ) {
                            ClosedBranchBanner()
                        }

                        // Branch error banner
                        AnimatedVisibility(visible = state.branchesErrorMessage != null) {
                            Row(
                                modifier = Modifier.fillMaxWidth()
                                    .background(Theme.colorScheme.error.copy(0.4f))
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = stringResource(Res.string.failed_to_load_branches),
                                    style = Theme.typography.title.medium,
                                    color = Theme.colorScheme.brand.onBrand
                                )
                                IconButton(onClick = listener::onRetryGetBranches) {
                                    Icon(Icons.Default.Refresh, contentDescription = "Retry", tint = Theme.colorScheme.brand.onBrand)
                                }
                            }
                        }

                        SectionCard(
                            title = stringResource(Res.string.order_details),
                            actionText = stringResource(Res.string.add_more),
                            onActionClick = listener::onAddMoreItems
                        ) {
                            state.cart.items.forEachIndexed { index, item ->
                                NewCartItemRow(
                                    item = item,
                                    listener = listener,
                                    updatingItemId = state.updatingItemId,
                                    updatingExtensionId = state.updatingExtensionId,
                                    userPoints = state.userPoints
                                )
                                if (index < state.cart.items.lastIndex) {
                                    HorizontalDivider(
                                        modifier = Modifier.padding(vertical = 12.dp),
                                        thickness = 0.5.dp,
                                        color = Color.LightGray
                                    )
                                }
                            }
                        }

                        // Payment Details
                        SectionCard(title = stringResource(Res.string.payment_details)) {
                            if (state.hasOnlyRewardItems) {
                                PaymentDetailRow(
                                    label = stringResource(Res.string.points_used),
                                    amount = "${state.totalRewardPoints} ${stringResource(Res.string.point)}"
                                )
                                state.userPoints?.let { points ->
                                    PaymentDetailRow(
                                        label = stringResource(Res.string.my_points),
                                        amount = "$points ${stringResource(Res.string.point)}"
                                    )
                                }
                            } else {
                                PaymentDetailRow(
                                    label = stringResource(Res.string.sub_total),
                                    amount = "${state.subTotal} ${stringResource(Res.string.currency_sar)}"
                                )

                                if (state.rewardDiscount > 0) {
                                    PaymentDetailRow(
                                        label = stringResource(Res.string.reward_discount),
                                        amount = "- ${state.rewardDiscount} ${stringResource(Res.string.currency_sar)}",
                                        textColor = Theme.colorScheme.success
                                    )
                                }
                            }

                            if (state.appliedDeliveryFee > 0) {
                                PaymentDetailRow(
                                    label = stringResource(Res.string.delivery_fee),
                                    amount = "${state.appliedDeliveryFee} ${stringResource(Res.string.currency_sar)}"
                                )
                            }

                            HorizontalDivider(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp), thickness = 0.5.dp, color = Color.LightGray)
                            PaymentDetailRow(
                                label = stringResource(Res.string.total_amount),
                                amount = "${state.payableTotal} ${stringResource(Res.string.currency_sar)}",
                                isBold = true
                            )
                        }

                        // Order Notes
                        SectionCard(title = stringResource(Res.string.order_notes)) {
                            OutlinedTextField(
                                value = state.orderNotes,
                                onValueChange = listener::onOrderNotesChanged,
                                placeholder = {
                                    Text(stringResource(Res.string.order_notes_hint), color = Color.LightGray, style = Theme.typography.body.small)
                                },
                                keyboardOptions = KeyboardOptions(
                                    imeAction = ImeAction.Done
                                ),

                                keyboardActions = KeyboardActions(
                                    onDone = {
                                        focusManager.clearFocus()
                                        keyboardController?.hide()
                                    }
                                ),

                                textStyle = Theme.typography.body.small,
                                modifier = Modifier.fillMaxWidth().height(80.dp),
                                shape = RoundedCornerShape(8.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    unfocusedBorderColor = Color.LightGray,
                                    focusedBorderColor = Theme.colorScheme.brand.brand
                                )
                            )
                        }

                        // Receiving Method
                        SectionCard(title = stringResource(Res.string.receiving_method)) {
                            ReceivingOptionRow(
                                title = stringResource(Res.string.pick_up_from_branch),
                                isSelected = state.isBranchPickup,
                                onClick = { listener.onPickupTypeChanged(PickupType.BRANCH) }
                            )
                            AnimatedVisibility(visible = state.isBranchPickup) {
                                InlineBranchList(
                                    branches = state.allBranches.map { b ->
                                        com.khater.rwaq.presentation.screens.branchScreen.uiState.BranchUiState(
                                            id = b.id,
                                            branchName = b.branchName,
                                            branchStatus = b.branchStatus
                                        )
                                    },
                                    selectedBranchId = state.selectedPickupBranch?.id,
                                    onSelectBranch = { id ->
                                        state.allBranches.find { it.id == id }?.let { listener.onBranchSelected(it) }
                                    },
                                    onClickTime = { id ->
                                        state.allBranches.find { it.id == id }?.let { listener.onClickWorkTimeButton(it) }
                                    },
                                    onClickLocation = { loc ->
                                        state.allBranches.find { it.branchLocation.latitude == loc.latitude && it.branchLocation.longitude == loc.longitude }?.let {
                                            listener.onClickLocationButton(it)
                                        }
                                    }
                                )
                            }

                            HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray, modifier = Modifier.padding(vertical = 8.dp))

                            ReceivingOptionRow(
                                title = stringResource(Res.string.drive_thru),
                                isSelected = state.isDriveThru,
                                onClick = { listener.onPickupTypeChanged(PickupType.DRIVE_THRU) }
                            )
                            AnimatedVisibility(visible = state.isDriveThru) {
                                Column {
                                    InlineBranchList(
                                        branches = state.driveThruBranches.map { b ->
                                            com.khater.rwaq.presentation.screens.branchScreen.uiState.BranchUiState(
                                                id = b.id,
                                                branchName = b.branchName,
                                                branchStatus = b.branchStatus
                                            )
                                        },
                                        selectedBranchId = state.selectedDriveThruBranch?.id,
                                        onSelectBranch = { id ->
                                            state.driveThruBranches.find { it.id == id }?.let { listener.onBranchSelected(it) }
                                        },
                                        onClickTime = { id ->
                                            state.driveThruBranches.find { it.id == id }?.let { listener.onClickWorkTimeButton(it) }
                                        },
                                        onClickLocation = { loc ->
                                            state.driveThruBranches.find { it.branchLocation.latitude == loc.latitude && it.branchLocation.longitude == loc.longitude }?.let {
                                                listener.onClickLocationButton(it)
                                            }
                                        }
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        stringResource(Res.string.choose_car),
                                        style = Theme.typography.body.small,
                                        color = Theme.colorScheme.primary.primary,
                                        modifier = Modifier.padding(bottom = 8.dp)
                                    )
                                    Box(
                                        modifier = Modifier.fillMaxWidth().border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
                                            .rippleClickable { listener.onOpenSelectCar() }.padding(12.dp)
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            if (state.selectedCar.id.isNotEmpty()) {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    if (state.selectedCar.imageUrl.isNotEmpty()) {
                                                        AsyncImage(
                                                            model = state.selectedCar.imageUrl,
                                                            contentDescription = null,
                                                            modifier = Modifier.size(30.dp),
                                                            placeholder = painterResource(Res.drawable.car),
                                                            error = painterResource(Res.drawable.car),
                                                            contentScale = ContentScale.Fit
                                                        )
                                                        Spacer(modifier = Modifier.width(8.dp))
                                                    }
                                                    Column {
                                                        Text(
                                                            state.selectedCar.name,
                                                            style = Theme.typography.body.medium,
                                                            color = Theme.colorScheme.primary.primary
                                                        )
                                                        Text(
                                                            state.selectedCar.carNumber,
                                                            style = Theme.typography.body.extraSmall,
                                                            color = Color.Gray
                                                        )
                                                    }
                                                }
                                            } else {
                                                Text(stringResource(Res.string.choose_car), style = Theme.typography.body.medium, color = Color.Gray)
                                            }
                                            Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = Color.Gray)
                                        }
                                    }
                                }
                            }

                            HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray, modifier = Modifier.padding(vertical = 8.dp))

                            ReceivingOptionRow(
                                title = stringResource(Res.string.delivery),
                                isSelected = state.isDelivery,
                                onClick = { listener.onPickupTypeChanged(PickupType.DELIVERY) }
                            )
                            AnimatedVisibility(visible = state.isDelivery) {
                                Column {
                                    Text(
                                        stringResource(Res.string.select_branch),
                                        style = Theme.typography.body.small,
                                        color = Theme.colorScheme.primary.primary,
                                        modifier = Modifier.padding(bottom = 8.dp)
                                    )
                                    InlineBranchList(
                                        branches = state.allBranches.map { b ->
                                            com.khater.rwaq.presentation.screens.branchScreen.uiState.BranchUiState(
                                                id = b.id,
                                                branchName = b.branchName,
                                                branchStatus = b.branchStatus
                                            )
                                        },
                                        selectedBranchId = state.selectedDeliveryBranch?.id,
                                        onSelectBranch = { id ->
                                            state.allBranches.find { it.id == id }?.let { listener.onBranchSelected(it) }
                                        },
                                        onClickTime = { id ->
                                            state.allBranches.find { it.id == id }?.let { listener.onClickWorkTimeButton(it) }
                                        },
                                        onClickLocation = { loc ->
                                            state.allBranches.find { it.branchLocation.latitude == loc.latitude && it.branchLocation.longitude == loc.longitude }?.let {
                                                listener.onClickLocationButton(it)
                                            }
                                        }
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    DeliveryDetails(
                                        state = state,
                                        listener = listener,
                                    )
                                }
                            }
                        }

                        // Payment Method Selection
                        // When the cart is reward-only (nothing payable online), only Cash is offered.
                        SectionCard(title = null) {
                            if (!state.mustPayWithCash) {
                                PaymentMethodRow(
                                    title = stringResource(Res.string.online_payment),
                                    icon = painterResource(Res.drawable.onlinepayemt),
                                    isSelected = state.selectedPaymentMethod == "ONLINE",
                                    onClick = { listener.onPaymentMethodChanged("ONLINE") }
                                )
                                AnimatedVisibility(
                                    visible = state.showApplePayOption && state.selectedPaymentMethod == "ONLINE"
                                ) {
                                    Column {
                                        HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray)
                                        ApplePayOptionRow(
                                            isSelected = state.isApplePaySelected,
                                            onSelectionChanged = listener::onApplePaySelectionChanged
                                        )
                                    }
                                }
                                HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray)
                            }
                            PaymentMethodRow(
                                title = stringResource(Res.string.cash),
                                icon = painterResource(Res.drawable.cash),
                                isSelected = state.mustPayWithCash || state.selectedPaymentMethod == "CASH",
                                onClick = { listener.onPaymentMethodChanged("CASH") }
                            )
                        }

                // Checkout Button
                PrimaryButton(
                    text = stringResource(Res.string.checkout_order),
                    onClick = listener::onCheckoutClicked,
                    isEnabled = !state.isCheckoutLoading && !state.isSelectedBranchClosed,
                    isLoading = state.isCheckoutLoading,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)
                )

                Spacer(modifier = Modifier.height(PADDING_BOTTOM_WITH_NAV_VISIBLE.dp))
            }
        }

            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
            ) {
                AnimatedVisibility(
                    visible = state.isWorkTimeOverlayVisible,
                    enter = slideInVertically(initialOffsetY = { it }),
                    exit = slideOutVertically(targetOffsetY = { it }),
                    modifier = Modifier.align(Alignment.BottomCenter)
                        .padding(bottom = PADDING_BOTTOM_WITH_NAV_VISIBLE.dp)
                ) {
                    AttachmentsBottomSheet(
                        workTimeUiState = state.selectedWorkTime,
                        onCancel = listener::onCloseWorkTimeBottomSheet
                    )
                }

                AnimatedVisibility(
                    visible = state.isSelectCarBottomSheetVisible,
                    enter = slideInVertically(initialOffsetY = { it }),
                    exit = slideOutVertically(targetOffsetY = { it }),
                    modifier = Modifier.align(Alignment.BottomCenter)
                        .padding(bottom = PADDING_BOTTOM_WITH_NAV_VISIBLE.dp)
                ) {
                    SelectCarBottomSheet(
                        onCancel = listener::onCloseSelectCar,
                        onCarSelected = { carUiState ->
                            listener.onCarSelected(
                                com.khater.rwaq.domain.entities.car.Car(
                                    id = carUiState.id,
                                    name = carUiState.name,
                                    imageUrl = carUiState.imageUrl,
                                    color = carUiState.color,
                                    colorName = carUiState.colorName,
                                    carNumber = carUiState.carNumber
                                )
                            )
                        },
                        selectedCar = com.khater.rwaq.presentation.screens.branchScreen.uiState.CarUiState(
                            id = state.selectedCar.id,
                            name = state.selectedCar.name,
                            imageUrl = state.selectedCar.imageUrl,
                            color = state.selectedCar.color,
                            colorName = state.selectedCar.colorName,
                            carNumber = state.selectedCar.carNumber
                        ),
                        onDeleteCar = { carId ->
                            state.cars.find { it.id == carId }?.let { car ->
                                listener.onDeleteCar(car)
                            }
                        },
                        onAddCar = listener::onAddCar,
                        onSaveChanges = listener::onSaveChanges,
                        cars = state.cars.map { car ->
                            com.khater.rwaq.presentation.screens.branchScreen.uiState.CarUiState(
                                id = car.id,
                                name = car.name,
                                imageUrl = car.imageUrl,
                                color = car.color,
                                colorName = car.colorName,
                                carNumber = car.carNumber
                            )
                        }
                    )
                }

                AnimatedVisibility(
                    visible = state.isCarDetailsBottomSheetVisible,
                    enter = slideInVertically(initialOffsetY = { it }),
                    exit = slideOutVertically(targetOffsetY = { it }),
                    modifier = Modifier.align(Alignment.BottomCenter)
                        .padding(bottom = PADDING_BOTTOM_WITH_NAV_VISIBLE.dp)
                ) {
                    AddCarWizardBottomSheet(
                        step = when (state.addCarStep) {
                            1 -> com.khater.rwaq.presentation.screens.branchScreen.uiState.AddCarStep.SELECT_BRAND
                            2 -> com.khater.rwaq.presentation.screens.branchScreen.uiState.AddCarStep.SELECT_COLOR
                            else -> com.khater.rwaq.presentation.screens.branchScreen.uiState.AddCarStep.ENTER_NUMBER
                        },
                        onCancel = listener::onCloseCarDetails,
                        onNext = listener::onAddCarNextStep,
                        carBrands = state.carBrands,
                        carColors = state.carColors,
                        selectedBrand = state.selectedCarBrand,
                        selectedColor = state.selectedCarColor?.color,
                        carNumber = state.newCarNumber,
                        onBrandSelect = listener::onCarBrandSelected,
                        onColorSelect = listener::onCarColorSelected,
                        onCarNumberChange = listener::onCarNumberChanged
                    )
                }
            }
        }
}

@Composable
private fun ClosedBranchBanner() {
    val transition = rememberInfiniteTransition(label = "closed_branch")
    val pulse by transition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(900),
            repeatMode = RepeatMode.Reverse
        ),
        label = "closed_branch_alpha"
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Theme.colorScheme.error.copy(alpha = 0.10f))
            .border(1.dp, Theme.colorScheme.error.copy(alpha = pulse), RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier.size(10.dp).clip(CircleShape)
                .background(Theme.colorScheme.error.copy(alpha = pulse))
        )
        Text(
            text = stringResource(Res.string.selected_branch_temporarily_closed),
            style = Theme.typography.body.small.copy(fontWeight = FontWeight.SemiBold),
            color = Theme.colorScheme.error
        )
    }
}

@Composable
private fun DeliveryDetails(
    state: NewCartUiState,
    listener: NewCartInteractionListener,
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.SpaceBetween
//        ) {
//            Text(
//                text = if (state.isLocationObtained) {
//                    stringResource(Res.string.current_location_ready)
//                } else {
//                    stringResource(Res.string.add_location)
//                },
//                style = Theme.typography.body.small,
//                color = if (state.isLocationObtained) Color(0xFF3B7D5C) else Color.Gray,
//                modifier = Modifier.weight(1f)
//            )
//            TextButton(onClick = listener::onRetryCurrentLocation) {
//                Text(
//                    text = stringResource(Res.string.detect_current_location),
//                    style = Theme.typography.body.small,
//                    color = Theme.colorScheme.brand.brand
//                )
//            }
//        }

        val keyboardController = LocalSoftwareKeyboardController.current
        val focusManager = LocalFocusManager.current


        OutlinedTextField(
            value = state.deliveryAddress,
            onValueChange = listener::onDeliveryAddressChanged,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),

            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                }
            ),
            label = {
                Text(
                    text = stringResource(Res.string.delivery_address),
                    style = Theme.typography.body.small
                )
            },
            placeholder = {
                Text(
                    text = stringResource(Res.string.delivery_address_hint),
                    color = Color.LightGray,
                    style = Theme.typography.body.small
                )
            },
            textStyle = Theme.typography.body.small,
            modifier = Modifier.fillMaxWidth().height(80.dp)  ,
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.LightGray,
                focusedBorderColor = Theme.colorScheme.brand.brand
            )
        )
    }
}

@Composable
fun NewCartItemRow(
    item: CartItem,
    listener: NewCartInteractionListener,
    updatingItemId: String?,
    updatingExtensionId: String?,
    userPoints: Int?,
) {
    val isThisItemUpdating = updatingItemId == item.id
    // The product counter spins only when the product line itself is updating,
    // not when one of its extensions is.
    val isProductUpdating = isThisItemUpdating && updatingExtensionId == null

    val canIncrease = !item.isRewardItem ||
            userPoints == null ||
            userPoints >= item.rewardPointsForOneMore()

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.size(60.dp).clip(RoundedCornerShape(8.dp)).background(Color(0xFFFAFAFA)),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = item.imageUrl,
                        contentDescription = item.productName,
                        contentScale = ContentScale.Fit,
                        placeholder = painterResource(Res.drawable.empty_product),
                        error = painterResource(Res.drawable.empty_product),
                        modifier = Modifier.fillMaxSize().padding(8.dp)
                    )
                }
                Column {
                    Text(item.productName, style = Theme.typography.body.medium, color = Theme.colorScheme.primary.primary)
                    Text(item.size, style = Theme.typography.body.extraSmall, color = Color.Gray)
                }
            }
            Text(
                if (item.isRewardItem) {
                    "${item.pointsCost} ${stringResource(Res.string.point)}"
                } else {
                    "${item.totalPrice} ${stringResource(Res.string.currency_sar)}"
                },
                style = Theme.typography.body.medium,
                color = Theme.colorScheme.primary.primary
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            QuantitySelector(
                count = item.quantity,
                maxCount = if (canIncrease) Int.MAX_VALUE else item.quantity,
                onIncrease = { listener.onIncreaseQuantity(item.id, item.quantity) },
                onDecrease = { listener.onDecreaseQuantity(item.id, item.quantity) },
                onDelete = { listener.onRemoveItem(item.id) },
                isBig = true,
                isLoading = isProductUpdating
            )
        }

        // Editable extensions for this cart item
        if (item.extensions.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                stringResource(Res.string.extensions),
                style = Theme.typography.body.extraSmall,
                color = Theme.colorScheme.primary.primary,
            )
            item.extensions.forEach { ext ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            ext.name,
                            style = Theme.typography.body.extraSmall,
                            color = Theme.colorScheme.shadePrimary
                        )
                        Text(
                            "${ext.price} ${stringResource(Res.string.currency_sar)}",
                            style = Theme.typography.body.extraSmall,
                            color = Color.Gray
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    QuantitySelector(
                        count = ext.quantity,
                        onIncrease = { listener.onIncreaseExtension(item.id, ext.extensionId) },
                        onDecrease = { listener.onDecreaseExtension(item.id, ext.extensionId) },
                        onDelete = { listener.onRemoveExtension(item.id, ext.extensionId) },
                        isBig = false,
                        isLoading = isThisItemUpdating && updatingExtensionId == ext.extensionId
                    )
                }
            }
        }
    }
}

private fun CartItem.rewardPointsForOneMore(): Int {
    val pointsFromUnitPrice = unitPrice.takeIf { it > 0.0 }
    val pointsFromTotalCost = if (pointsCost > 0 && quantity > 0) {
        pointsCost.toDouble() / quantity
    } else {
        0.0
    }
    return ceil(pointsFromUnitPrice ?: pointsFromTotalCost).toInt()
}

@Composable
fun QuantitySelector(
    count: Int,
    maxCount: Int = Int.MAX_VALUE,
    onDecrease: () -> Unit,
    onIncrease: () -> Unit,
    onDelete: (() -> Unit)? = null,
    isBig: Boolean = false,
    isLoading: Boolean = false
) {
    val height = if (isBig) 35.dp else 25.dp
    val iconSize = if (isBig) 20.dp else 16.dp

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .height(height)
            .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
            .padding(horizontal = 4.dp)
            .animateContentSize()
    ) {
        val isDeleteState = count == 1 && onDelete != null

        val leftIconPainter = if (isDeleteState) {
            rememberVectorPainter(Icons.Outlined.Delete)
        } else {
            painterResource(Res.drawable.minus)
        }

        IconButton(
            onClick = { if (isDeleteState) onDelete() else onDecrease() },
            modifier = Modifier.size(height),
            enabled = !isLoading
        ) {
            Icon(
                painter = leftIconPainter,
                contentDescription = null,
                modifier = Modifier.size(iconSize),
                tint = if (isDeleteState) Color.Red else Color.Black
            )
        }

        if (isLoading) {
            Box(
                modifier = Modifier.padding(horizontal = 8.dp).size(iconSize),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(iconSize),
                    color = Theme.colorScheme.brand.brand,
                    strokeWidth = 2.dp
                )
            }
        } else {
            Text(
                text = count.toString(),
                style = Theme.typography.body.medium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = if (isBig) 16.sp else 14.sp
                ),
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }

        IconButton(
            onClick = onIncrease,
            enabled = count < maxCount && !isLoading,
            modifier = Modifier.size(height)
        ) {
            Icon(
                painter = painterResource(Res.drawable.plus),
                contentDescription = null,
                modifier = Modifier.size(iconSize),
                tint = if (count < maxCount) Color.Black else Color.Gray
            )
        }
    }
}
@Composable
fun ReceivingOptionRow(title: String, isSelected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).rippleClickable { onClick() }.padding(vertical = 12.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(20.dp).clip(CircleShape)
                .border(2.dp, if (isSelected) Theme.colorScheme.brand.brand else Color.Gray, CircleShape)
                .background(if (isSelected) Theme.colorScheme.brand.brand else Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            if (isSelected) Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(title, style = Theme.typography.body.medium, color = Theme.colorScheme.primary.primary)
    }
}

@Composable
fun PaymentDetailRow(
    label: String,
    amount: String,
    isBold: Boolean = false,
    textColor: Color = Theme.colorScheme.primary.primary
) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, style = if (isBold) Theme.typography.body.medium else Theme.typography.body.small, color = Theme.colorScheme.primary.primary)
        Text(amount, style = if (isBold) Theme.typography.body.medium else Theme.typography.body.small, color = if (isBold) Theme.colorScheme.primary.primary else textColor)
    }
}

@Composable
fun PaymentMethodRow(title: String, icon: Painter, isSelected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).rippleClickable { onClick() }.padding(vertical = 12.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Text(title, style = Theme.typography.body.small, modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier.size(24.dp).clip(RoundedCornerShape(4.dp))
                .background(if (isSelected) Theme.colorScheme.brand.brand else Color.Transparent)
                .border(1.dp, if (isSelected) Theme.colorScheme.brand.brand else Color.Gray, RoundedCornerShape(4.dp)),
            contentAlignment = Alignment.Center
        ) {
            if (isSelected) Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
        }
    }
}

@Composable
fun ApplePayOptionRow(isSelected: Boolean, onSelectionChanged: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .rippleClickable { onSelectionChanged(!isSelected) }
            .padding(vertical = 10.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(Res.drawable.apple_pay),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = stringResource(Res.string.pay_with_apple_pay),
            style = Theme.typography.body.small,
            modifier = Modifier.weight(1f)
        )
        Checkbox(
            checked = isSelected,
            onCheckedChange = onSelectionChanged,
            colors = CheckboxDefaults.colors(
                checkedColor = Theme.colorScheme.brand.brand,
                uncheckedColor = Color.Gray,
                checkmarkColor = Color.White
            )
        )
    }
}
