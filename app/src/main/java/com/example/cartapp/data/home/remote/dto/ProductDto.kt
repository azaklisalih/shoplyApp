package com.example.cartapp.data.home.remote.dto

data class ProductDto(
    val id: String,
    val name: String,
    val image: String,
    val price: String,
    val description: String,
    val model: String,
    val brand: String,
    val createdAt: String
)