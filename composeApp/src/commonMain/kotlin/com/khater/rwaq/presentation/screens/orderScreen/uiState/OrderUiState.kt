package com.khater.rwaq.presentation.screens.orderScreen.uiState

import androidx.compose.ui.graphics.Color
import com.khater.rwaq.domain.entities.order.NewOrder
import com.khater.rwaq.domain.entities.order.OrderItem
import com.khater.rwaq.presentation.model.SnackBarState
import org.jetbrains.compose.resources.StringResource
import rwaq.composeapp.generated.resources.Res
import rwaq.composeapp.generated.resources.status_cancelled
import rwaq.composeapp.generated.resources.status_completed
import rwaq.composeapp.generated.resources.status_pending
import rwaq.composeapp.generated.resources.status_processing
import rwaq.composeapp.generated.resources.status_unknown

data class OrderUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val snackBar: SnackBarState = SnackBarState(),
    val orders: List<OrderUiModel> = emptyList()
)

fun NewOrder.toUiModel(): OrderUiModel {
    return OrderUiModel(
        id = this.id,
        orderNumber = "#${this.id.takeLast(5).uppercase()}",
        formattedDate = this.createdAt.toReadableDate(),
        totalPrice = "${this.totalAmount}",
        status = OrderStatusUi.fromString(this.status),
        paymentMethod = this.paymentMethod.replace("_", " ").lowercase().capitalizeWords(),
        itemsCount = this.items.sumOf { it.quantity },
        itemsSummary = this.items.joinToString(", ") { it.productName },
        items = this.items.map { it.toUiModel() }
    )
}

fun OrderItem.toUiModel(): OrderItemUiModel {
    // 1. Map Extensions to a UI model containing both Name and Price
    // Assuming your Extension entity has a 'price' field. If not, remove the price part.
    val extensionUiModels = this.extensions.map {
        ExtensionUiModel(
            name = "+ ${it.name} (${it.quantity})",
            // Assuming 'it.price' exists. If it's a total for that extension, use it directly.
            // If strictly not available, you might need to adjust this line.
            price = "${it.price * it.quantity}"
        )
    }

    return OrderItemUiModel(
        name = this.productName,
        // 2. Removed size (e.g. "Standard") and kept only quantity (e.g. "1x")
        quantityDetails = "${this.quantity}x",

        // 3. Breakdown of prices
        // Assuming 'this.price' is the unit price of the product
        productUnitPrice = "${this.unitPrice * this.quantity}",

        // The final total for this row (Product + Extensions) * Quantity
        totalRowPrice = "${this.totalPrice.toInt()}",

        imageUrl = this.imageUrl,
        extensions = extensionUiModels
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

// --- Updated Data Classes ---

data class OrderUiModel(
    val id: String = "",
    val orderNumber: String = "",
    val formattedDate: String = "",
    val totalPrice: String = "",
    val status: OrderStatusUi = OrderStatusUi.PENDING,
    val paymentMethod: String = "",
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
    val extensions: List<ExtensionUiModel> = emptyList() // List of extensions with prices
)

data class ExtensionUiModel(
    val name: String,
    val price: String
)

enum class OrderStatusUi(val label: StringResource, val color: Color) {
    PENDING(Res.string.status_pending, Color(0xFFFF9800)),
    COMPLETED(Res.string.status_completed, Color(0xFF4CAF50)),
    CANCELLED(Res.string.status_cancelled, Color(0xFFF44336)),
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