package com.khater.rwaq.presentation.screens.orderScreen.uiState

import androidx.compose.ui.graphics.Color
import com.khater.rwaq.domain.entities.order.NewOrder
import com.khater.rwaq.domain.entities.order.OrderItem
import com.khater.rwaq.domain.model.PickupType
import com.khater.rwaq.presentation.model.SnackBarState
import org.jetbrains.compose.resources.StringResource
import rwaq.composeapp.generated.resources.Res
import rwaq.composeapp.generated.resources.drive_thru
import rwaq.composeapp.generated.resources.delivery
import rwaq.composeapp.generated.resources.pickup_from_branch
import rwaq.composeapp.generated.resources.status_assigned
import rwaq.composeapp.generated.resources.status_cancelled
import rwaq.composeapp.generated.resources.status_completed
import rwaq.composeapp.generated.resources.status_confirmed
import rwaq.composeapp.generated.resources.status_delivered
import rwaq.composeapp.generated.resources.status_in_transit
import rwaq.composeapp.generated.resources.status_not_assigned
import rwaq.composeapp.generated.resources.status_pending
import rwaq.composeapp.generated.resources.status_processing
import rwaq.composeapp.generated.resources.status_rejected
import rwaq.composeapp.generated.resources.status_shipped
import rwaq.composeapp.generated.resources.status_unknown

data class OrderUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val snackBar: SnackBarState = SnackBarState(),
    val orders: List<OrderUiModel> = emptyList()
)

fun NewOrder.toUiModel(): OrderUiModel {
    val orderType = resolveOrderType()
    val isDriveThru = orderType == OrderTypeUi.DRIVE_THRU
    val isDelivery = orderType == OrderTypeUi.DELIVERY
    val branchName = items.firstOrNull { it.branchName.isNotBlank() }?.branchName.orEmpty()
    val carSummary = listOfNotNull(
        carName,
        carColorName ?: carColor,
        carNumber
    ).joinToString(" - ")

    return OrderUiModel(
        id = this.id,
        orderNumber = "#${this.id.takeLast(5).uppercase()}",
        formattedDate = this.createdAt.toReadableDate(),
        totalPrice = this.totalAmount.toMoneyText(),
        status = OrderStatusUi.fromString(this.status),
        paymentMethod = this.paymentMethod.replace("_", " ").lowercase().capitalizeWords(),
        paymentStatus = PaymentStatusUi.fromString(this.paymentStatus),
        paymentStatusText = this.paymentStatus.statusLabel(),
        orderType = orderType,
        pickupTypeLabel = this.pickupTypeLabel.orEmpty(),
        isDriveThru = isDriveThru,
        isDelivery = isDelivery,
        branchName = branchName,
        orderAddress = this.orderAddress.orEmpty(),
        customerName = this.customerName.orEmpty(),
        customerPhone = this.customerPhone.orEmpty(),
        carName = this.carName.orEmpty(),
        carNumber = this.carNumber.orEmpty(),
        carColor = (this.carColorName ?: this.carColor).orEmpty(),
        carSummary = carSummary,
        assignedDeliveryDriver = this.assignedDeliveryDriver.orEmpty(),
        deliveryStatus = DeliveryStatusUi.fromString(
            deliveryStatus = this.deliveryStatus,
            fallbackOrderStatus = this.status
        ),
        totalCashback = this.totalCashback.toMoneyText(),
        cashbackStatus = this.cashbackStatus?.statusLabel().orEmpty(),
        notes = this.notes.orEmpty(),
        itemsCount = this.items.sumOf { it.quantity },
        itemsSummary = this.items.joinToString(", ") { it.productName },
        items = this.items.map { it.toUiModel() }
    )
}

fun OrderItem.toUiModel(): OrderItemUiModel {
    val extensionUiModels = this.extensions.map {
        ExtensionUiModel(
            name = "+ ${it.name} (${it.quantity})",
            price = (it.price * it.quantity).toMoneyText()
        )
    }

    return OrderItemUiModel(
        name = this.productName,
        quantityDetails = "${this.quantity}x",
        productUnitPrice = (this.unitPrice * this.quantity).toMoneyText(),
        totalRowPrice = this.totalPrice.toMoneyText(),

        imageUrl = this.imageUrl,
        extensions = extensionUiModels,
        size = this.size,
        cashbackAmount = this.cashbackAmount.toMoneyText(),
        pointsCost = this.pointsCost,
        isRewardItem = this.isRewardItem
    )
}

// --- Helper Extensions (Kept same as before) ---
private fun String.toReadableDate(): String {
    return try {
        val datePart = this.substringBefore("T")
        val timePart = this.substringAfter("T").substringBefore(".")
        "$datePart $timePart"
    } catch (e: Exception) {
        this
    }
}

private fun String.capitalizeWords(): String = split(" ").joinToString(" ") { word ->
    word.lowercase().replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
}

private fun String.statusLabel(): String =
    replace("_", " ").lowercase().capitalizeWords().ifBlank { "-" }

private fun Double.toMoneyText(): String {
    val rounded = kotlin.math.round(this * 100) / 100.0
    return if (rounded % 1.0 == 0.0) {
        rounded.toInt().toString()
    } else {
        rounded.toString()
    }
}

private fun NewOrder.resolveOrderType(): OrderTypeUi {
    val normalizedPickupType = pickupType.orEmpty().uppercase()
    when (PickupType.from(normalizedPickupType)) {
        PickupType.BRANCH -> if (normalizedPickupType == PickupType.BRANCH.name) return OrderTypeUi.PICKUP
        PickupType.DRIVE_THRU -> return OrderTypeUi.DRIVE_THRU
        PickupType.DELIVERY -> return OrderTypeUi.DELIVERY
    }

    val hasCarDetails = listOf(carName, carNumber, carColor, carColorName)
        .any { !it.isNullOrBlank() }
    val itemHasCarDetails = items.any { item ->
        !item.carName.isNullOrBlank() ||
            !item.carNumber.isNullOrBlank() ||
            !item.carColor.isNullOrBlank() ||
            !item.carColorName.isNullOrBlank()
    }
    val normalizedDeliveryStatus = deliveryStatus.uppercase()

    return when {
        normalizedPickupType == "CAR_PICKUP" ||
            items.any { !it.isPickupFromBranch } ||
            hasCarDetails ||
            itemHasCarDetails -> OrderTypeUi.DRIVE_THRU

        !orderAddress.isNullOrBlank() ||
            !assignedDeliveryDriver.isNullOrBlank() ||
            (normalizedDeliveryStatus.isNotBlank() && normalizedDeliveryStatus != "NOT_ASSIGNED") -> OrderTypeUi.DELIVERY

        else -> OrderTypeUi.PICKUP
    }
}

// --- Updated Data Classes ---

data class OrderUiModel(
    val id: String = "",
    val orderNumber: String = "",
    val formattedDate: String = "",
    val totalPrice: String = "",
    val status: OrderStatusUi = OrderStatusUi.PENDING,
    val paymentMethod: String = "",
    val paymentStatus: PaymentStatusUi = PaymentStatusUi.UNKNOWN,
    val paymentStatusText: String = "",
    val orderType: OrderTypeUi = OrderTypeUi.PICKUP,
    val pickupTypeLabel: String = "",
    val isDriveThru: Boolean = false,
    val isDelivery: Boolean = false,
    val branchName: String = "",
    val orderAddress: String = "",
    val customerName: String = "",
    val customerPhone: String = "",
    val carName: String = "",
    val carNumber: String = "",
    val carColor: String = "",
    val carSummary: String = "",
    val assignedDeliveryDriver: String = "",
    val deliveryStatus: DeliveryStatusUi = DeliveryStatusUi.NOT_ASSIGNED,
    val totalCashback: String = "",
    val cashbackStatus: String = "",
    val notes: String = "",
    val itemsCount: Int = 0,
    val itemsSummary: String = "",
    val items: List<OrderItemUiModel> = emptyList(),
)

data class OrderItemUiModel(
    val name: String = "",
    val quantityDetails: String = "", // e.g., "1x"
    val productUnitPrice: String = "", // Price of the item itself (without extensions)
    val totalRowPrice: String = "",    // Total price (Item + Extensions)
    val imageUrl: String = "",
    val size: String = "",
    val cashbackAmount: String = "",
    val pointsCost: Int = 0,
    val isRewardItem: Boolean = false,
    val extensions: List<ExtensionUiModel> = emptyList() // List of extensions with prices
)

data class ExtensionUiModel(
    val name: String,
    val price: String
)

enum class OrderTypeUi(val label: StringResource, val color: Color) {
    PICKUP(Res.string.pickup_from_branch, Color(0xFF3B7D5C)),
    DRIVE_THRU(Res.string.drive_thru, Color(0xFF2E8FBB)),
    DELIVERY(Res.string.delivery, Color(0xFF8A5CF6))
}

enum class PaymentStatusUi(val color: Color) {
    PAID(Color(0xFF2EAD4A)),
    PENDING(Color(0xFFFF9800)),
    FAILED(Color(0xFFE53935)),
    CANCELLED(Color(0xFFE53935)),
    REFUNDED(Color(0xFF3F7DD5)),
    UNKNOWN(Color.Gray);

    companion object {
        fun fromString(status: String): PaymentStatusUi {
            return when (status.uppercase()) {
                "PAID", "SUCCESS", "SUCCESSFUL" -> PAID
                "PENDING", "UNPAID", "" -> PENDING
                "FAILED", "DECLINED" -> FAILED
                "CANCELLED", "CANCELED" -> CANCELLED
                "REFUNDED" -> REFUNDED
                else -> UNKNOWN
            }
        }
    }
}

enum class DeliveryStatusUi(
    val label: StringResource,
    val color: Color,
    val stepIndex: Int
) {
    NOT_ASSIGNED(Res.string.status_not_assigned, Color(0xFFFF9800), 0),
    PROCESSING(Res.string.status_processing, Color(0xFFFF9800), 0),
    ASSIGNED(Res.string.status_assigned, Color(0xFF43B02A), 1),
    SHIPPED(Res.string.status_shipped, Color(0xFF43B02A), 1),
    IN_TRANSIT(Res.string.status_in_transit, Color(0xFF43B02A), 2),
    DELIVERED(Res.string.status_delivered, Color(0xFF43B02A), 3),
    CANCELLED(Res.string.status_cancelled, Color(0xFFE53935), 0),
    UNKNOWN(Res.string.status_unknown, Color.Gray, 0);

    companion object {
        fun fromString(deliveryStatus: String, fallbackOrderStatus: String): DeliveryStatusUi {
            val normalized = deliveryStatus.ifBlank { fallbackOrderStatus }.uppercase()
            return when (normalized) {
                "NOT_ASSIGNED" -> NOT_ASSIGNED
                "PENDING", "CONFIRMED", "PROCESSING" -> PROCESSING
                "ASSIGNED", "DRIVER_ASSIGNED" -> ASSIGNED
                "SHIPPED", "PICKED_UP", "READY", "READY_FOR_PICKUP" -> SHIPPED
                "IN_TRANSIT", "OUT_FOR_DELIVERY", "ON_THE_WAY" -> IN_TRANSIT
                "DELIVERED", "COMPLETED" -> DELIVERED
                "CANCELLED", "CANCELED", "REJECTED" -> CANCELLED
                else -> UNKNOWN
            }
        }
    }
}

enum class OrderStatusUi(val label: StringResource, val color: Color) {
    PENDING(Res.string.status_pending, Color(0xFFFF9800)),
    COMPLETED(Res.string.status_completed, Color(0xFF4CAF50)),
    CONFIRMED(Res.string.status_confirmed, Color(0xFF4CAFA0)),
    CANCELLED(Res.string.status_cancelled, Color(0xFFF44336)),
    REJECTED(Res.string.status_rejected, Color(0xFFAA0900)),
    PROCESSING(Res.string.status_processing, Color(0xFFFFC107)),
    UNKNOWN(Res.string.status_unknown, Color.Gray);

    companion object {
        fun fromString(status: String): OrderStatusUi {
            return try {
                entries.firstOrNull { it.name.equals(status, ignoreCase = true) } ?: UNKNOWN
            } catch (e: Exception) {
                UNKNOWN
            }
        }
    }
}
