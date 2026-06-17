package com.khater.rwaq.data.dto.cart

import com.khater.rwaq.domain.entities.cart.*

fun CartResponse.toDomain(): Cart = data.toDomain(userPoints)

fun CartData.toDomain(userPoints: Int? = null): Cart = Cart(
    id = id,
    items = items.map { it.toDomain() },
    subtotal = subtotal,
    deliveryFee = deliveryFee,
    totalQuantity = totalQuantity,
    notes = notes,
    pickupType = pickupType,
    pickupTypeLabel = pickupTypeLabel,
    branchId = branchId,
    branchName = branchName,
    carName = carName,
    carNumber = carNumber,
    carColor = carColor,
    carColorName = carColorName,
    orderAddress = orderAddress,
    latitude = latitude ?: orderLocation?.latitude,
    longitude = longitude ?: orderLocation?.longitude,
    couponCode = couponCode,
    userPoints = userPoints
)

fun com.khater.rwaq.data.dto.cart.CartItem.toDomain(): com.khater.rwaq.domain.entities.cart.CartItem =
    com.khater.rwaq.domain.entities.cart.CartItem(
        id = id,
        productId = product.id,
        productName = productName,
        productNameAr = productNameAr,
        quantity = quantity,
        size = size,
        sizeAr = sizeAr,
        unitPrice = unitPrice,
        totalPrice = totalPrice,
        imageUrl = if (imageUrl.isNotEmpty()) imageUrl else product.imageUrl,
        extensions = extensions.map { it.toDomain() },
        isRewardItem = isRewardItem,
        pointsCost = pointsCost.toInt(),
        cashbackAmount = cashbackAmount
    )

fun com.khater.rwaq.data.dto.cart.CartItem.CartExtension.toDomain(): com.khater.rwaq.domain.entities.cart.CartExtension =
    com.khater.rwaq.domain.entities.cart.CartExtension(
        extensionId = extensionId,
        name = name,
        nameAr = nameAr,
        price = price,
        quantity = quantity
    )

fun CheckoutResponse.toDomain(): CheckoutResult = CheckoutResult(
    orderId = data.order?.id ?: data.sessionId ?: "",
    totalAmount = data.order?.totalAmount ?: 0.0,
    paymentStatus = data.order?.paymentStatus ?: "PENDING",
    paymentInfo = data.payment?.toDomain()
)

fun com.khater.rwaq.data.dto.cart.PaymentInfo.toDomain(): com.khater.rwaq.domain.entities.cart.PaymentInfo =
    com.khater.rwaq.domain.entities.cart.PaymentInfo(
        clientSecret = clientSecret,
        publicKey = publicKey,
        paymobOrderId = paymobOrderId,
        paymentUrl = paymentUrl
    )
