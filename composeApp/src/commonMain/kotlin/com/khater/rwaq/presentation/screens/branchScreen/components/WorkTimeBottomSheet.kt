package com.khater.rwaq.presentation.screens.branchScreen.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.khater.rwaq.designSystem.component.icon.Icon
import com.khater.rwaq.designSystem.theme.theme.Theme
import com.khater.rwaq.presentation.screens.branchScreen.uiState.BranchWorkTimeUiState
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import rwaq.composeapp.generated.resources.Res
import rwaq.composeapp.generated.resources.available_all_day
import rwaq.composeapp.generated.resources.closes
import rwaq.composeapp.generated.resources.ic_cancle
import rwaq.composeapp.generated.resources.ic_location
import rwaq.composeapp.generated.resources.ic_time
import rwaq.composeapp.generated.resources.no_schedule_available
import rwaq.composeapp.generated.resources.opens
import rwaq.composeapp.generated.resources.work_time

@Composable
fun AttachmentsBottomSheet(
    modifier: Modifier = Modifier,
    workTimeUiState:  List<BranchWorkTimeUiState>,
    onCancel:() -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .dropShadow(
                shape = RoundedCornerShape(
                    topEnd = Theme.spacing._12,
                    topStart = Theme.spacing._12
                ), shadow = Shadow(
                    radius = Theme.spacing._12,
                    spread = 0.dp,
                    color = Color.Black.copy(alpha = .06f),
                    offset = DpOffset(0.dp, (-2).dp)
                )
            )
            .background(
                color = Theme.colorScheme.brand.onBrand,
                shape = RoundedCornerShape(topEnd = Theme.spacing._12, topStart = Theme.spacing._12)
            )
            .padding(
                start = Theme.spacing._16,
                end = Theme.spacing._16,
                top = Theme.spacing._16,
                bottom = Theme.spacing._49
            ),
        verticalArrangement = Arrangement.spacedBy(Theme.spacing._12)
    ) {
        Surface(
            color = Theme.colorScheme.brand.onBrand
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_cancle),
                contentDescription = "Cancel",
                modifier = Modifier.size(32.dp)
                    .clickable(
                        onClick = onCancel
                    )

            )

            AttachmentBottomSheetContent(
                modifier = Modifier.fillMaxWidth(),
                workTimeUiState  = workTimeUiState
            )
        }




    }
}

@Composable
private fun AttachmentBottomSheetContent(
    workTimeUiState:  List<BranchWorkTimeUiState>,
    modifier: Modifier = Modifier,
) {

// 1. Manage Selection State
    // Default to the first day, or null if list is empty
    var selectedDay by remember { mutableStateOf(workTimeUiState.firstOrNull()) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(Res.string.work_time),
            style = Theme.typography.title.medium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // 2. The Days Row (FlowRow)
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            workTimeUiState.forEach { state ->
                DayChip(
                    text = state.day,
                    isSelected = state == selectedDay,
                    onClick = { selectedDay = state }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 3. The Detail View (Animated)
        // We handle the null case if the list is empty
        if (selectedDay != null) {
            TimeDetailCard(state = selectedDay!!)
        } else {
            Text(stringResource(Res.string.no_schedule_available), style = Theme.typography.title.medium)
        }
    }
}


@Composable
private fun DayChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) Theme.colorScheme.brand.brand else Theme.colorScheme.brand.brand.copy(0.2f)
    val contentColor = if (isSelected) Theme.colorScheme.brand.onBrand else Theme.colorScheme.primary.primary

    Box(
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .clip(CircleShape)
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = Theme.typography.body.medium,
            color = contentColor,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

// Component: The Card showing the time
@Composable
private fun TimeDetailCard(state: BranchWorkTimeUiState) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Theme.colorScheme.brand.brand.copy(0.2f)
        ),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        // This adds a smooth fade/slide animation when switching days
        AnimatedContent(
            targetState = state,
            label = "TimeChangeAnimation"
        ) { targetState ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (targetState.isAvailableAllDay) {
                    // 24 Hour View
                    TimeInfoRow(
                        icon = painterResource(Res.drawable.ic_time),
                        label =stringResource(Res.string.available_all_day),
                        highlight = true
                    )
                } else {
                    // Start / End Time View
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        TimeColumn(label = stringResource(Res.string.opens), time = targetState.startTime)

                        // Divider Line
                        VerticalDivider(
                            modifier = Modifier.height(30.dp),
                            color = Theme.colorScheme.shadePrimary
                        )

                        TimeColumn(label = stringResource(Res.string.closes), time = targetState.endTime)
                    }
                }
            }
        }
    }
}

@Composable
private fun TimeColumn(label: String, time: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally
    , verticalArrangement = Arrangement.spacedBy(Theme.spacing._4)) {
        Text(
            text = label.uppercase(),
            style = Theme.typography.body.small,
            color = Theme.colorScheme.shadePrimary
        )
         Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(Res.drawable.ic_time),
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = Theme.colorScheme.primary.primary
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = time.ifEmpty { "--:--" },
                style = Theme.typography.body.medium,
                color = Theme.colorScheme.primary.primary
            )
        }
    }
}

@Composable
private fun TimeInfoRow(icon: Painter, label: String, highlight: Boolean = false) {
    Column(horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Theme.spacing._8)) {
         Icon(
            painter = icon,
            contentDescription = null,
            modifier = Modifier.size(30.dp),
            tint = Theme.colorScheme.primary.primary
        )
         Text(
            text = label,
            style = Theme.typography.title.medium,
            color = Theme.colorScheme.primary.primary
        )
    }
}