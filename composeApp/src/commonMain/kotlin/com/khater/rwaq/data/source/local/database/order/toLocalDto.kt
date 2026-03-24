package com.khater.rwaq.data.source.local.database.order

import com.khater.rwaq.domain.entities.order.Order

fun Order.toLocalDto(): OrderLocalDto {
    return OrderLocalDto(
        id = id,
        name = name,
        totalPrice = totalPrice,
        itemPrice = itemPrice,
        count = count,
        size = size,
        extensions = extension,
        // Map new fields
        branchId = branchId,
        branchName = branchName,
        isPickupFromBranch = isPickupFromBranch,
        carName = carName,
        carNumber = carNumber,
        carColor = carColor,
        imageUrl = imageUrl,
        productId = productId,
        isReward = isReward
    )
}

fun OrderLocalDto.toDomain(): Order {
    return Order(
        id = id,
        name = name,
        totalPrice = totalPrice,
        itemPrice = itemPrice,
        count = count,
        size = size,
        extension = extensions,
        // Map new fields
        branchId = branchId,
        branchName = branchName,
        isPickupFromBranch = isPickupFromBranch,
        carName = carName,
        carNumber = carNumber,
        carColor = carColor,
        imageUrl = imageUrl,
        productId = productId,
        isReward = isReward
    )
}

fun List<OrderLocalDto>.toDomain(): List<Order> = map { it.toDomain() }