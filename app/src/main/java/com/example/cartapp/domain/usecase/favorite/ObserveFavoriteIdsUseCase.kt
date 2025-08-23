package com.example.cartapp.domain.usecase.favorite

import com.example.cartapp.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveFavoriteIdsUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    operator fun invoke(): Flow<Set<String>> {
        return repository.observeFavoriteIds()
    }
} 