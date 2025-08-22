package com.example.cartapp.domain.usecase.cart

import com.example.cartapp.domain.repository.ProductRepository
import javax.inject.Inject

class RemoveFromCartUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(productId: String) {
        repository.removeFromCart(productId)
    }
} 