package com.example.cartapp.presentation.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cartapp.domain.model.Product
import com.example.cartapp.domain.usecase.home.GetProductsUseCase
import com.example.cartapp.domain.usecase.home.SearchProductsUseCase
import com.example.cartapp.domain.usecase.cart.AddToCartUseCase
import com.example.cartapp.domain.usecase.favorite.AddToFavoritesUseCase
import com.example.cartapp.domain.usecase.favorite.RemoveFromFavoritesUseCase
import com.example.cartapp.domain.usecase.favorite.IsFavoriteUseCase
import com.example.cartapp.presentation.ui_state.HomeUIState
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
    private val searchProducts: SearchProductsUseCase,
    private val addToCart: AddToCartUseCase,
    private val addToFavorites: AddToFavoritesUseCase,
    private val removeFromFavorites: RemoveFromFavoritesUseCase,
    private val isFavorite: IsFavoriteUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUIState(isLoading = true))
    val uiState: StateFlow<HomeUIState> = _uiState.asStateFlow()

    val searchQuery = MutableLiveData("")

    init {
        fetchProducts()
        loadFilterData()
        observeFavoriteStates()
    }
    
    private fun observeFavoriteStates() {
        viewModelScope.launch {
            // Observe favorite states for all products
            _uiState.value.products.forEach { product ->
                isFavorite.invoke(product.id).collect { isFav ->
                    updateFavoriteState(product.id, isFav)
                }
            }
        }
    }
    
    private fun observeFavoriteStatesForProducts(products: List<Product>) {
        viewModelScope.launch {
            products.forEach { product ->
                isFavorite.invoke(product.id).collect { isFav ->
                    updateFavoriteState(product.id, isFav)
                }
            }
        }
    }
    


    fun fetchProducts() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true, error = null, page = 0) }
                
                val currentState = _uiState.value

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
                        observeFavoriteStatesForProducts(filteredProducts)
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
                        
                        observeFavoriteStatesForProducts(filteredProducts)
                    }
            } catch (exception: Exception) {
                _uiState.update { it.copy(isLoading = false, error = exception.message) }
            }
        }
    }

    private fun applyLocalFilters(products: List<Product>, state: HomeUIState): List<Product> {
        var filteredProducts = products
        
        if (state.selectedBrands.isNotEmpty()) {
            filteredProducts = filteredProducts.filter { product ->
                state.selectedBrands.any { selectedBrand ->
                    product.brand.trim().equals(selectedBrand.trim(), ignoreCase = true)
                }
            }
        }
        
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
    fun applyAllFilters(
        sortBy: String?, 
        order: String?, 
        brands: Set<String>, 
        models: Set<String>
    ) {
        _uiState.update { 
            it.copy(
                selectedSortBy = sortBy,
                selectedSortOrder = order,
                selectedBrands = brands,
                selectedModels = models,
                page = 0
            )
        }
        fetchProducts()
    }
    
    fun applyFilters(sortBy: String?, order: String?) {
        applyAllFilters(sortBy, order, _uiState.value.selectedBrands, _uiState.value.selectedModels)
    }
    
    fun applyBrandFilter(brands: Set<String>) {
        applyAllFilters(_uiState.value.selectedSortBy, _uiState.value.selectedSortOrder, brands, _uiState.value.selectedModels)
    }
    
    fun applyModelFilter(models: Set<String>) {
        applyAllFilters(_uiState.value.selectedSortBy, _uiState.value.selectedSortOrder, _uiState.value.selectedBrands, models)
    }
    
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
        
        clearFilterViewModelFilters()
    }
    
    private fun clearFilterViewModelFilters() {
    }
    
    fun addToCart(product: Product) {
        viewModelScope.launch {
            try {
                addToCart.invoke(product, 1)
                // You can add a success message or snackbar here
            } catch (exception: Exception) {
                // Handle error
                _uiState.update { it.copy(error = exception.message) }
            }
        }
    }
    
    fun toggleFavorite(product: Product) {
        viewModelScope.launch {
            try {
                val isCurrentlyFavorite = _uiState.value.favoriteStates[product.id] ?: false
                
                if (isCurrentlyFavorite) {
                    removeFromFavorites.invoke(product.id)
                } else {
                    addToFavorites.invoke(product)
                }
                
                updateFavoriteState(product.id, !isCurrentlyFavorite)
            } catch (exception: Exception) {
                _uiState.update { it.copy(error = exception.message) }
            }
        }
    }
    
    private fun updateFavoriteState(productId: String, isFavorite: Boolean) {
        val currentStates = (_uiState.value.favoriteStates as MutableMap<String, Boolean>).toMutableMap()
        currentStates[productId] = isFavorite
        _uiState.update { it.copy(favoriteStates = currentStates) }
    }
    
    // Filter functionality
    private fun loadFilterData() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isFilterDataLoading = true, filterError = null) }
                
                getProducts().collect { products ->
                    val normalizedProducts = products.map { product ->
                        product.copy(
                            brand = product.brand.trim(),
                            model = product.model.trim()
                        )
                    }
                    
                    val uniqueBrands = normalizedProducts.map { it.brand }.distinct().sorted()
                    val uniqueModels = normalizedProducts.map { it.model }.distinct().sorted()
                    
                    // Create brand-model mapping
                    val brandModelMap = normalizedProducts.groupBy { it.brand }
                        .mapValues { (_, products) -> products.map { it.model }.distinct().sorted() }
                    
                    _uiState.update { 
                        it.copy(
                            allBrands = uniqueBrands,
                            allModels = uniqueModels,
                            filteredBrands = uniqueBrands,
                            filteredModels = uniqueModels,
                            brandModelMap = brandModelMap,
                            isFilterDataLoading = false
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isFilterDataLoading = false,
                        filterError = e.message ?: "Failed to load filter data"
                    )
                }
            }
        }
    }
    
    fun toggleBrand(brand: String) {
        val currentBrands = _uiState.value.selectedBrands.toMutableSet()
        if (currentBrands.contains(brand)) {
            // Remove the brand
            currentBrands.remove(brand)
        } else {
            // Clear other brands and add only this one (single selection)
            currentBrands.clear()
            currentBrands.add(brand)
        }
        
        // Clear model selection when brand changes
        _uiState.update { 
            it.copy(
                selectedBrands = currentBrands,
                selectedModels = emptySet()
            )
        }
        
        // Update filtered models based on selected brands
        updateFilteredModels()
    }
    
    fun toggleModel(model: String) {
        val currentModels = _uiState.value.selectedModels.toMutableSet()
        if (currentModels.contains(model)) {
            // Remove the model
            currentModels.remove(model)
        } else {
            // Clear other models and add only this one (single selection)
            currentModels.clear()
            currentModels.add(model)
        }
        _uiState.update { it.copy(selectedModels = currentModels) }
    }
    
    fun setSorting(sortBy: String?, order: String?) {
        _uiState.update { 
            it.copy(
                selectedSortBy = sortBy,
                selectedSortOrder = order
            )
        }
    }
    
    fun updateBrandSearch(query: String) {
        val filteredBrands = if (query.isEmpty()) {
            _uiState.value.allBrands
        } else {
            _uiState.value.allBrands.filter { brand ->
                brand.contains(query, ignoreCase = true)
            }
        }
        _uiState.update { 
            it.copy(
                brandSearchQuery = query,
                filteredBrands = filteredBrands
            )
        }
    }
    
    fun updateModelSearch(query: String) {
        _uiState.update { it.copy(modelSearchQuery = query) }
        updateFilteredModels()
    }
    
    private fun updateFilteredModels() {
        val currentState = _uiState.value
        
        // Get available models based on selected brand
        val availableModels = if (currentState.selectedBrands.isNotEmpty()) {
            val selectedBrand = currentState.selectedBrands.first()
            // Use brand-model mapping to get models for selected brand
            currentState.brandModelMap[selectedBrand] ?: emptyList()
        } else {
            currentState.allModels
        }
        
        // Apply search filter
        val filteredModels = if (currentState.modelSearchQuery.isEmpty()) {
            availableModels
        } else {
            availableModels.filter { model ->
                model.contains(currentState.modelSearchQuery, ignoreCase = true)
            }
        }
        
        _uiState.update { 
            it.copy(filteredModels = filteredModels)
        }
    }
    
    fun applyFilters() {
        fetchProducts()
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
                            
                            // Observe favorite states for new products
                            observeFavoriteStatesForProducts(filteredNewProducts)
                        } else {
                            _uiState.update { it.copy(isLoadingMore = false) }
                        }
                    }
            } catch (exception: Exception) {
                _uiState.update { it.copy(isLoadingMore = false, error = exception.message) }
            }
        }
    }
    
}