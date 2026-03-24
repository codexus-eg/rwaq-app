package com.khater.rwaq.data.dto.order

import com.khater.rwaq.data.dto.base.PagedDataDto
import com.khater.rwaq.domain.entities.order.NewOrder
import com.khater.rwaq.domain.entities.order.OrderItem
import com.khater.rwaq.domain.entities.order.OrderItemExtension
import com.khater.rwaq.domain.model.PagedData
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
    val paymentMethod: String,
    @SerialName("totalAmount")
    val totalAmount: Double, // JSON has Int, but Money usually Double
    @SerialName("status")
    val status: String,
    @SerialName("createdAt")
    val createdAt: String,
    @SerialName("updatedAt")
    val updatedAt: String
){
 fun toDomain(): NewOrder {
     return NewOrder(
         id = this.id,
         items = this.items.map { it.toDomain() },
         notes = this.notes,
         couponCode = this.couponCode,
         paymentMethod = this.paymentMethod,
         totalAmount = this.totalAmount,
         status = this.status,
         createdAt = this.createdAt,
         updatedAt = this.updatedAt
     )
}
}

@Serializable
data class OrderItemDto(
    @SerialName("productName")
    val productName: String,
    @SerialName("quantity")
    val quantity: Int,
    @SerialName("size")
    val size: String,
    @SerialName("unitPrice")
    val unitPrice: Double,
    @SerialName("totalPrice")
    val totalPrice: Double,
    @SerialName("imageUrl")
    val imageUrl: String? = null,
    @SerialName("extensions")
    val extensions: List<OrderExtensionDto> = emptyList(),
    @SerialName("branchId")
    val branchId: String? = null,
    @SerialName("branchName")
    val branchName: String? = null,
    @SerialName("isPickupFromBranch")
    val isPickupFromBranch: Boolean,
    @SerialName("carName")
    val carName: String? = null,
    @SerialName("carNumber")
    val carNumber: String? = null,
    @SerialName("carColor")
    val carColor: Int? = null
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
        quantity = this.quantity,
        size = this.size,
        unitPrice = this.unitPrice,
        totalPrice = this.totalPrice,
        // Handle empty URLs if necessary
        imageUrl = this.imageUrl ?: "",

        // Map the list of extensions
        extensions = this.extensions.map { it.toDomain() },

        branchName = this.branchName ?: "",
        isPickupFromBranch = this.isPickupFromBranch,

        // Logic: If it is pickup from branch, car details are irrelevant (null).
        // If the JSON returns empty strings "", we convert them to null for the Domain
        carName = if (this.carName.isNullOrBlank()) null else this.carName,
        carNumber = if (this.carNumber.isNullOrBlank()) null else this.carNumber,
        carColor = if (this.carColor == 0 || this.carColor == null) null else this.carColor
    )
}

fun OrderExtensionDto.toDomain(): OrderItemExtension {
    return OrderItemExtension(
        name = this.name,
        price = this.price,
        quantity = this.quantity
    )
}
