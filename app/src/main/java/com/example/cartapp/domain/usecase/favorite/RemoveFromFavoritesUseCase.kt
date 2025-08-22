package com.example.cartapp.domain.usecase.favorite

import com.example.cartapp.domain.repository.ProductRepository
import javax.inject.Inject

class RemoveFromFavoritesUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(productId: String) {
        repository.removeFromFavorites(productId)
    }
} 