package com.example.cartapp.domain.usecase.favorite

import com.example.cartapp.domain.model.Favorite
import com.example.cartapp.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoritesUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    operator fun invoke(): Flow<List<Favorite>> {
        return repository.getFavorites()
    }
} 