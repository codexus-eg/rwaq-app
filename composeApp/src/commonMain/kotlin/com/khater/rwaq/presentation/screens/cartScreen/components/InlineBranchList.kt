package com.khater.rwaq.presentation.screens.cartScreen.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.khater.rwaq.designSystem.component.text.Text
import com.khater.rwaq.designSystem.theme.theme.Theme
import com.khater.rwaq.designSystem.util.rippleIndication
import com.khater.rwaq.presentation.screens.branchScreen.components.BranchIcon
import com.khater.rwaq.presentation.screens.branchScreen.uiState.BranchUiState
import com.khater.rwaq.presentation.screens.branchScreen.uiState.LocationUiState
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import rwaq.composeapp.generated.resources.Res
import rwaq.composeapp.generated.resources.distance_from_you
import rwaq.composeapp.generated.resources.ic_location
import rwaq.composeapp.generated.resources.ic_time

@Composable
fun InlineBranchList(
    modifier: Modifier = Modifier,
    branches: List<BranchUiState>,
    selectedBranchId: String?,
    onSelectBranch: (String) -> Unit,
    onClickLocation: (LocationUiState) -> Unit ,
    onClickTime: (String) -> Unit ,
    textColor: Color = Theme.colorScheme.primary.primary,
    textStyle: TextStyle = Theme.typography.body.large,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = Theme.spacing._16, vertical = Theme.spacing._8)
    ) {
        branches.forEach { option ->
            // key() ensures Compose knows exactly which item is which
            key(option.id) {
                val selected = option.id == selectedBranchId

                val borderColor by animateColorAsState(
                    targetValue = if (selected) Theme.colorScheme.brand.brand else Theme.colorScheme.border.primary,
                    animationSpec = tween(300),
                    label = "BorderColor"
                )
                val radioBorderColor by animateColorAsState(
                    targetValue = if (selected) Theme.colorScheme.brand.brand else Theme.colorScheme.secondary.secondary,
                    animationSpec = tween(300),
                    label = "RadioColor"
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = Theme.spacing._8)
                        .clip(RoundedCornerShape(Theme.spacing._12))
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = rippleIndication()
                        ) { onSelectBranch(option.id) }
                        .border(1.dp, borderColor, RoundedCornerShape(Theme.spacing._12))
                        .padding(Theme.spacing._16),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Theme.spacing._12)
                ) {
                    // --- Radio Button ---
                    Box(
                        modifier = Modifier
                            .size(Theme.spacing._24)
                            .clip(CircleShape)
                            .border(1.dp, radioBorderColor, CircleShape)
                            .padding(4.dp), // Padding creates the gap between border and dot
                        contentAlignment = Alignment.Center
                    ) {
                        // Use simple alpha animation instead of Visibility for smoother list performance
                        androidx.compose.animation.AnimatedVisibility(
                            visible = selected,
                            enter = androidx.compose.animation.fadeIn(tween(200)),
                            exit = androidx.compose.animation.fadeOut(tween(200))
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape)
                                    .background(Theme.colorScheme.brand.brand)
                            )
                        }
                    }

                    // --- Content ---
                    InlineBranchCard(
                        branchName = option.branchName,
                        textColor = textColor,
                        textStyle = textStyle,
                        option = option,
                        onClickLocation = { onClickLocation(option.branchLocation) },
                        onClickTime = { onClickTime(option.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun InlineBranchCard(
    branchName: String,
    textColor: Color,
    textStyle: TextStyle,
    option: BranchUiState,
    onClickLocation: () -> Unit,
    onClickTime: () -> Unit
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(Theme.spacing._8)) {
            Text(
                text = branchName,
                color = textColor,
                style = textStyle
            )
            // Branch Status (Open/Closed)
            Text(
                text = stringResource(option.branchStatus.status),
                style = Theme.typography.body.extraSmall,
                color = textColor,
                modifier = Modifier
                    .clip(RoundedCornerShape(Theme.spacing._8))
                    .background(option.branchStatus.color.copy(0.4f))
                    .padding(horizontal = Theme.spacing._8, vertical = Theme.spacing._4)
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Column(
            verticalArrangement = Arrangement.spacedBy(Theme.spacing._12),
            horizontalAlignment = Alignment.End
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Theme.spacing._12)
            ) {
                BranchIcon(
                    scale = 1.2f,
                    painter = painterResource(Res.drawable.ic_location),
                    onClick = onClickLocation
                )
                BranchIcon(
                    painter = painterResource(Res.drawable.ic_time),
                    onClick = onClickTime
                )
            }
//            Text(
//                text = stringResource(Res.string.distance_from_you, option.distanceAwayFromYou),
//                style = Theme.typography.body.extraSmall,
//                color = textColor,
//            )
        }
    }
}