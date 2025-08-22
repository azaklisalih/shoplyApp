package com.example.cartapp.domain.usecase.productdetail

import com.example.cartapp.domain.repository.FavoriteRepository
import javax.inject.Inject

class CheckFavoriteUseCase @Inject constructor(
    private val favoriteRepository: FavoriteRepository
) {
    suspend operator fun invoke(productId: Int): Boolean {
        return favoriteRepository.isFavorite(productId)
    }
}
