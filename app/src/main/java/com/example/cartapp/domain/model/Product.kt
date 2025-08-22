package com.example.cartapp.domain.model

data class Product(
    val id: String,
    val name: String,
    val image: String,
    val price: String,
    val description: String,
    val model: String,
    val brand: String,
    val createdAt: String
) {
    val priceAsDouble: Double
        get() = price.toDoubleOrNull() ?: 0.0
    
    val title: String
        get() = name
}
