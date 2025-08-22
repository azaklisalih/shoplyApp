package com.example.cartapp.presentation.filter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cartapp.domain.usecase.home.GetProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FilterUIState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val brands: List<String> = emptyList(),
    val models: List<String> = emptyList(),
    val filteredBrands: List<String> = emptyList(),
    val filteredModels: List<String> = emptyList(),
    val selectedBrands: Set<String> = emptySet(),
    val selectedModels: Set<String> = emptySet(),
    val selectedSortBy: String? = null,
    val selectedOrder: String? = null,
    val brandSearchQuery: String = "",
    val modelSearchQuery: String = "",
    val allProducts: List<com.example.cartapp.domain.model.Product> = emptyList()
)

@HiltViewModel
class FilterViewModel @Inject constructor(
    private val getProducts: com.example.cartapp.domain.usecase.home.GetProductsUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(FilterUIState(isLoading = true))
    val uiState: StateFlow<FilterUIState> = _uiState.asStateFlow()
    
    init {
        loadFilterData()
    }
    
    private fun loadFilterData() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true, error = null) }
                
                // Load products first to get brand-model relationships
                getProducts.invoke().collect { products ->
                    // Normalize brand and model names (trim whitespace, handle case)
                    val normalizedProducts = products.map { product ->
                        product.copy(
                            brand = product.brand.trim(),
                            model = product.model.trim()
                        )
                    }
                    
                    // Extract unique brands and models from products
                    val uniqueBrands = normalizedProducts.map { it.brand }.distinct().sorted()
                    val uniqueModels = normalizedProducts.map { it.model }.distinct().sorted()
                    
                    // Group models by brand
                    val modelsByBrand = normalizedProducts.groupBy { it.brand }
                        .mapValues { (_, products) -> products.map { it.model }.distinct().sorted() }
                    
                    _uiState.update { 
                        it.copy(
                            brands = uniqueBrands,
                            filteredBrands = uniqueBrands,
                            models = uniqueModels,
                            filteredModels = uniqueModels,
                            allProducts = normalizedProducts,
                            isLoading = false
                        )
                    }
                }
                
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load filter data"
                    )
                }
            }
        }
    }
    
    fun toggleBrand(brand: String) {
        val currentBrands = _uiState.value.selectedBrands.toMutableSet()
        if (currentBrands.contains(brand)) {
            currentBrands.remove(brand)
        } else {
            currentBrands.add(brand)
        }
        
        // Clear model selection when brand changes
        val currentModels = _uiState.value.selectedModels.toMutableSet()
        currentModels.clear()
        
        _uiState.update { 
            it.copy(
                selectedBrands = currentBrands,
                selectedModels = currentModels
            )
        }
        
        // Update model search to reflect brand changes
        updateModelSearch(_uiState.value.modelSearchQuery)
    }
    
    fun toggleModel(model: String) {
        val currentModels = _uiState.value.selectedModels.toMutableSet()
        if (currentModels.contains(model)) {
            currentModels.remove(model)
        } else {
            currentModels.add(model)
        }
        _uiState.update { it.copy(selectedModels = currentModels) }
    }
    
    fun setSorting(sortBy: String?, order: String?) {
        _uiState.update { 
            it.copy(
                selectedSortBy = sortBy,
                selectedOrder = order
            )
        }
    }
    
    fun clearFilters() {
        _uiState.update { 
            it.copy(
                selectedBrands = emptySet(),
                selectedModels = emptySet(),
                selectedSortBy = null,
                selectedOrder = null
            )
        }
    }
    
    fun updateBrandSearch(query: String) {
        _uiState.update { currentState ->
            val filteredBrands = if (query.isEmpty()) {
                currentState.brands
            } else {
                currentState.brands.filter { brand ->
                    brand.contains(query, ignoreCase = true)
                }
            }
            currentState.copy(
                brandSearchQuery = query,
                filteredBrands = filteredBrands
            )
        }
    }
    
    fun updateModelSearch(query: String) {
        _uiState.update { currentState ->
            // Get models that belong to selected brands
            val availableModels = if (currentState.selectedBrands.isNotEmpty()) {
                // Filter products by selected brands and get their models
                currentState.allProducts
                    .filter { product ->
                        currentState.selectedBrands.any { selectedBrand ->
                            product.brand.trim().equals(selectedBrand.trim(), ignoreCase = true)
                        }
                    }
                    .map { it.model }
                    .distinct()
                    .sorted()
            } else {
                // If no brands selected, show all models
                currentState.models
            }
            
            // Then filter by search query
            val filteredModels = if (query.isEmpty()) {
                availableModels
            } else {
                availableModels.filter { model ->
                    model.trim().contains(query.trim(), ignoreCase = true)
                }
            }
            
            currentState.copy(
                modelSearchQuery = query,
                filteredModels = filteredModels
            )
        }
    }
} 