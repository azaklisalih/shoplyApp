package com.example.cartapp.data.home.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartItemEntity(
    @PrimaryKey val productId: String,
    val name: String,
    val price: String,
    val image: String,
    val description: String,
    val model: String,
    val brand: String,
    val quantity: Int = 1
)
