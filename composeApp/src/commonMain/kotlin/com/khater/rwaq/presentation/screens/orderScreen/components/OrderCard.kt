package com.khater.rwaq.presentation.screens.orderScreen.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.khater.rwaq.designSystem.theme.theme.Theme
import com.khater.rwaq.presentation.screens.cartScreen.components.SectionCard
import com.khater.rwaq.presentation.screens.orderScreen.uiState.DeliveryStatusUi
import com.khater.rwaq.presentation.screens.orderScreen.uiState.OrderItemUiModel
import com.khater.rwaq.presentation.screens.orderScreen.uiState.OrderTypeUi
import com.khater.rwaq.presentation.screens.orderScreen.uiState.OrderUiModel
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import rwaq.composeapp.generated.resources.Res
import rwaq.composeapp.generated.resources.assigned_driver
import rwaq.composeapp.generated.resources.branch_label
import rwaq.composeapp.generated.resources.car
import rwaq.composeapp.generated.resources.car_details
import rwaq.composeapp.generated.resources.cart_icon
import rwaq.composeapp.generated.resources.cashback
import rwaq.composeapp.generated.resources.currency_sar
import rwaq.composeapp.generated.resources.delivery_fee
import rwaq.composeapp.generated.resources.delivery_status
import rwaq.composeapp.generated.resources.ic_check_circle
import rwaq.composeapp.generated.resources.ic_location
import rwaq.composeapp.generated.resources.notes_label
import rwaq.composeapp.generated.resources.onlinepayemt
import rwaq.composeapp.generated.resources.order_address
import rwaq.composeapp.generated.resources.order_details
import rwaq.composeapp.generated.resources.order_items_count
import rwaq.composeapp.generated.resources.order_type
import rwaq.composeapp.generated.resources.payment_method
import rwaq.composeapp.generated.resources.payment_status
import rwaq.composeapp.generated.resources.pick_up_from_branch
import rwaq.composeapp.generated.resources.pick_up_from_car
import rwaq.composeapp.generated.resources.reward_item
import rwaq.composeapp.generated.resources.status_delivered
import rwaq.composeapp.generated.resources.status_in_transit
import rwaq.composeapp.generated.resources.status_not_assigned
import rwaq.composeapp.generated.resources.status_processing
import rwaq.composeapp.generated.resources.status_shipped

@Composable
fun OrderCard(
    order: OrderUiModel,
    modifier: Modifier = Modifier,
) {
    SectionCard(
        title = null,
        modifier = modifier.padding(bottom = 8.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            OrderHeader(order)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                MetaPill(
                    label = stringResource(Res.string.order_type),
                    value = order.pickupTypeLabel.ifBlank { stringResource(order.orderType.label) },
                    color = order.orderType.color,
                    icon = order.orderType.icon,
                    modifier = Modifier.weight(1f)
                )
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "${order.totalPrice} ${stringResource(Res.string.currency_sar)}",
                        style = Theme.typography.body.large.copy(fontWeight = FontWeight.Bold),
                        color = Theme.colorScheme.brand.brand
                    )
                    Text(
                        text = stringResource(Res.string.order_items_count, order.itemsCount),
                        style = Theme.typography.body.small,
                        color = Color.Gray
                    )
                }
            }

            DashedDivider(color = Color(0xFFE0DDD7))

            PaymentSummary(order)
            if (order.totalCashback != "0") {
                DetailLine(
                    label = stringResource(Res.string.cashback),
                    value = listOf(
                        "${order.totalCashback} ${stringResource(Res.string.currency_sar)}",
                        order.cashbackStatus
                    ).filter { it.isNotBlank() }.joinToString(" - "),
                    valueColor = Theme.colorScheme.brand.brand
                )
            }

            when {
                order.isDriveThru -> {
                    DeliveryTimeline(order.deliveryStatus)
                    DriveThruDetails(order)
                }

                order.isDelivery -> {
                    DeliveryTimeline(order.deliveryStatus)
                    DeliveryDetails(order)
                }

                else -> PickupDetails(order)
            }

            if (order.notes.isNotBlank()) {
                DetailLine(
                    label = stringResource(Res.string.notes_label),
                    value = order.notes
                )
            }

            DashedDivider(color = Color(0xFFE0DDD7))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                order.items.forEachIndexed { index, item ->
                    OrderItemDetailedRow(
                        item = item,
                        isLastItem = index == order.items.lastIndex
                    )
                }
            }
        }
    }
}

private val OrderTypeUi.icon: org.jetbrains.compose.resources.DrawableResource
    get() = when (this) {
        OrderTypeUi.PICKUP -> Res.drawable.pick_up_from_branch
        OrderTypeUi.DRIVE_THRU -> Res.drawable.pick_up_from_car
        OrderTypeUi.DELIVERY -> Res.drawable.ic_location
    }

@Composable
private fun OrderHeader(order: OrderUiModel) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(Res.string.order_details),
                style = Theme.typography.body.large.copy(fontWeight = FontWeight.Bold),
                color = Theme.colorScheme.primary.primary
            )
            Text(
                text = order.formattedDate,
                style = Theme.typography.body.small,
                color = Color.Gray
            )
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = order.orderNumber,
                style = Theme.typography.body.medium.copy(fontWeight = FontWeight.SemiBold),
                color = Theme.colorScheme.primary.primary
            )
            Spacer(Modifier.height(6.dp))
            StatusBadge(
                status = stringResource(order.status.label),
                color = order.status.color
            )
        }
    }
}

@Composable
private fun PaymentSummary(order: OrderUiModel) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        MetaPill(
            label = stringResource(Res.string.payment_method),
            value = order.paymentMethod,
            color = Color(0xFF4C6FFF),
            icon = Res.drawable.onlinepayemt,
            modifier = Modifier.weight(1f)
        )
        MetaPill(
            label = stringResource(Res.string.payment_status),
            value = order.paymentStatusText,
            color = order.paymentStatus.color,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun PickupDetails(order: OrderUiModel) {
    DetailLine(
        label = stringResource(Res.string.branch_label),
        value = order.branchName,
        icon = Res.drawable.pick_up_from_branch
    )
}

@Composable
private fun DriveThruDetails(order: OrderUiModel) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        DetailLine(
            label = stringResource(Res.string.delivery_status),
            value = stringResource(order.deliveryStatus.label),
            valueColor = order.deliveryStatus.color
        )
        DetailLine(
            label = stringResource(Res.string.assigned_driver),
            value = order.assignedDeliveryDriver.ifBlank { stringResource(Res.string.status_not_assigned) }
        )
        DetailLine(
            label = stringResource(Res.string.order_address),
            value = order.orderAddress,
            icon = Res.drawable.ic_location
        )
        DetailLine(
            label = stringResource(Res.string.car_details),
            value = order.carSummary,
            icon = Res.drawable.car
        )
        DetailLine(
            label = stringResource(Res.string.branch_label),
            value = order.branchName,
            icon = Res.drawable.pick_up_from_branch
        )
    }
}

@Composable
private fun DeliveryDetails(order: OrderUiModel) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        DetailLine(
            label = stringResource(Res.string.delivery_status),
            value = stringResource(order.deliveryStatus.label),
            valueColor = order.deliveryStatus.color
        )
        DetailLine(
            label = stringResource(Res.string.assigned_driver),
            value = order.assignedDeliveryDriver.ifBlank { stringResource(Res.string.status_not_assigned) }
        )
        DetailLine(
            label = stringResource(Res.string.order_address),
            value = order.orderAddress,
            icon = Res.drawable.ic_location
        )
        if (order.deliveryFee != "0") {
            DetailLine(
                label = stringResource(Res.string.delivery_fee),
                value = "${order.deliveryFee} ${stringResource(Res.string.currency_sar)}",
                valueColor = Theme.colorScheme.brand.brand
            )
        }
    }
}

@Composable
private fun DeliveryTimeline(status: DeliveryStatusUi) {
    val steps = listOf(
        TimelineStep(Res.string.status_processing, Res.drawable.cart_icon),
        TimelineStep(Res.string.status_shipped, Res.drawable.pick_up_from_branch),
        TimelineStep(Res.string.status_in_transit, Res.drawable.car),
        TimelineStep(Res.string.status_delivered, Res.drawable.ic_check_circle)
    )
    val activeIndex = status.stepIndex.coerceIn(0, steps.lastIndex)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFFF8F6F1))
            .padding(horizontal = 8.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            steps.forEachIndexed { index, step ->
                TimelineNode(
                    step = step,
                    isActive = index == activeIndex,
                    isCompleted = index < activeIndex || status == DeliveryStatusUi.DELIVERED,
                    color = status.color,
                    showStartLine = index > 0,
                    showEndLine = index < steps.lastIndex,
                    modifier = Modifier.weight(1f)
                )
            }
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            steps.forEach { step ->
                Text(
                    text = stringResource(step.label),
                    style = Theme.typography.body.small.copy(
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = Color(0xFF4E4A45),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun TimelineNode(
    step: TimelineStep,
    isActive: Boolean,
    isCompleted: Boolean,
    color: Color,
    showStartLine: Boolean,
    showEndLine: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (showStartLine) {
            TimelineConnector(
                completed = isCompleted || isActive,
                color = color,
                modifier = Modifier.weight(1f)
            )
        } else {
            Spacer(Modifier.weight(1f))
        }

        TimelineCircle(
            icon = step.icon,
            isActive = isActive,
            isCompleted = isCompleted,
            color = color
        )

        if (showEndLine) {
            TimelineConnector(
                completed = isCompleted,
                color = color,
                modifier = Modifier.weight(1f)
            )
        } else {
            Spacer(Modifier.weight(1f))
        }
    }
}

@Composable
private fun TimelineCircle(
    icon: org.jetbrains.compose.resources.DrawableResource,
    isActive: Boolean,
    isCompleted: Boolean,
    color: Color
) {
    val infiniteTransition = rememberInfiniteTransition(label = "delivery_status_pulse")
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.86f,
        targetValue = 1.12f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 850),
            repeatMode = RepeatMode.Reverse
        ),
        label = "delivery_status_pulse_scale"
    )
    val fillColor = when {
        isActive || isCompleted -> color
        else -> Color(0xFF9E9E9E)
    }

    Box(
        modifier = Modifier.size(46.dp),
        contentAlignment = Alignment.Center
    ) {
        if (isActive) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .graphicsLayer {
                        scaleX = pulse
                        scaleY = pulse
                    }
                    .background(color.copy(alpha = 0.18f), CircleShape)
            )
        }
        Box(
            modifier = Modifier
                .size(34.dp)
                .background(fillColor, CircleShape)
                .border(1.dp, Color.White.copy(alpha = 0.85f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
private fun TimelineConnector(
    completed: Boolean,
    color: Color,
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
            .height(4.dp)
            .fillMaxWidth()
    ) {
        drawLine(
            color = if (completed) color else Color(0xFFB7B2AA),
            start = androidx.compose.ui.geometry.Offset(0f, size.height / 2f),
            end = androidx.compose.ui.geometry.Offset(size.width, size.height / 2f),
            strokeWidth = size.height,
            pathEffect = if (completed) null else PathEffect.dashPathEffect(floatArrayOf(8f, 8f))
        )
    }
}

@Composable
fun OrderItemDetailedRow(item: OrderItemUiModel, isLastItem: Boolean) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFF2EFE9)),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = item.imageUrl,
                    contentDescription = item.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(48.dp)
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Text(
                    text = item.name,
                    style = Theme.typography.body.medium.copy(fontWeight = FontWeight.SemiBold),
                    color = Theme.colorScheme.primary.primary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = listOf(item.quantityDetails, item.size)
                        .filter { it.isNotBlank() }
                        .joinToString(" - "),
                    style = Theme.typography.body.small,
                    color = Color.Gray
                )
                if (item.isRewardItem) {
                    StatusBadge(
                        status = stringResource(Res.string.reward_item),
                        color = Theme.colorScheme.brand.brand
                    )
                }
            }

            Text(
                text = "${item.totalRowPrice} ${stringResource(Res.string.currency_sar)}",
                style = Theme.typography.body.medium.copy(fontWeight = FontWeight.Bold),
                color = Theme.colorScheme.primary.primary
            )
        }

        item.extensions.forEach { extension ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp, start = 58.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = extension.name,
                    style = Theme.typography.body.small.copy(fontSize = 13.sp),
                    color = Color.Gray,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "${extension.price} ${stringResource(Res.string.currency_sar)}",
                    style = Theme.typography.body.small.copy(fontSize = 13.sp),
                    color = Color.Gray
                )
            }
        }

        if (item.cashbackAmount != "0") {
            Text(
                text = "${stringResource(Res.string.cashback)} ${item.cashbackAmount} ${stringResource(Res.string.currency_sar)}",
                style = Theme.typography.body.small.copy(fontSize = 12.sp),
                color = Theme.colorScheme.brand.brand,
                modifier = Modifier.padding(top = 4.dp, start = 58.dp)
            )
        }

        if (!isLastItem) {
            DashedDivider(
                width = 0.86f,
                modifier = Modifier.padding(top = 10.dp)
            )
        }
    }
}

@Composable
private fun MetaPill(
    label: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier,
    icon: org.jetbrains.compose.resources.DrawableResource? = null
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(color.copy(alpha = 0.09f))
            .padding(horizontal = 10.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        icon?.let {
            Icon(
                painter = painterResource(it),
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(18.dp)
            )
        }
        Column {
            Text(
                text = label,
                style = Theme.typography.body.small.copy(fontSize = 11.sp),
                color = Color.Gray
            )
            Text(
                text = value.ifBlank { "-" },
                style = Theme.typography.body.small.copy(fontWeight = FontWeight.SemiBold),
                color = color,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun DetailLine(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    valueColor: Color = Theme.colorScheme.primary.primary,
    icon: org.jetbrains.compose.resources.DrawableResource? = null
) {
    if (value.isBlank()) return

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        icon?.let {
            Icon(
                painter = painterResource(it),
                contentDescription = null,
                tint = Theme.colorScheme.brand.brand,
                modifier = Modifier.padding(top = 2.dp).size(16.dp)
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = Theme.typography.body.small.copy(fontSize = 11.sp),
                color = Color.Gray
            )
            Text(
                text = value,
                style = Theme.typography.body.small.copy(fontWeight = FontWeight.SemiBold),
                color = valueColor
            )
        }
    }
}

@Composable
fun DashedDivider(
    modifier: Modifier = Modifier,
    color: Color = Color.Gray,
    thickness: Float = 2f,
    dashLength: Float = 10f,
    gapLength: Float = 10f,
    width: Float = 1f
) {
    Canvas(
        modifier = modifier
            .fillMaxWidth(width)
            .height(1.dp)
    ) {
        drawLine(
            color = color,
            start = androidx.compose.ui.geometry.Offset(0f, 0f),
            end = androidx.compose.ui.geometry.Offset(size.width, 0f),
            strokeWidth = thickness,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(dashLength, gapLength), 0f)
        )
    }
}

@Composable
fun StatusBadge(status: String, color: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(color.copy(alpha = 0.1f))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = status,
            style = Theme.typography.body.small.copy(fontWeight = FontWeight.Medium),
            color = color
        )
    }
}

private data class TimelineStep(
    val label: org.jetbrains.compose.resources.StringResource,
    val icon: org.jetbrains.compose.resources.DrawableResource
)
