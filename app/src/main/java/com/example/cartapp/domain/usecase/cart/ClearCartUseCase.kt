package com.example.cartapp.domain.usecase.cart

import com.example.cartapp.domain.repository.CartRepository
import javax.inject.Inject

class ClearCartUseCase @Inject constructor(
    private val repository: CartRepository
) {
    suspend operator fun invoke() {
        repository.clearCart()
    }
} 