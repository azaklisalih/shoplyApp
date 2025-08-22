package com.example.cartapp.domain.usecase.filter

import com.example.cartapp.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetBrandsUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    operator fun invoke(): Flow<List<String>> {
        return repository.getProductsFlow().map { products ->
            products.map { it.brand }
                .distinct()
                .sorted()
        }
    }
} 