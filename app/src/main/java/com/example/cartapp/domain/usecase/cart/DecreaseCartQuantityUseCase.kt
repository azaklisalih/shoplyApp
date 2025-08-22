package com.example.cartapp.domain.usecase.cart

import com.example.cartapp.domain.repository.CartRepository
import javax.inject.Inject

class DecreaseCartQuantityUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(productId: Int) {
        cartRepository.decrease(productId)
    }
}