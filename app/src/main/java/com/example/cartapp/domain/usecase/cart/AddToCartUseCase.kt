package com.example.cartapp.domain.usecase.cart

import com.example.cartapp.domain.model.Product
import com.example.cartapp.domain.repository.ProductRepository
import javax.inject.Inject

class AddToCartUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(product: Product, quantity: Int = 1) {
        repository.addToCart(product, quantity)
    }
} 