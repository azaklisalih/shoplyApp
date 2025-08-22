package com.example.cartapp.data.home.mapper

import com.example.cartapp.data.home.local.entities.FavoriteEntity
import com.example.cartapp.domain.model.Favorite

fun FavoriteEntity.toDomain(): Favorite {
    return Favorite(
        productId = productId,
        name = name,
        price = price,
        image = image,
        description = description,
        model = model,
        brand = brand,
        createdAt = createdAt
    )
}

fun Favorite.toEntity(): FavoriteEntity {
    return FavoriteEntity(
        productId = productId,
        name = name,
        price = price,
        image = image,
        description = description,
        model = model,
        brand = brand,
        createdAt = createdAt
    )
}

fun List<FavoriteEntity>.toDomainList(): List<Favorite> {
    return map { it.toDomain() }
} 