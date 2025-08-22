package com.example.cartapp.domain.model

data class Favorite(
    val productId: String,
    val name: String,
    val price: String,
    val image: String,
    val description: String,
    val model: String,
    val brand: String,
    val createdAt: String
) {
    val priceAsDouble: Double
        get() = price.toDoubleOrNull() ?: 0.0
}