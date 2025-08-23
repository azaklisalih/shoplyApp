package com.example.cartapp.domain.usecase.favorite

import com.example.cartapp.domain.model.Favorite
import com.example.cartapp.domain.model.Product
import javax.inject.Inject

class ConvertFavoriteToProductUseCase @Inject constructor() {
    operator fun invoke(favorite: Favorite): Product {
        return Product(
            id = favorite.productId,
            name = favorite.name,
            price = favorite.price,
            image = favorite.image,
            description = favorite.description,
            model = favorite.model,
            brand = favorite.brand,
            createdAt = favorite.createdAt
        )
    }
} 