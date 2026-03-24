package com.khater.rwaq.presentation.screens.orderScreen.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.khater.rwaq.designSystem.theme.theme.Theme
import com.khater.rwaq.presentation.screens.cartScreen.components.SectionCard
import com.khater.rwaq.presentation.screens.orderScreen.uiState.OrderItemUiModel
import com.khater.rwaq.presentation.screens.orderScreen.uiState.OrderUiModel
import kotlinx.datetime.Month
import org.jetbrains.compose.resources.stringResource
import rwaq.composeapp.generated.resources.Res
import rwaq.composeapp.generated.resources.currency_sar
import rwaq.composeapp.generated.resources.item_total
import rwaq.composeapp.generated.resources.order_details
import rwaq.composeapp.generated.resources.order_items_count

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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // --- Header (Name, Date, ID, Status) ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(Res.string.order_details), // Or User Name if available
                    style = Theme.typography.body.large.copy(fontWeight = FontWeight.Bold),
                    color = Theme.colorScheme.primary.primary
                )
                Text(
                    text = order.orderNumber,
                    style = Theme.typography.body.large.copy(fontWeight = FontWeight.SemiBold),
                    color = Color.Gray
                )


            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = order.formattedDate,
                    style = Theme.typography.body.small,
                    color = Color.Gray
                )
                StatusBadge(
                    status = stringResource(order.status.label),
                    color = order.status.color
                )
            }
            DashedDivider(color = Color.LightGray)

            // --- Order Summary Header (Green Total) ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(Res.string.order_items_count, order.itemsCount),
                    style = Theme.typography.body.medium.copy(fontWeight = FontWeight.Bold),
                    color = Theme.colorScheme.primary.primary
                )

                Text(
                    text = "${order.totalPrice} ${stringResource(Res.string.currency_sar)}",
                    style = Theme.typography.body.large.copy(fontWeight = FontWeight.Bold),
                    color = Color(0xFF4CAF50) // Green color like image
                )
            }

            // --- Items List ---
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp) // Spacing between different items
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

@Composable
fun OrderItemDetailedRow(item: OrderItemUiModel, isLastItem: Boolean) {
    Column(modifier = Modifier.fillMaxWidth()) {

        // 1. Product Name & Product Price
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "${item.name} (${item.quantityDetails})",
                style = Theme.typography.body.medium,
                color = Theme.colorScheme.primary.primary,
                modifier = Modifier.weight(1f)
            )

            Text(
                text = "${item.productUnitPrice} ${stringResource(Res.string.currency_sar)}",
                style = Theme.typography.body.medium,
                color = Theme.colorScheme.primary.primary
            )
        }

        // 2. Extensions List (Indented)
        item.extensions.forEach { extension ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = extension.name,
                    style = Theme.typography.body.small.copy(fontSize = 14.sp),
                    color = Color.Gray,
                    modifier = Modifier.padding(start = 16.dp).weight(1f) // Indent extensions
                )

                Text(
                    text = "${extension.price} ${stringResource(Res.string.currency_sar)}",
                    style = Theme.typography.body.small.copy(fontSize = 14.sp),
                    color = Color.Gray
                )
            }
        }
        if (!isLastItem)
            DashedDivider(width = 0.45f, modifier = Modifier.padding(top = 4.dp))
//        // 3. Item Total (If you want to display the specific total for this item row)
//        // If the product has extensions, it's helpful to see the sum.
//        if (item.extensions.isNotEmpty()) {
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(top = 4.dp),
//                horizontalArrangement = Arrangement.End // Align to right
//            ) {
//                Text(
//                    text = stringResource(Res.string.item_total,item.totalRowPrice),
//                    style = Theme.typography.body.small.copy(fontWeight = FontWeight.SemiBold),
//                    color = Theme.colorScheme.primary.primary
//                )
//            }
//        }
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
            start = Offset(0f, 0f),
            end = Offset(size.width, 0f),
            strokeWidth = thickness,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(dashLength, gapLength), 0f)
        )
    }
}

@Composable
fun StatusBadge(status: String, color: Color) {
    androidx.compose.foundation.layout.Box(
        modifier = Modifier.background
            (color.copy(alpha = 0.1f), androidx.compose.foundation.shape.RoundedCornerShape(4.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = status,
            style = Theme.typography.body.small.copy(fontWeight = FontWeight.Medium),
            color = color
        )
    }
}