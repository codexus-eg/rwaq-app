package com.khater.rwaq.data.dto.cart

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.*

object ProductBriefSerializer : KSerializer<ProductBriefDto> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("ProductBrief", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): ProductBriefDto {
        val jsonDecoder = decoder as? JsonDecoder ?: return ProductBriefDto(id = decoder.decodeString())
        val element = jsonDecoder.decodeJsonElement()
        return if (element is JsonObject) {
            val id = element["_id"]?.jsonPrimitive?.content ?: element["id"]?.jsonPrimitive?.content ?: ""
            val imageUrl = element["imageUrl"]?.jsonPrimitive?.content ?: ""
            ProductBriefDto(id = id, imageUrl = imageUrl)
        } else {
            ProductBriefDto(id = element.jsonPrimitive.content)
        }
    }

    override fun serialize(encoder: Encoder, value: ProductBriefDto) {
        encoder.encodeString(value.id)
    }
}

@Serializable
data class ProductBriefDto(
    val id: String,
    val imageUrl: String = ""
)

// --- Request Models ---

@Serializable
data class AddToCartRequestDto(
    @SerialName("productId")
    val productId: String,
    @SerialName("quantity")
    val quantity: Int,
    @SerialName("size")
    val size: String,
    @SerialName("sizeId")
    val sizeId: String,
    @SerialName("isRewardItem")
    val isRewardItem: Boolean = false,
    @SerialName("extensions")
    val extensions: List<CartExtensionDto> = emptyList()
) {
    @Serializable
    data class CartExtensionDto(
        @SerialName("extensionId")
        val extensionId: String,
        @SerialName("name")
        val name: String,
        @SerialName("nameAr")
        val nameAr: String = "",
        @SerialName("price")
        val price: Double,
        @SerialName("quantity")
        val quantity: Int
    )
}

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class UpdateCartItemRequestDto(
    @SerialName("quantity")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    val quantity: Int? = null,
    // When non-null the backend replaces the item's extensions with this list.
    // Omit an extension (or send quantity 0 and filter it out) to remove it.
    @SerialName("extensions")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    val extensions: List<AddToCartRequestDto.CartExtensionDto>? = null
)

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class UpdateCartMetadataRequestDto(
    @SerialName("notes")
    val notes: String = "",
    @SerialName("pickupType")
    val pickupType: String = "BRANCH",
    @SerialName("branchId")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    val branchId: String? = null,
    @SerialName("orderAddress")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    val orderAddress: String? = null,
    @SerialName("carName")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    val carName: String? = null,
    @SerialName("carNumber")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    val carNumber: String? = null,
    @SerialName("carColor")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    val carColor: String? = null,
    @SerialName("carColorName")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    val carColorName: String? = null,
    @SerialName("latitude")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    val latitude: Double? = null,
    @SerialName("longitude")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    val longitude: Double? = null
)

@Serializable
data class ApplyCouponRequestDto(
    @SerialName("couponCode")
    val couponCode: String
)

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class CheckoutRequestDto(
    @SerialName("paymentMethod")
    val paymentMethod: String = "ONLINE",
    @SerialName("onlinePaymentMethod")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    val onlinePaymentMethod: String? = null,
    @SerialName("pickupType")
    val pickupType: String = "BRANCH",
    @SerialName("branchId")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    val branchId: String? = null,
    @SerialName("orderAddress")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    val orderAddress: String? = null,
    @SerialName("carNumber")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    val carNumber: String? = null,
    @SerialName("carColor")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    val carColor: String? = null,
    @SerialName("carName")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    val carName: String? = null,
    @SerialName("carColorName")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    val carColorName: String? = null,
    @SerialName("notes")
    val notes: String = "",
    @SerialName("latitude")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    val latitude: Double? = null,
    @SerialName("longitude")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    val longitude: Double? = null
)

// --- Response Models ---

@Serializable
data class CartResponse(
    @SerialName("success")
    val success: Boolean,
    @SerialName("data")
    val data: CartData,
    @SerialName("userPoints")
    val userPoints: Int? = null,
    @SerialName("message")
    val message: String = ""
)

@Serializable
data class CartData(
    @SerialName("_id")
    val id: String,
    @SerialName("items")
    val items: List<CartItem> = emptyList(),
    @SerialName("notes")
    val notes: String = "",
    @SerialName("pickupType")
    val pickupType: String = "BRANCH",
    @SerialName("pickupTypeLabel")
    val pickupTypeLabel: String = "",
    @SerialName("branchId")
    val branchId: String = "",
    @SerialName("branchName")
    val branchName: String = "",
    @SerialName("orderAddress")
    val orderAddress: String = "",
    @SerialName("latitude")
    val latitude: Double? = null,
    @SerialName("longitude")
    val longitude: Double? = null,
    @SerialName("orderLocation")
    val orderLocation: CartLocationDto? = null,
    @SerialName("carName")
    val carName: String = "",
    @SerialName("carNumber")
    val carNumber: String = "",
    @SerialName("carColor")
    val carColor: String = "",
    @SerialName("carColorName")
    val carColorName: String = "",
    @SerialName("couponCode")
    val couponCode: String? = null,
    @SerialName("subtotal")
    val subtotal: Double = 0.0,
    @SerialName("deliveryFee")
    val deliveryFee: Double = 0.0,
    @SerialName("totalCashback")
    val totalCashback: Double = 0.0,
    @SerialName("totalQuantity")
    val totalQuantity: Int = 0
)

@Serializable
data class CartItem(
    @SerialName("_id")
    val id: String,
    @Serializable(with = ProductBriefSerializer::class)
    @SerialName("productId")
    val product: ProductBriefDto,
    @SerialName("productName")
    val productName: String,
    @SerialName("productNameAr")
    val productNameAr: String = "",
    @SerialName("quantity")
    val quantity: Int,
    @SerialName("size")
    val size: String,
    @SerialName("sizeAr")
    val sizeAr: String = "",
    @SerialName("unitPrice")
    val unitPrice: Double,
    @SerialName("totalPrice")
    val totalPrice: Double,
    @SerialName("imageUrl")
    val imageUrl: String = "",
    @SerialName("extensions")
    val extensions: List<CartExtension> = emptyList(),
    @SerialName("isRewardItem")
    val isRewardItem: Boolean = false,
    @SerialName("pointsCost")
    val pointsCost: Double = 0.0,
    @SerialName("cashbackAmount")
    val cashbackAmount: Double = 0.0,
    @SerialName("createdAt")
    val createdAt: String = ""
) {
    @Serializable
    data class CartExtension(
        @SerialName("extensionId")
        val extensionId: String,
        @SerialName("name")
        val name: String,
        @SerialName("nameAr")
        val nameAr: String = "",
        @SerialName("price")
        val price: Double,
        @SerialName("quantity")
        val quantity: Int
    )
}

@Serializable
data class CartLocationDto(
    @SerialName("latitude")
    val latitude: Double? = null,
    @SerialName("longitude")
    val longitude: Double? = null,
)

@Serializable
data class CheckoutResponse(
    @SerialName("success")
    val success: Boolean,
    @SerialName("data")
    val data: CheckoutData
)

@Serializable
data class CheckoutData(
    @SerialName("sessionId")
    val sessionId: String? = null,
    @SerialName("order")
    val order: OrderSummary? = null,
    @SerialName("payment")
    val payment: PaymentInfo? = null
)

@Serializable
data class OrderSummary(
    @SerialName("id")
    val id: String,
    @SerialName("totalAmount")
    val totalAmount: Double,
    @SerialName("paymentStatus")
    val paymentStatus: String
)

@Serializable
data class PaymentInfo(
    @SerialName("clientSecret")
    val clientSecret: String,
    @SerialName("publicKey")
    val publicKey: String,
    @SerialName("paymobOrderId")
    val paymobOrderId: String,
    @SerialName("paymentUrl")
    val paymentUrl: String = ""
)
