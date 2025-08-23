package com.example.cartapp.presentation.ui_state

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
    val selectedModels: Set<String> = emptySet(),
    val favoriteStates: Map<String, Boolean> = emptyMap(),
    val allBrands: List<String> = emptyList(),
    val allModels: List<String> = emptyList(),
    val filteredBrands: List<String> = emptyList(),
    val filteredModels: List<String> = emptyList(),
    val brandSearchQuery: String = "",
    val modelSearchQuery: String = "",
    val isFilterDataLoading: Boolean = false,
    val filterError: String? = null,
    val brandModelMap: Map<String, List<String>> = emptyMap(),
    val animatedCartProductId: String? = null,
    val animatedFavoriteProductId: String? = null
) 