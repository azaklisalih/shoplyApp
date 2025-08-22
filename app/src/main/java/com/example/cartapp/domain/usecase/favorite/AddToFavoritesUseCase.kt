package com.example.cartapp.domain.usecase.favorite

import com.example.cartapp.domain.model.Product
import com.example.cartapp.domain.repository.ProductRepository
import javax.inject.Inject

class AddToFavoritesUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(product: Product) {
        repository.addToFavorites(product)
    }
} 