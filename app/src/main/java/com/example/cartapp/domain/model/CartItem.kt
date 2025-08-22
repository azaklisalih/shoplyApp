package com.example.cartapp.domain.model

data class CartItem(
    val productId: String,
    val name: String,
    val price: String,
    val image: String,
    val description: String,
    val model: String,
    val brand: String,
    val quantity: Int = 1
) {
    val priceAsDouble: Double
        get() = price.toDoubleOrNull() ?: 0.0
    
    val totalPrice: Double
        get() = priceAsDouble * quantity
}
