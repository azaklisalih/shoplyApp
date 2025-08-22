package com.example.cartapp.presentation.home

import com.example.cartapp.domain.model.Product

data class HomeUIState(
    val products: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val error: String? = null,
    val total: Int = 0,
    val page: Int = 0,
    val pageSize: Int = 20,
    val selectedCategory: String? = null,
    val selectedSortBy: String? = null,
    val selectedSortOrder: String? = null,
    val selectedBrands: Set<String> = emptySet(),
    val selectedModels: Set<String> = emptySet()
)
