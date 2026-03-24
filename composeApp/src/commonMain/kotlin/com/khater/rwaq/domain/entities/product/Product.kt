package com.khater.rwaq.domain.entities.product

data class Product(
    val id: String,
    val name: String,
    val basePrice: Double,
    val imageUrl: String,
    val category: String,
    val description: String,
    val extensions: List<ProductExtension>,
    val sizes: List<ProductSize>,
    val isInStock: Boolean,
    val discount: Double,
    val isCoffee: Boolean,
    val isSpecialOffer: Boolean,
    val isMachine: Boolean,
    val productCashback: ProductCashback,
)

data class ProductExtension(
    val id: String,
    val name: String,
    val price: Double,
)

data class ProductCashback(
    val enabled: Boolean,
    val type: String,
    val amount: Double,
)

data class ProductSize(
    val id: String,
    val name: String,
    val price: Double,
)

data class Category(
    val id: String,
    val name: String, // Assume backend sends the correct language or handle localization here
)