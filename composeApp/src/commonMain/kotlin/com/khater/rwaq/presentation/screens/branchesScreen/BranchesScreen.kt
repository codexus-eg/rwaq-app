package com.khater.rwaq.presentation.screens.branchesScreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.khater.rwaq.designSystem.component.indicator.DotsProgressIndicator
import com.khater.rwaq.designSystem.component.scaffold.HomeScaffold
import com.khater.rwaq.designSystem.theme.theme.Theme
import com.khater.rwaq.presentation.composables.EmptyOrErrorContent
import com.khater.rwaq.presentation.composables.EventHandler
import com.khater.rwaq.presentation.composables.RwaqBackButton
import com.khater.rwaq.presentation.composables.RwaqTopBar
import com.khater.rwaq.presentation.navigation.Screen
import com.khater.rwaq.presentation.screens.branchScreen.BranchContent
import com.khater.rwaq.presentation.screens.branchScreen.components.AttachmentsBottomSheet
import com.khater.rwaq.presentation.screens.branchScreen.components.BranchCard
import com.khater.rwaq.presentation.screens.branchScreen.components.SelectedBranchSection
import com.khater.rwaq.presentation.screens.branchScreen.uiState.BranchScreenInteractionListener
import com.khater.rwaq.presentation.screens.branchScreen.uiState.BranchScreenUiEffect
import com.khater.rwaq.presentation.screens.branchesScreen.uiState.BranchesScreenInteractionListener
import com.khater.rwaq.presentation.screens.branchesScreen.uiState.BranchesScreenUiEffect
import com.khater.rwaq.presentation.screens.branchesScreen.uiState.BranchesUiState
import com.khater.rwaq.presentation.util.MapsUrl
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import rwaq.composeapp.generated.resources.Res
import rwaq.composeapp.generated.resources.branches
import rwaq.composeapp.generated.resources.choose_branch
import rwaq.composeapp.generated.resources.empty_car
import rwaq.composeapp.generated.resources.no_branches_to_display
import rwaq.composeapp.generated.resources.something_went_wrong

@Composable
fun BranchesScreen(branchViewModel: BranchesViewModel = koinViewModel()) {
    val state = branchViewModel.state.collectAsStateWithLifecycle().value
    val urlHandler = LocalUriHandler.current
    EventHandler(branchViewModel.effect) { effect, controller ->
        when (effect) {

            is BranchesScreenUiEffect.NavigateToExternalMap -> {
                urlHandler.openUri("${String.MapsUrl}${effect.location.latitude},${effect.location.longitude}")
            }

            BranchesScreenUiEffect.NavigateBack -> {
                controller.navigateUp()
            }

        }
    }

    BranchesContent(
        state = state,
        interactionListener = branchViewModel as BranchesScreenInteractionListener
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BranchesContent(
    state: BranchesUiState,
    interactionListener: BranchesScreenInteractionListener,
) {
    BackHandler(
        enabled = state.isWorkTimeOverlayVisible
    ) {
        when {
            state.isWorkTimeOverlayVisible -> interactionListener.onCloseWorkTimeBottomSheet()
        }
    }
    Box(
        modifier = Modifier.fillMaxSize().imePadding(),
        contentAlignment = Alignment.Center
    ) {
        HomeScaffold(
            hasStatusBarColor = true,
            snackBarState = state.snackBar,
            topBar = {
                RwaqTopBar(
                    containerColor = Theme.colorScheme.brand.brand,
                    leadingContent = {
                        RwaqBackButton(
                            onClick = interactionListener::onBack,
                            hasDropShadow = false,
                            tint = Theme.colorScheme.brand.onBrand

                        )
                    },
                    middleContent = {
                        Text(
                            text = stringResource(Res.string.branches),
                            color = Theme.colorScheme.brand.onBrand,
                            style = Theme.typography.headline.medium,
                            modifier = Modifier.offset(x = (-22).dp)
                        )
                    },
                    isCenterAligned = true,
                )
            }
        ) {
            when {
                state.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize() ,
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

                state.branches.isEmpty() && !state.isLoading -> {
                    EmptyOrErrorContent(
                        painter = painterResource(Res.drawable.empty_car),
                        message = stringResource(Res.string.no_branches_to_display),
                        modifier = Modifier.padding(Theme.spacing._49)
                    )
                }

                state.branches.isNotEmpty() -> {

                    state.branches.forEach {
                        Box(
                            modifier = Modifier.fillMaxWidth().padding(12.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .border(
                                    1.dp,
                                    Theme.colorScheme.shadePrimary,
                                    RoundedCornerShape(12.dp)
                                ).padding(12.dp)
                        ) {
                            BranchCard(
                                branchName = it.branchName,
                                textColor = Theme.colorScheme.primary.primary,
                                textStyle = Theme.typography.body.large,
                                option = it,
                                onClickLocation = { interactionListener.onClickLocationButton(it.branchLocation) },
                                onClickTime = { interactionListener.onClickWorkTimeButton(it.id) }
                            )
                        }


                    }


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
    }
}