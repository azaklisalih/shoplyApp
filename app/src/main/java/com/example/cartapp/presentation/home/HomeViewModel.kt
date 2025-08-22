package com.example.cartapp.presentation.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cartapp.domain.usecase.home.GetProductsUseCase
import com.example.cartapp.domain.usecase.home.SearchProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getProducts: GetProductsUseCase,
    private val searchProducts: SearchProductsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUIState(isLoading = true))
    val uiState: StateFlow<HomeUIState> = _uiState.asStateFlow()

    val searchQuery = MutableLiveData("")

    init {
        fetchProducts()
    }

    fun fetchProducts() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true, error = null, page = 0) }
                
                val currentState = _uiState.value
                
                // Handle multiple brands/models - API might not support multiple values
                // So we'll use the first selected brand/model for API call
                val selectedBrand = currentState.selectedBrands.firstOrNull()
                val selectedModel = currentState.selectedModels.firstOrNull()
                
                getProducts(
                    limit = currentState.pageSize, 
                    skip = 0, 
                    sortBy = currentState.selectedSortBy,
                    order = currentState.selectedSortOrder,
                    brand = selectedBrand,
                    model = selectedModel
                )
                    .onStart {
                        delay(500)
                    }
                    .catch { exception ->
                        _uiState.update { it.copy(isLoading = false, error = exception.message) }
                    }
                    .collect { products ->
                        // Apply additional local filtering for multiple brands/models
                        val filteredProducts = applyLocalFilters(products, currentState)
                        
                        _uiState.update { 
                            it.copy(
                                products = filteredProducts,
                                isLoading = false,
                                total = filteredProducts.size,
                                page = 1
                            )
                        }
                    }
            } catch (exception: Exception) {
                _uiState.update { it.copy(isLoading = false, error = exception.message) }
            }
        }
    }

    fun searchProducts(query: String) {
        if (query.isBlank()) {
            fetchProducts()
            return
        }
        
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true, error = null, page = 0) }
                
                searchProducts.invoke(query)
                    .onStart {
                        delay(500)
                    }
                    .catch { exception ->
                        _uiState.update { it.copy(isLoading = false, error = exception.message) }
                    }
                    .collect { products ->
                        // Apply current filters to search results
                        val currentState = _uiState.value
                        val filteredProducts = applyLocalFilters(products, currentState)
                        
                        _uiState.update { 
                            it.copy(
                                products = filteredProducts,
                                isLoading = false,
                                total = filteredProducts.size,
                                page = 1
                            )
                        }
                    }
            } catch (exception: Exception) {
                _uiState.update { it.copy(isLoading = false, error = exception.message) }
            }
        }
    }

    // Apply local filters for multiple brands/models
    private fun applyLocalFilters(products: List<com.example.cartapp.domain.model.Product>, state: HomeUIState): List<com.example.cartapp.domain.model.Product> {
        var filteredProducts = products
        
        // Apply brand filter if multiple brands are selected
        if (state.selectedBrands.isNotEmpty()) {
            filteredProducts = filteredProducts.filter { product ->
                state.selectedBrands.any { selectedBrand ->
                    product.brand.trim().equals(selectedBrand.trim(), ignoreCase = true)
                }
            }
        }
        
        // Apply model filter if multiple models are selected
        if (state.selectedModels.isNotEmpty()) {
            filteredProducts = filteredProducts.filter { product ->
                state.selectedModels.any { selectedModel ->
                    product.model.trim().equals(selectedModel.trim(), ignoreCase = true)
                }
            }
        }
        
        return filteredProducts
    }

    // API Filter functions for FilterFragment
    fun applyFilters(sortBy: String?, order: String?) {
        _uiState.update { 
            it.copy(
                selectedSortBy = sortBy,
                selectedSortOrder = order,
                page = 0
            )
        }
        fetchProducts()
    }
    
    fun applyBrandFilter(brands: Set<String>) {
        _uiState.update { 
            it.copy(
                selectedBrands = brands,
                page = 0
            )
        }
        fetchProducts()
    }
    
    fun applyModelFilter(models: Set<String>) {
        _uiState.update { 
            it.copy(
                selectedModels = models,
                page = 0
            )
        }
        fetchProducts()
    }
    
    // Clear all filters
    fun clearFilters() {
        _uiState.update { 
            it.copy(
                selectedSortBy = null,
                selectedSortOrder = null,
                selectedBrands = emptySet(),
                selectedModels = emptySet(),
                page = 0
            )
        }
        fetchProducts()
    }
    
    // Functions for HomeFragment
    fun addToCart(product: com.example.cartapp.domain.model.Product) {
        // TODO: Implement add to cart functionality
        println("✅ Added to cart: ${product.name}")
    }
    
    fun toggleFavorite(product: com.example.cartapp.domain.model.Product) {
        // TODO: Implement favorite functionality
        println("⭐ Toggled favorite: ${product.name}")
    }
    
    fun loadMoreProducts() {
        val currentState = _uiState.value
        
        // Don't load more if already loading or if we have all products
        if (currentState.isLoadingMore || currentState.isLoading) {
            return
        }
        
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoadingMore = true) }
                
                val nextPage = currentState.page + 1
                val skip = (nextPage - 1) * currentState.pageSize
                
                // Handle multiple brands/models - API might not support multiple values
                val selectedBrand = currentState.selectedBrands.firstOrNull()
                val selectedModel = currentState.selectedModels.firstOrNull()
                
                getProducts(
                    limit = currentState.pageSize,
                    skip = skip,
                    sortBy = currentState.selectedSortBy,
                    order = currentState.selectedSortOrder,
                    brand = selectedBrand,
                    model = selectedModel
                )
                    .catch { exception ->
                        _uiState.update { it.copy(isLoadingMore = false, error = exception.message) }
                    }
                    .collect { newProducts ->
                        if (newProducts.isNotEmpty()) {
                            // Apply local filters to new products
                            val filteredNewProducts = applyLocalFilters(newProducts, currentState)
                            
                            // Combine with existing products
                            val allProducts = currentState.products + filteredNewProducts
                            
                            _uiState.update { 
                                it.copy(
                                    products = allProducts,
                                    isLoadingMore = false,
                                    page = nextPage,
                                    total = allProducts.size
                                )
                            }
                        } else {
                            // No more products to load
                            _uiState.update { it.copy(isLoadingMore = false) }
                        }
                    }
            } catch (exception: Exception) {
                _uiState.update { it.copy(isLoadingMore = false, error = exception.message) }
            }
        }
    }
    
    val favoriteStates: kotlinx.coroutines.flow.StateFlow<Map<String, Boolean>> = 
        kotlinx.coroutines.flow.MutableStateFlow<Map<String, Boolean>>(emptyMap()).asStateFlow()
}