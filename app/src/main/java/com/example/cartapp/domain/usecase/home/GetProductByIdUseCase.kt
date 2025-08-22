package com.example.cartapp.domain.usecase.home

import com.example.cartapp.domain.model.Product
import com.example.cartapp.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProductByIdUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    operator fun invoke(id: String): Flow<Product> {
        return repository.getProductByIdFlow(id)
    }
} 