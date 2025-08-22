package com.example.cartapp.domain.usecase.home

import com.example.cartapp.domain.model.Product
import com.example.cartapp.domain.repository.ProductRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetProductsUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    operator fun invoke(
        limit: Int? = null,
        skip: Int? = null,
        sortBy: String? = null,
        order: String? = null,
        brand: String? = null,
        model: String? = null
    ): Flow<List<Product>> = repository.getProductsFlow(limit, skip, sortBy, order, brand, model)
}