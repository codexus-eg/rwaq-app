package com.khater.rwaq.presentation.screens.branchScreen.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key.Companion.R
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.khater.rwaq.designSystem.component.icon.Icon
import com.khater.rwaq.designSystem.component.text.Text
import com.khater.rwaq.designSystem.theme.theme.Theme
import com.khater.rwaq.designSystem.util.rippleIndication
import com.khater.rwaq.presentation.screens.branchScreen.uiState.BranchUiState
import com.khater.rwaq.presentation.screens.branchScreen.uiState.BranchWorkTimeUiState
import com.khater.rwaq.presentation.screens.branchScreen.uiState.CarUiState
import com.khater.rwaq.presentation.screens.branchScreen.uiState.LocationUiState
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import rwaq.composeapp.generated.resources.Res
import rwaq.composeapp.generated.resources.car
import rwaq.composeapp.generated.resources.choose
import rwaq.composeapp.generated.resources.choose_car
import rwaq.composeapp.generated.resources.distance_from_you
import rwaq.composeapp.generated.resources.ic_location
import rwaq.composeapp.generated.resources.ic_time
import rwaq.composeapp.generated.resources.select_branch

@Composable
fun SelectedBranchSection(
    modifier: Modifier = Modifier,
    isDriveThru: Boolean = false,
    textColor: Color = Theme.colorScheme.primary.primary,
    textStyle: TextStyle = Theme.typography.body.large,
    options: List<BranchUiState>,
    selectedOption: BranchUiState,
    selectedCar: CarUiState,
    onSelectCar: () -> Unit,
    onOptionSelected: (BranchUiState) -> Unit = {},
    onClickLocation: (LocationUiState) -> Unit = {},
    onClickTime: (String) -> Unit = {},
) {
    Column(
        modifier = modifier.fillMaxWidth()
            .clip(RoundedCornerShape(topStart = Theme.spacing._12, topEnd = Theme.spacing._12))
             .padding(horizontal = Theme.spacing._16)
    ) {

        if (isDriveThru) {
            Text(
                text = stringResource(Res.string.choose_car),
                color = Theme.colorScheme.primary.primary,
                style = Theme.typography.title.small,
                modifier = Modifier.padding(bottom = Theme.spacing._12, top = Theme.spacing._24)
            )
            ChooseCarSection(
                selectedCar = selectedCar,
                onClickChooseCar = onSelectCar
            )
        }
        Text(
            text = stringResource(Res.string.select_branch),
            color = Theme.colorScheme.primary.primary,
            style = Theme.typography.title.small,
            modifier = Modifier.padding(vertical = Theme.spacing._12)
        )
        options.forEach { option ->
            val selected = option == selectedOption
            val borderColor by animateColorAsState(
                targetValue = if (selected) Theme.colorScheme.brand.brand else Theme.colorScheme.border.primary,
                animationSpec = tween(700),
                label = "Border Color"
            )
            val borderCircleColor by animateColorAsState(
                targetValue = if (selected) Theme.colorScheme.brand.brand else Theme.colorScheme.secondary.secondary,
                animationSpec = tween(700),
                label = "Border Circle Color"
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = Theme.spacing._12)
                    .clip(RoundedCornerShape(Theme.spacing._12))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rippleIndication()
                    ) { onOptionSelected(option) }
                    .border(1.dp, borderColor, RoundedCornerShape(Theme.spacing._12))
                    .padding(Theme.spacing._16),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Theme.spacing._12)
            ) {
                Box(
                    modifier = Modifier
                        .size(Theme.spacing._24)
                        .clip(CircleShape)
                        .border(1.dp, borderCircleColor, CircleShape)
                        .padding(3.dp),
                    contentAlignment = Alignment.Center
                ) {
                    AnimatedContent(
                        targetState = selected,
                        contentAlignment = Alignment.Center
                    ) {
                        when (it) {
                            true -> Box(
                                modifier = Modifier
                                    .size(18.dp)
                                    .clip(CircleShape)
                                    .background(Theme.colorScheme.brand.brand),
                            )
                            false -> {}
                        }
                    }
                }
                BranchCard(
                    branchName = option.branchName,
                    textColor = textColor,
                    textStyle = textStyle,
                    option = option,
                    onClickLocation = { onClickLocation(option.branchLocation) },
                    onClickTime = { onClickTime(option.id) }

                )

//                Column(verticalArrangement = Arrangement.spacedBy(Theme.spacing._8)) {
//                    Text(
//                        text = option.branchName,
//                        color = textColor,
//                        style = textStyle
//                    )
//                    Text(
//                        text = stringResource(option.branchStatus.status),
//                        style = Theme.typography.body.extraSmall,
//                        color = textColor,
//                        modifier = Modifier.clip(RoundedCornerShape(Theme.spacing._8))
//                            .background(option.branchStatus.color.copy(0.4f))
//                            .padding(horizontal = Theme.spacing._8, vertical = Theme.spacing._4)
//                    )
//                }
//                Spacer(modifier = Modifier.weight(1f))
//                Column(
//                    verticalArrangement = Arrangement.spacedBy(Theme.spacing._12),
//                    horizontalAlignment = Alignment.End
//                ) {
//                    Row(
//                        verticalAlignment = Alignment.CenterVertically,
//                        horizontalArrangement = Arrangement.spacedBy(Theme.spacing._12)
//                    ) {
//                        BranchIcon(
//                            scale = 1.2f,
//                            painter = painterResource(Res.drawable.ic_location),
//                            onClick = { onClickLocation(option.branchLocation) }
//                        )
//
//                        BranchIcon(
//                            painter = painterResource(Res.drawable.ic_time),
//                            onClick = { onClickTime(option.id) }
//                        )
//                    }
//
//                    Text(
//                        text = stringResource(
//                            Res.string.distance_from_you,
//                            option.distanceAwayFromYou
//                        ),
//                        style = Theme.typography.body.extraSmall,
//                        color = textColor,
//                    )
//                }
            }
        }
    }
}


@Composable
fun BranchCard(
    branchName: String,
    textColor: Color,
    textStyle: TextStyle,
    option: BranchUiState,
    onClickLocation:()-> Unit,
    onClickTime:()-> Unit
){
    Row(modifier = Modifier.fillMaxWidth()){

    Column(verticalArrangement = Arrangement.spacedBy(Theme.spacing._8)) {
        Text(
            text =  branchName,
            color = textColor,
            style = textStyle
        )
        Text(
            text = stringResource(option.branchStatus.status),
            style = Theme.typography.body.extraSmall,
            color = textColor,
            modifier = Modifier.clip(RoundedCornerShape(Theme.spacing._8))
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

//        Text(
//            text = stringResource(
//                Res.string.distance_from_you,
//                option.distanceAwayFromYou
//            ),
//            style = Theme.typography.body.extraSmall,
//            color = textColor,
//        )
    }
    }
}