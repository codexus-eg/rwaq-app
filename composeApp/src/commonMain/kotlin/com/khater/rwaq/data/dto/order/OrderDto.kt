package com.khater.rwaq.data.dto.order

import com.khater.rwaq.data.dto.base.PagedDataDto
import com.khater.rwaq.domain.entities.order.NewOrder
import com.khater.rwaq.domain.entities.order.OrderGeoLocation
import com.khater.rwaq.domain.entities.order.OrderItem
import com.khater.rwaq.domain.entities.order.OrderItemExtension
import com.khater.rwaq.domain.model.PagedData
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.doubleOrNull

@Serializable
data class OrderDto(
    @SerialName("_id")
    val id: String, // Mapping _id to id
    @SerialName("items")
    val items: List<OrderItemDto>,
    @SerialName("notes")
    val notes: String? = null,
    @SerialName("couponCode")
    val couponCode: String? = null,
    @SerialName("paymentMethod")
    val paymentMethod: String = "",
    @SerialName("paymentStatus")
    val paymentStatus: String = "",
    @SerialName("paymobTransactionId")
    val paymobTransactionId: String? = null,
    @SerialName("paymobOrderId")
    val paymobOrderId: String? = null,
    @SerialName("totalAmount")
    val totalAmount: Double = 0.0, // JSON has Int, but Money usually Double
    @SerialName("deliveryFee")
    val deliveryFee: Double = 0.0,
    @SerialName("status")
    val status: String = "",
    @SerialName("pickupType")
    val pickupType: String? = null,
    @SerialName("pickupTypeLabel")
    val pickupTypeLabel: String? = null,
    @SerialName("orderLocation")
    val orderLocation: OrderLocationDto? = null,
    @SerialName("customerName")
    val customerName: String? = null,
    @SerialName("customerEmail")
    val customerEmail: String? = null,
    @SerialName("customerPhone")
    val customerPhone: String? = null,
    @SerialName("orderAddress")
    val orderAddress: String? = null,
    @SerialName("assignedDeliveryDriver")
    val assignedDeliveryDriver: JsonElement? = null,
    @SerialName("deliveryStatus")
    val deliveryStatus: String = "",
    @SerialName("deliveryAssignedAt")
    val deliveryAssignedAt: String? = null,
    @SerialName("deliveredAt")
    val deliveredAt: String? = null,
    @SerialName("totalCashback")
    val totalCashback: Double = 0.0,
    @SerialName("cashbackStatus")
    val cashbackStatus: String? = null,
    @SerialName("pointsDeducted")
    val pointsDeducted: Boolean = false,
    @SerialName("carName")
    val carName: String? = null,
    @SerialName("carNumber")
    val carNumber: String? = null,
    @SerialName("carColor")
    val carColor: String? = null,
    @SerialName("carColorName")
    val carColorName: String? = null,
    @SerialName("createdAt")
    val createdAt: String = "",
    @SerialName("updatedAt")
    val updatedAt: String = ""
){
 fun toDomain(): NewOrder {
     val orderItems = this.items.map { it.toDomain() }
     return NewOrder(
         id = this.id,
         items = orderItems,
         notes = this.notes,
         couponCode = this.couponCode,
         paymentMethod = this.paymentMethod,
         paymentStatus = this.paymentStatus,
         paymobTransactionId = this.paymobTransactionId,
         paymobOrderId = this.paymobOrderId,
         totalAmount = this.totalAmount,
         deliveryFee = this.deliveryFee,
	         status = this.status,
	         pickupType = this.pickupType,
	         pickupTypeLabel = this.pickupTypeLabel,
	         orderLocation = this.orderLocation?.toDomain(),
         customerName = this.customerName,
         customerEmail = this.customerEmail,
         customerPhone = this.customerPhone,
         orderAddress = this.orderAddress,
         assignedDeliveryDriver = this.assignedDeliveryDriver.toDriverLabel(),
         deliveryStatus = this.deliveryStatus,
         deliveryAssignedAt = this.deliveryAssignedAt,
         deliveredAt = this.deliveredAt,
         totalCashback = this.totalCashback,
         cashbackStatus = this.cashbackStatus,
         pointsDeducted = this.pointsDeducted,
         carName = this.carName.cleanOrNull() ?: orderItems.firstNotNullOfOrNull { it.carName },
         carNumber = this.carNumber.cleanOrNull() ?: orderItems.firstNotNullOfOrNull { it.carNumber },
         carColor = this.carColor.cleanOrNull() ?: orderItems.firstNotNullOfOrNull { it.carColor },
         carColorName = this.carColorName.cleanOrNull() ?: orderItems.firstNotNullOfOrNull { it.carColorName },
         createdAt = this.createdAt,
         updatedAt = this.updatedAt
     )
}
}

@Serializable
data class OrderLocationDto(
    @SerialName("latitude")
    val latitude: Double = 0.0,
    @SerialName("longitude")
    val longitude: Double = 0.0
)

@Serializable
data class OrderItemDto(
    @SerialName("productId")
    val productId: String? = null,
    @SerialName("productName")
    val productName: String = "",
    @SerialName("quantity")
    val quantity: Int = 0,
    @SerialName("size")
    val size: String = "",
    @SerialName("unitPrice")
    val unitPrice: Double = 0.0,
    @SerialName("totalPrice")
    val totalPrice: Double = 0.0,
    @SerialName("cashbackAmount")
    val cashbackAmount: Double = 0.0,
    @SerialName("pointsCost")
    val pointsCost: Int = 0,
    @SerialName("isRewardItem")
    val isRewardItem: Boolean = false,
    @SerialName("imageUrl")
    val imageUrl: String? = null,
    @SerialName("extensions")
    val extensions: List<OrderExtensionDto> = emptyList(),
    @SerialName("branchId")
    val branchId: String? = null,
    @SerialName("branchName")
    val branchName: String? = null,
    @SerialName("isPickupFromBranch")
    val isPickupFromBranch: Boolean = true,
    @SerialName("carName")
    val carName: String? = null,
    @SerialName("carNumber")
    val carNumber: String? = null,
    @SerialName("carColor")
    val carColor: String? = null,
    @SerialName("carColorName")
    val carColorName: String? = null
)

@Serializable
data class OrderExtensionDto(
    @SerialName("extensionId")
    val extensionId: String,
    @SerialName("name")
    val name: String,
    @SerialName("price")
    val price: Double,
    @SerialName("quantity")
    val quantity: Int
)




fun List<OrderDto>.toListOfOrders(): List<NewOrder> {
    return mapNotNull { it.toDomain() }
}

fun PagedDataDto<OrderDto>.toPagedListOfOrders(): PagedData<NewOrder> {
    return PagedData(
        data = data.toListOfOrders(),
        totalItems = totalItems,
        isLastPage = pageNumber >= totalPages
    )
}




fun OrderItemDto.toDomain(): OrderItem {
    return OrderItem(
        productName = this.productName,
        productId = this.productId,
        quantity = this.quantity,
        size = this.size,
        unitPrice = this.unitPrice,
        totalPrice = this.totalPrice,
        cashbackAmount = this.cashbackAmount,
        pointsCost = this.pointsCost,
        isRewardItem = this.isRewardItem,
        // Handle empty URLs if necessary
        imageUrl = this.imageUrl ?: "",

        // Map the list of extensions
        extensions = this.extensions.map { it.toDomain() },

        branchName = this.branchName ?: "",
        isPickupFromBranch = this.isPickupFromBranch,

        // Logic: If it is pickup from branch, car details are irrelevant (null).
        // If the JSON returns empty strings "", we convert them to null for the Domain
        carName = this.carName.cleanOrNull(),
        carNumber = this.carNumber.cleanOrNull(),
        carColor = this.carColor.cleanOrNull(),
        carColorName = this.carColorName.cleanOrNull()
    )
}

fun OrderExtensionDto.toDomain(): OrderItemExtension {
    return OrderItemExtension(
        name = this.name,
        price = this.price,
        quantity = this.quantity
    )
}

private fun OrderLocationDto.toDomain(): OrderGeoLocation =
    OrderGeoLocation(latitude = latitude, longitude = longitude)

private fun String?.cleanOrNull(): String? = this?.trim()?.takeIf { it.isNotEmpty() }

private fun JsonElement?.toDriverLabel(): String? {
    return when (this) {
        null -> null
        is JsonPrimitive -> contentOrNull.cleanOrNull()
        is JsonObject -> {
            val user = this["user"] as? JsonObject
            val name = stringValue("name")
                ?: stringValue("fullName")
                ?: stringValue("driverName")
                ?: stringValue("username")
                ?: user?.stringValue("name")
                ?: user?.stringValue("fullName")
            val phone = stringValue("phone")
                ?: stringValue("phoneNumber")
                ?: stringValue("mobile")
                ?: user?.stringValue("phone")
                ?: user?.stringValue("phoneNumber")
            listOfNotNull(name, phone)
                .distinct()
                .joinToString(" - ")
                .cleanOrNull()
        }

        else -> null
    }
}

private fun JsonObject.stringValue(key: String): String? {
    val primitive = this[key] as? JsonPrimitive ?: return null
    return primitive.contentOrNull.cleanOrNull()
        ?: primitive.doubleOrNull?.toString()
}
