package com.example.cartapp.domain.usecase.home

import com.example.cartapp.domain.model.Product
import javax.inject.Inject

class LocalSearchUseCase @Inject constructor() {
    
    operator fun invoke(
        products: List<Product>,
        query: String,
        selectedBrands: Set<String> = emptySet(),
        selectedModels: Set<String> = emptySet()
    ): List<Product> {
        if (query.isBlank()) {
            return applyFilters(products, selectedBrands, selectedModels)
        }
        
        val searchQuery = query.trim().lowercase()
        
        return products.filter { product ->
            val matchesSearch = product.name.contains(searchQuery, ignoreCase = true) ||
                    product.description.contains(searchQuery, ignoreCase = true) ||
                    product.brand.contains(searchQuery, ignoreCase = true) ||
                    product.model.contains(searchQuery, ignoreCase = true)
            
            val matchesFilters = applyFilters(listOf(product), selectedBrands, selectedModels).isNotEmpty()
            
            matchesSearch && matchesFilters
        }
    }
    
    private fun applyFilters(
        products: List<Product>,
        selectedBrands: Set<String>,
        selectedModels: Set<String>
    ): List<Product> {
        var filteredProducts = products
        
        if (selectedBrands.isNotEmpty()) {
            filteredProducts = filteredProducts.filter { product ->
                selectedBrands.any { selectedBrand ->
                    product.brand.trim().equals(selectedBrand.trim(), ignoreCase = true)
                }
            }
        }
        
        if (selectedModels.isNotEmpty()) {
            filteredProducts = filteredProducts.filter { product ->
                selectedModels.any { selectedModel ->
                    product.model.trim().equals(selectedModel.trim(), ignoreCase = true)
                }
            }
        }
        
        return filteredProducts
    }
} 