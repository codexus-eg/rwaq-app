package com.khater.rwaq.data.dto.product

import com.khater.rwaq.data.dto.base.PagedDataDto
import com.khater.rwaq.domain.entities.product.Product
import com.khater.rwaq.domain.entities.product.ProductCashback
import com.khater.rwaq.domain.entities.product.ProductExtension
import com.khater.rwaq.domain.entities.product.ProductSize
import com.khater.rwaq.domain.model.PagedData
import com.khater.rwaq.presentation.screens.productScreen.uiState.ProductDetailsUiState
import com.khater.rwaq.presentation.screens.productScreen.uiState.ProductExtensionUiModel
import com.khater.rwaq.presentation.screens.productScreen.uiState.ProductSizeUiModel
import com.khater.rwaq.presentation.screens.productScreen.uiState.ProductUiModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductDto(
    @SerialName("_id")
    val id: String? = null,
    val name: String? = null,
    val price: Double? = null,
    val imageUrl: String? = null,
    val category: String? = null,
    val description: String? = null,
    val extensions: List<ExtensionDto>? = null,
    val sizes: List<SizeDto>? = null,
    val isInStock: Boolean? = null,
    val discount: Double? = null,
    val isCoffee: Boolean? = null,
    val isSpecialOffer: Boolean? = null,
    val isMachine: Boolean? = null,
    @SerialName("cashback")
    val productCashback: ProductCashbackDto? = null,
)

@Serializable
data class ProductCashbackDto(
    val enabled: Boolean? = null,
    val type: String? = null,
    val amount: Double? = null,
)


@Serializable
data class ExtensionDto(
    @SerialName("_id") val id: String,
    val name: String,
    val price: Double,
    val maxCount: Int? = null,
)

@Serializable
data class SizeDto(
    @SerialName("_id") val id: String,
    val name: String,
    val price: Double,
)


fun ProductDto.toEntity() = Product(
    id = id ?: "",
    name = name ?: "",
    basePrice = price ?: 0.0,
    imageUrl = imageUrl ?: "",
    category = category ?: "",
    description = description ?: "",
    extensions = extensions?.map { ProductExtension(it.id, it.name, it.price) } ?: emptyList(),
    sizes = sizes?.map { ProductSize(it.id, it.name, it.price) } ?: emptyList(),
    isInStock = isInStock == true,
    discount = discount ?: 0.0,
    isCoffee = isCoffee == true,
    isSpecialOffer = isSpecialOffer == true,
    isMachine = isMachine == true,
    productCashback =  ProductCashback(
        enabled = productCashback?.enabled == true,
        amount = productCashback?.amount ?: 0.0,
        type = productCashback?.type ?: ""
    )
)


// Entity -> List UI Model
fun Product.toUiModel() = ProductUiModel(
    id = id,
    name = name,
    imageUrl = imageUrl,
    price = basePrice,
    category = category,
    discount = discount.toString()
)

// Entity -> Details UI State (Initial State when opening sheet)
fun Product.toDetailsUiState(): ProductDetailsUiState {
    // Default: Select first size if available
    val initialSizeId = sizes.firstOrNull()?.id
    val rawPrice = sizes.firstOrNull()?.price ?: basePrice
    // Calculate initial price
    val finalPrice = (rawPrice - discount).coerceAtLeast(0.0)

    return ProductDetailsUiState(
        id = id,
        name = name,
        description = description,
        imageUrl = imageUrl,
        sizes = sizes.map { ProductSizeUiModel(it.id, it.name, it.price, it.id == initialSizeId) },
        extensions = extensions.map {
            ProductExtensionUiModel(
                id = it.id,
                name = it.name,
                price = it.price,
                maxCount = 10, // Default to 10 if null, or use it.maxCount from entity
                currentQty = 0
            )
        },
        selectedSizeId = initialSizeId,
        selectedExtensionIds = emptySet(),
        productQuantity = 1,
        calculatedSingleUnitTestPrice = finalPrice,
        calculatedTotalPrice = finalPrice, // quantity 1,
        hasCashback = productCashback.enabled,
        cashBackAmount = productCashback.amount
    )
}

fun List<ProductDto>.toListOfProducts(): List<Product> {
    return mapNotNull { it.toEntity() }
}


fun PagedDataDto<ProductDto>.toPagedListOfProducts(): PagedData<Product> {
    return PagedData(
        data = data.toListOfProducts(),
        totalItems = totalItems,
        isLastPage = pageNumber >= totalPages,
        userPoints = userPoints
    )
}
