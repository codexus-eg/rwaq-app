package com.khater.rwaq.presentation.screens.cartScreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.touchlab.kermit.Logger
import coil3.compose.AsyncImage
import com.khater.rwaq.designSystem.component.button.PrimaryButton
import com.khater.rwaq.designSystem.component.dialog.BasicDialog
import com.khater.rwaq.designSystem.component.dialog.Dialog
import com.khater.rwaq.designSystem.component.indicator.DotsProgressIndicator
import com.khater.rwaq.designSystem.component.scaffold.HomeScaffold
import com.khater.rwaq.designSystem.component.text.Text
import com.khater.rwaq.designSystem.theme.theme.Theme
import com.khater.rwaq.domain.entities.order.Order
import com.khater.rwaq.presentation.composables.EmptyOrErrorContent
import com.khater.rwaq.presentation.composables.EventHandler
import com.khater.rwaq.presentation.composables.RwaqBackButton
import com.khater.rwaq.presentation.composables.RwaqTopBar
import com.khater.rwaq.presentation.navigation.Screen
import com.khater.rwaq.presentation.payment.rememberPaymobLauncher
import com.khater.rwaq.presentation.screens.branchScreen.components.AddCarWizardBottomSheet
import com.khater.rwaq.presentation.screens.branchScreen.components.AttachmentsBottomSheet
import com.khater.rwaq.presentation.screens.branchScreen.components.SelectCarBottomSheet
import com.khater.rwaq.presentation.screens.branchScreen.components.SelectedBranchSection
import com.khater.rwaq.presentation.screens.cartScreen.components.InlineBranchList
import com.khater.rwaq.presentation.screens.cartScreen.components.SectionCard
import com.khater.rwaq.presentation.screens.cartScreen.uiStates.CartInteractionListener
import com.khater.rwaq.presentation.screens.cartScreen.uiStates.CartScreenUIEffect
import com.khater.rwaq.presentation.screens.cartScreen.uiStates.CartScreenUIEffect.NavigateBack
import com.khater.rwaq.presentation.screens.cartScreen.uiStates.CartUiState
import com.khater.rwaq.presentation.screens.cartScreen.uiStates.PaymentMethod
import com.khater.rwaq.presentation.util.Dimensions
import com.khater.rwaq.presentation.util.Dimensions.PADDING_BOTTOM_WITH_NAV_VISIBLE
import com.khater.rwaq.presentation.util.MapsUrl
import com.khater.rwaq.presentation.util.rippleClickable
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import rwaq.composeapp.generated.resources.Res
import rwaq.composeapp.generated.resources.add_more
import rwaq.composeapp.generated.resources.cart
import rwaq.composeapp.generated.resources.cart_empty
import rwaq.composeapp.generated.resources.cash
import rwaq.composeapp.generated.resources.checkout_order
import rwaq.composeapp.generated.resources.choose_branch
import rwaq.composeapp.generated.resources.choose_car
import rwaq.composeapp.generated.resources.currency_sar
import rwaq.composeapp.generated.resources.drive_thru
import rwaq.composeapp.generated.resources.empty_cart
import rwaq.composeapp.generated.resources.extensions
import rwaq.composeapp.generated.resources.failed_to_load_branches
import rwaq.composeapp.generated.resources.minus
import rwaq.composeapp.generated.resources.no_branches_to_display
import rwaq.composeapp.generated.resources.online_payment
import rwaq.composeapp.generated.resources.onlinepayemt
import rwaq.composeapp.generated.resources.order_details
import rwaq.composeapp.generated.resources.order_from_this_branch
import rwaq.composeapp.generated.resources.order_notes
import rwaq.composeapp.generated.resources.order_notes_hint
import rwaq.composeapp.generated.resources.payment_details
import rwaq.composeapp.generated.resources.pick_up_from_branch
import rwaq.composeapp.generated.resources.plus
import rwaq.composeapp.generated.resources.receiving_method
import rwaq.composeapp.generated.resources.reward_discount
import rwaq.composeapp.generated.resources.something_went_wrong
import rwaq.composeapp.generated.resources.sub_total
import rwaq.composeapp.generated.resources.total_amount
import rwaq.composeapp.generated.resources.login
import rwaq.composeapp.generated.resources.welcome
import rwaq.composeapp.generated.resources.rwaq_logo


@Composable
fun CartScreen(
    cartViewModel: CartViewModel = koinViewModel(),
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                cartViewModel.checkAuthentication()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
    val state = cartViewModel.state.collectAsStateWithLifecycle().value
    val urlHandler = LocalUriHandler.current


    // 👇 create the platform-specific launcher once
    val paymentLauncher = rememberPaymobLauncher(
        onSuccess = {
            Logger.i("success")
            cartViewModel.onPaymentFinished()   // ✅ clear cart + go back
        },
        onFailure = {
            Logger.i("onFailure $it")
            cartViewModel.onPaymentFinished()   // ✅ clear cart + go back
        },
        onPending = {
            Logger.i("onPending")
            cartViewModel.onPaymentFinished()   // ✅ clear cart + go back
        },
    )


    EventHandler(cartViewModel.effect) { effect, controller ->
        when (effect) {
            NavigateBack -> controller.navigateUp()
            is
            CartScreenUIEffect.NavigateToExternalMap,
                -> {
                urlHandler.openUri("${String.MapsUrl}${effect.location.latitude},${effect.location.longitude}")
            }

            CartScreenUIEffect.NavigateToLogin -> {
                controller.navigate(Screen.LoginScreen)
            }

            is CartScreenUIEffect.NavigateToPayment -> {
                paymentLauncher.launch(
                    clientSecret = effect.clientSecret,
                    publicKey = effect.publicKey,
                )
            }
        }
    }
    CartContent(state = state, interactionListener = cartViewModel)
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CartContent(state: CartUiState, interactionListener: CartInteractionListener) {

    // --- BACK HANDLER ---
//    BackHandler(
//        enabled = state.isSelectCarBottomSheetVisible || state.isCarDetailsBottomSheetVisible
//    ) {
//        when {
//            state.isCarDetailsBottomSheetVisible -> interactionListener.onCloseCarDetails()
//            state.isSelectCarBottomSheetVisible -> interactionListener.onCloseSelectCar()
//        }
//    }

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

    LaunchedEffect(state.selectedBranch?.workTime, state.isSelectCarBottomSheetVisible) {
        if (state.isWorkTimeOverlayVisible) {
            interactionListener.onCloseWorkTimeBottomSheet()
        }
    }
    Box(
        modifier = Modifier.fillMaxSize(),
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
                            onClick = interactionListener::onBack,
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
                        onDismiss = { },
                        onCancelClick = { },
                        hasDismissButton = false,
                        dismissOnBackPress = false,
                        dismissOnClickOutside = false,
                        actionButtons = {
                            PrimaryButton(
                                text = stringResource(Res.string.login),
                                onClick = interactionListener::onClickLogin,
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
            }
        ) {
            when {
                state.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize().weight(1f),
                        contentAlignment = Alignment.BottomCenter
                    ) { DotsProgressIndicator(dotSize = Theme.spacing._8) }
                }

                state.errorMessage != null -> {
                    EmptyOrErrorContent(
                        painter = painterResource(Res.drawable.something_went_wrong),
                        message = state.errorMessage,
                        modifier = Modifier.padding(Theme.spacing._49)
                    )
                }

                state.orders.isEmpty() && !state.isLoading -> {
                    EmptyOrErrorContent(
                        painter = painterResource(Res.drawable.empty_cart),
                        message = stringResource(Res.string.cart_empty),
                        imageSize = 200.dp,
                        modifier = Modifier.weight(1f).padding(Theme.spacing._49)
                    )
                }

                else -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 12.dp)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Spacer(modifier = Modifier.height(12.dp))

                        AnimatedVisibility(
                            visible = state.branchesErrorMessage != null
                        ) {
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
                                IconButton(onClick = interactionListener::onRetryGetBranches) {
                                    Icon(
                                        imageVector = Icons.Default.Refresh,
                                        contentDescription = "Retry loading branches",
                                        tint = Theme.colorScheme.brand.onBrand
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))

                        // 1. Order Details
                        SectionCard(
                            title = stringResource(Res.string.order_details),
                            actionText = stringResource(Res.string.add_more),
                            onActionClick = interactionListener::onAddMoreItems
                        ) {
                            state.orders.forEachIndexed { index, order ->
                                CartItemRow(order, interactionListener)
                                if (index < state.orders.lastIndex) HorizontalDivider(
                                    modifier = Modifier.padding(
                                        vertical = 12.dp
                                    ), thickness = 0.5.dp, color = Color.LightGray
                                )
                            }
                        }



                        SectionCard(title = stringResource(Res.string.payment_details)) {
                            // A. Gross Sub-Total
                            PaymentDetailRow(
                                label = stringResource(Res.string.sub_total),
                                amount = "${state.subTotal} ${stringResource(Res.string.currency_sar)}"
                            )

                            // B. Reward Discount Section (Only show if > 0)
                            if (state.rewardDiscount > 0) {
                                PaymentDetailRow(
                                    label = stringResource(Res.string.reward_discount), // Replace with stringResource(Res.string.rewards_discount)
                                    amount = "- ${state.rewardDiscount} ${stringResource(Res.string.currency_sar)}",
                                    textColor = Color(0xFF4CAF50) // Green color to indicate saving
                                )
                            }

                            HorizontalDivider(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                                thickness = 0.5.dp,
                                color = Color.LightGray
                            )

                            // C. Net Total Amount
                            PaymentDetailRow(
                                label = stringResource(Res.string.total_amount),
                                amount = "${state.cartTotal} ${stringResource(Res.string.currency_sar)}",
                                isBold = true
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                        }
//                        // 2. Payment Details
//                        SectionCard(title = stringResource(Res.string.payment_details)) {
//                            PaymentDetailRow(stringResource(Res.string.sub_total), "${state.cartTotal} ${stringResource(Res.string.currency_sar)}")
//                            HorizontalDivider(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp), thickness = 0.5.dp, color = Color.LightGray)
//                            PaymentDetailRow(stringResource(Res.string.total_amount), "${state.cartTotal} ${stringResource(Res.string.currency_sar)}", isBold = true)
//                            Spacer(modifier = Modifier.height(4.dp))
//                        }

                        // 3. Order Notes
                        SectionCard(title = stringResource(Res.string.order_notes)) {
                            OutlinedTextField(
                                value = state.orderNotes,
                                onValueChange = interactionListener::onOrderNotesChanged,
                                placeholder = {
                                    Text(
                                        stringResource(Res.string.order_notes_hint),
                                        color = Color.LightGray,
                                        style = Theme.typography.body.small
                                    )
                                },
                                textStyle = Theme.typography.body.small,
                                modifier = Modifier.fillMaxWidth().height(80.dp),
                                shape = RoundedCornerShape(8.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    unfocusedBorderColor = Color.LightGray,
                                    focusedBorderColor = Theme.colorScheme.brand.brand,
                                    unfocusedContainerColor = Color.White,
                                    focusedContainerColor = Color.White
                                )
                            )
                        }
                        // 4. RECEIVING METHOD (New Inline Section)
                        SectionCard(title = stringResource(Res.string.receiving_method)) {
                            // Pickup Option
                            ReceivingOptionRow(
                                title = stringResource(Res.string.pick_up_from_branch),
                                isSelected = !state.isDriveThru,
                                onClick = { interactionListener.onOrderTypeChanged(false) }
                            )
                            AnimatedVisibility(
                                visible = !state.isDriveThru,
                                enter = expandVertically(),
                                exit = shrinkVertically()
                            ) {

                                InlineBranchList(
                                    branches = state.allBranches,
                                    selectedBranchId = state.selectedPickupBranch?.id,
                                    onSelectBranch = interactionListener::onBranchSelected,
                                    onClickTime = interactionListener::onClickWorkTimeButton,
                                    onClickLocation = interactionListener::onClickLocationButton
                                )
                            }

                            HorizontalDivider(
                                thickness = 0.5.dp,
                                color = Color.LightGray,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )

                            // Drive Thru Option
                            ReceivingOptionRow(
                                title = stringResource(Res.string.drive_thru),
                                isSelected = state.isDriveThru,
                                onClick = { interactionListener.onOrderTypeChanged(true) }
                            )
                            AnimatedVisibility(
                                visible = state.isDriveThru,
                                enter = expandVertically(),
                                exit = shrinkVertically()
                            ) {
                                Column {
                                    InlineBranchList(
                                        branches = state.driveThruBranches,
                                        selectedBranchId = state.selectedDriveThruBranch?.id,
                                        onSelectBranch = interactionListener::onBranchSelected,
                                        onClickTime = interactionListener::onClickWorkTimeButton,
                                        onClickLocation = interactionListener::onClickLocationButton
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        stringResource(Res.string.choose_car),
                                        style = Theme.typography.body.small,
                                        color = Theme.colorScheme.primary.primary,
                                        modifier = Modifier.padding(bottom = 8.dp)
                                    )

                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
                                            .rippleClickable { interactionListener.onOpenSelectCar() }
                                            .padding(12.dp)
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            if (state.selectedCar.id.isNotEmpty()) {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    // Car Image or Icon
                                                    if (state.selectedCar.imageUrl.isNotEmpty()) {
                                                        AsyncImage(
                                                            model = state.selectedCar.imageUrl,
                                                            contentDescription = null,
                                                            modifier = Modifier.size(30.dp)
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
                                                Text(
                                                    stringResource(Res.string.choose_car),
                                                    style = Theme.typography.body.medium,
                                                    color = Color.Gray
                                                )
                                            }
                                            Icon(
                                                Icons.Default.ArrowDropDown,
                                                contentDescription = null,
                                                tint = Color.Gray
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // 5. Payment Methods
                        SectionCard(title = null) {
                            PaymentMethodRow(
                                title = stringResource(Res.string.online_payment),
                                icon = painterResource(Res.drawable.onlinepayemt),
                                isSelected = state.selectedPaymentMethod == PaymentMethod.ONLINE,
                                onClick = { interactionListener.onPaymentMethodChanged(PaymentMethod.ONLINE) }
                            )
                            HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray)
                            PaymentMethodRow(
                                title = stringResource(Res.string.cash),
                                icon = painterResource(Res.drawable.cash),
                                isSelected = state.selectedPaymentMethod == PaymentMethod.CASH,
                                onClick = { interactionListener.onPaymentMethodChanged(PaymentMethod.CASH) }
                            )
                        }

                        // Checkout Button
                        PrimaryButton(
                            text = stringResource(Res.string.checkout_order),
                            onClick = interactionListener::onPlaceOrder,
                            isEnabled = !state.isSendingOrderLoading || state.branches.isNotEmpty(),
                            isLoading = state.isSendingOrderLoading,
                            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                        )

                        Spacer(modifier = Modifier.height(PADDING_BOTTOM_WITH_NAV_VISIBLE.dp))
                    }
                }
            }
        }

        // --- BOTTOM SHEETS (ONLY CAR & ADD CAR) ---

        AnimatedVisibility(
            visible = state.isWorkTimeOverlayVisible,
            enter = slideInVertically(initialOffsetY = { it }),
            exit = slideOutVertically(targetOffsetY = { it }),
            modifier = Modifier.align(Alignment.BottomCenter)
                .padding(bottom = Dimensions.PADDING_BOTTOM_WITH_NAV_VISIBLE.dp)
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
                .padding(bottom = Dimensions.PADDING_BOTTOM_WITH_NAV_VISIBLE.dp)
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
                .padding(bottom = Dimensions.PADDING_BOTTOM_WITH_NAV_VISIBLE.dp)
        ) {
            AddCarWizardBottomSheet(
                step = state.addCarStep,
                onCancel = interactionListener::onCloseCarDetails,
                onNext = interactionListener::onAddCarNextStep,
                carBrands = state.carBrands,
                carColors = state.carColors,
                selectedBrand = state.selectedCarBrand,
                selectedColor = state.selectedCarColor,
                carNumber = state.newCarNumber,
                onBrandSelect = interactionListener::onCarBrandSelected,
                onColorSelect = interactionListener::onCarColorSelected,
                onCarNumberChange = interactionListener::onCarNumberChanged
            )
        }
    }
}

// --- HELPER COMPONENTS ---

@Composable
fun ReceivingOptionRow(title: String, isSelected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .rippleClickable { onClick() }
            .padding(vertical = 12.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Checkbox/Radio Circle
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .border(
                    2.dp,
                    if (isSelected) Theme.colorScheme.brand.brand else Color.Gray,
                    CircleShape
                )
                .background(if (isSelected) Theme.colorScheme.brand.brand else Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            if (isSelected) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(title, style = Theme.typography.body.medium, color = Theme.colorScheme.primary.primary)
    }
}


@Composable
fun TabButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .height(40.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(if (isSelected) Theme.colorScheme.brand.brand else Color.Transparent)
            .rippleClickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = Theme.typography.body.small,
            color = if (isSelected) Color.White else Theme.colorScheme.primary.primary
        )
    }
}

// Custom Sheet to match the style of SelectCarBottomSheet (White container at bottom)
@Composable
fun CheckoutBottomSheet(
    state: CartUiState,
    onClose: () -> Unit,
    onOrderTypeChanged: (Boolean) -> Unit,
    onBranchSelected: (String) -> Unit,
    onProceed: () -> Unit,
) {
    // Semi-transparent scrim
    Box(
        modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.5f))
            .clickable { onClose() }
    )

    // Sheet Content
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.85f)
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .background(Color.White)
            .clickable(enabled = false) {} // Prevent click through
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(Res.string.choose_branch),
            style = Theme.typography.headline.medium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Toggles
        Row(
            modifier = Modifier.fillMaxWidth()
                .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp)).padding(4.dp)
        ) {
            TabButton(
                "Pickup from Branch",
                !state.isDriveThru,
                { onOrderTypeChanged(false) },
                Modifier.weight(1f)
            )
            TabButton(
                "Drive Thru",
                state.isDriveThru,
                { onOrderTypeChanged(true) },
                Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (state.branches.isEmpty()) {
            Box(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(stringResource(Res.string.no_branches_to_display))
            }
        } else {
            // We wrap this in a Box to allow weight distribution
            Box(modifier = Modifier.weight(1f)) {
                SelectedBranchSection(
                    options = state.branches,
                    isDriveThru = state.isDriveThru,
                    selectedOption = state.selectedBranch ?: state.branches.first(),
                    selectedCar = state.selectedCar,
                    onSelectCar = { /* Not needed here */ },
                    onOptionSelected = { onBranchSelected(it.id) },
                    onClickTime = { /* Optional */ },
                    onClickLocation = { /* Optional */ }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            PrimaryButton(
                text = if (state.isDriveThru) "Next: Choose Car" else stringResource(Res.string.order_from_this_branch),
                onClick = onProceed,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun CartItemRow(order: Order, listener: CartInteractionListener) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Row(
                modifier = Modifier,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFFAFAFA)),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = order.imageUrl,
                        contentDescription = order.name,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp)
                    )

                }
                Text(
                    "${order.name} (${order.size})",
                    style = Theme.typography.body.extraSmall,
                    color = Theme.colorScheme.primary.primary,
                    modifier = Modifier
                )
            }
            Text(
                "${order.totalPrice} " + stringResource(Res.string.currency_sar),
                style = Theme.typography.body.extraSmall,
                color = Theme.colorScheme.primary.primary,

                )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Main Item Counter
            QuantitySelector(
                count = order.count,
                onIncrease = { listener.onIncreaseOrderCount(order) },
                onDecrease = { listener.onDecreaseOrderCount(order) },
                // If count is 1, hitting delete calls onDeleteOrder
                onDelete = { listener.onDeleteOrder(order.id) },
                isBig = true
            )
        }

        // Extensions
        if (order.extension.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                stringResource(Res.string.extensions),
                style = Theme.typography.body.extraSmall,
                color = Theme.colorScheme.primary.primary,
            )
            order.extension.forEach { ext ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        ext.name,
                        style = Theme.typography.body.extraSmall,
                        color = Theme.colorScheme.shadePrimary
                    )
                    // Extension Counter
                    QuantitySelector(
                        count = ext.count,
                        onIncrease = { listener.onIncreaseExtension(order, ext) },
                        onDecrease = { listener.onDecreaseExtension(order, ext) },
                        // Extensions behave same way (delete icon if 1)
                        onDelete = { listener.onDecreaseExtension(order, ext) },
                        isBig = false
                    )
                }
            }
        }
    }
}

@Composable
fun QuantitySelector(
    count: Int,
    maxCount: Int = Int.MAX_VALUE,
    onDecrease: () -> Unit,
    onIncrease: () -> Unit,
    onDelete: (() -> Unit)? = null, // Added onDelete parameter
    isBig: Boolean = false,
) {
    // Adjusted height to match your CounterButton logic roughly, or keep standard
    // Small: 40dp (Standard), Big: 50dp
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
        // --- LOGIC: Determine Icon and Action ---
        val isDeleteState = count == 1 && onDelete != null

        val leftIconPainter = if (isDeleteState) {
            rememberVectorPainter(Icons.Outlined.Delete)
        } else {
            painterResource(Res.drawable.minus)
        }

        val leftIconTint = if (isDeleteState) Color(0xffff7675) // Optional: Make delete red
        else if (count > 0) Theme.colorScheme.primary.primary
        else Theme.colorScheme.shadePrimary

        val leftAction = if (isDeleteState) {
            { onDelete.invoke() }
        } else {
            onDecrease
        }

        // --- LEFT BUTTON (Minus or Delete) ---
        com.khater.rwaq.designSystem.component.icon.IconButton(
            painter = leftIconPainter,
            contentDescription = if (isDeleteState) "Delete item" else "Decrease amount",
            tint = leftIconTint,
            onClick = { leftAction() },
            modifier = Modifier.size(iconSize).clip(CircleShape)
        )

        // --- COUNT TEXT ---
        Text(
            text = "$count",
            modifier = Modifier.padding(horizontal = 8.dp),
            style = if (isBig) Theme.typography.body.medium else Theme.typography.body.small,
            color = Theme.colorScheme.shadePrimary,
        )

        // --- RIGHT BUTTON (Plus) ---
        com.khater.rwaq.designSystem.component.icon.IconButton(
            painter = painterResource(Res.drawable.plus),
            contentDescription = "Plus amount",
            tint = if (count < maxCount) Theme.colorScheme.primary.primary
            else Theme.colorScheme.shadePrimary,
            onClick = onIncrease,
            modifier = Modifier.size(iconSize).clip(CircleShape)
        )
    }
}


//
//@Composable
//fun CounterButton(
//    count: Int,
//    onIncrease: () -> Unit,
//    onDecrease: () -> Unit,
//    onDelete: (() -> Unit)? = null,
//    isSmall: Boolean = false,
//) {
//    val height = if (isSmall) 25.dp else 30.dp
//    val iconSize = if (isSmall) 14.dp else 18.dp
//    val textSize = if (isSmall) 10.sp else 14.sp
//    val padding = if (isSmall) 8.dp else 12.dp
//
//    Row(verticalAlignment = Alignment.CenterVertically) {
//        // Left Button (Delete or Minus)
//        Box(
//            modifier = Modifier
//                .size(height)
//                .clip(RoundedCornerShape(8.dp))
//                .then(
//                    if (isSmall)
//                        Modifier.border(
//                            1.dp,
//                            Theme.colorScheme.shadePrimary.copy(0.2f),
//                            RoundedCornerShape(8.dp)
//                        )
//                    else Modifier.background(Theme.colorScheme.brand.brand.copy(0.6f))
//                )
//
//                .clickable {
//                    // Logic: Show Delete if count == 1, else Decrease
//                    if (count > 1) onDecrease() else onDelete?.invoke() ?: onDecrease()
//                },
//            contentAlignment = Alignment.Center
//        ) {
//            if (count == 1 && onDelete != null) {
//                Icon(
//                    Icons.Outlined.Delete,
//                    contentDescription = "Delete",
//                    tint = if (isSmall) Color.Black.copy(0.4f) else Color.White,
//                    modifier = Modifier.size(iconSize)
//                )
//            } else {
//                Icon(
//                    Icons.Default.Remove,
//                    contentDescription = "Remove",
//                    tint = if (isSmall) Color.Black else Color.White,
//                    modifier = Modifier.size(iconSize)
//                )
//            }
//        }
//
//        Text(
//            text = count.toString(),
//            modifier = Modifier.padding(horizontal = padding),
//            style = Theme.typography.title.medium.copy(fontSize = textSize),
//            fontWeight = FontWeight.Bold
//        )
//
//        // Right Button (Add)
//        Box(
//            modifier = Modifier
//                .size(height)
//                .clip(RoundedCornerShape(8.dp))
//                .then(
//                    if (isSmall)
//                        Modifier.border(
//                            1.dp,
//                            Theme.colorScheme.shadePrimary.copy(0.2f),
//                            RoundedCornerShape(8.dp)
//                        )
//                    else Modifier.background(Theme.colorScheme.brand.brand.copy(0.6f))
//                ).clickable { onIncrease() },
//            contentAlignment = Alignment.Center
//        ) {
//            Icon(
//                Icons.Default.Add,
//                contentDescription = "Add",
//                tint = if (isSmall) Color.Black.copy(0.4f) else Color.White,
//                modifier = Modifier.size(iconSize)
//            )
//        }
//    }
//}


@Composable
fun PaymentDetailRow(
    label: String,
    amount: String,
    isBold: Boolean = false,
    textColor: Color = Theme.colorScheme.primary.primary,
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            label,
            style = if (isBold) Theme.typography.body.medium else Theme.typography.body.small,
            color = Theme.colorScheme.primary.primary,
        )
        Text(
            amount,
            style = if (isBold) Theme.typography.body.medium else Theme.typography.body.small,
            color = if (isBold) Theme.colorScheme.primary.primary else textColor,
        )
    }
}

@Composable
fun PaymentMethodRow(title: String, icon: Painter, isSelected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .rippleClickable { onClick() }
            .padding(vertical = 12.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Text(title, style = Theme.typography.body.small, modifier = Modifier.weight(1f))

        // Custom Selection Box
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(if (isSelected) Theme.colorScheme.brand.brand else Color.Transparent)
                .border(
                    1.dp,
                    if (isSelected) Theme.colorScheme.brand.brand else Color.Gray,
                    RoundedCornerShape(4.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isSelected) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}