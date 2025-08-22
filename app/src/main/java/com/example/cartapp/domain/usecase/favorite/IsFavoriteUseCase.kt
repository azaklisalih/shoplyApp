package com.example.cartapp.domain.usecase.favorite

import com.example.cartapp.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class IsFavoriteUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    operator fun invoke(productId: String): Flow<Boolean> {
        return repository.isFavorite(productId)
    }
} 