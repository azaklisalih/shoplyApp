package com.example.cartapp.data.home.mapper

import com.example.cartapp.data.home.local.entities.CartItemEntity
import com.example.cartapp.domain.model.CartItem

fun CartItemEntity.toDomain(): CartItem {
    return CartItem(
        productId = productId,
        name = name,
        price = price,
        image = image,
        description = description,
        model = model,
        brand = brand,
        quantity = quantity
    )
}

fun CartItem.toEntity(): CartItemEntity {
    return CartItemEntity(
        productId = productId,
        name = name,
        price = price,
        image = image,
        description = description,
        model = model,
        brand = brand,
        quantity = quantity
    )
}

fun List<CartItemEntity>.toDomainList(): List<CartItem> {
    return map { it.toDomain() }
} 