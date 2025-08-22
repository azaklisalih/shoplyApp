package com.example.cartapp.domain.usecase.cart

import com.example.cartapp.domain.repository.CartRepository
import javax.inject.Inject

class CheckoutUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    /**
     * Checkout sürecini başlatır.
     */
    suspend operator fun invoke() {
    }
}