package com.example.cartapp.domain.usecase.productdetail

import com.example.cartapp.domain.repository.FavoriteRepository
import javax.inject.Inject

class RemoveFavoriteUseCase @Inject constructor(
    private val favoriteRepository: FavoriteRepository
) {
    suspend operator fun invoke(productId: Int) {
        favoriteRepository.removeFavorite(productId)
    }
}