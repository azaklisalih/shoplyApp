package com.example.cartapp.data.home.mapper

import com.example.cartapp.data.home.remote.dto.ProductDto
import com.example.cartapp.domain.model.Product

fun ProductDto.toDomain(): Product {
    return Product(
        id = id,
        name = name,
        image = image,
        price = price,
        description = description,
        model = model,
        brand = brand,
        createdAt = createdAt
    )
}

fun List<ProductDto>.toDomainList(): List<Product> {
    return map { it.toDomain() }
} 