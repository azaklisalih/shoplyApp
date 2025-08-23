package com.example.cartapp.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cartapp.R
import com.example.cartapp.domain.model.Product
import com.example.cartapp.domain.usecase.home.GetProductsUseCase
import com.example.cartapp.domain.usecase.home.SearchProductsUseCase
import com.example.cartapp.domain.usecase.cart.AddToCartUseCase
import com.example.cartapp.domain.usecase.favorite.AddToFavoritesUseCase
import com.example.cartapp.domain.usecase.favorite.RemoveFromFavoritesUseCase
import com.example.cartapp.domain.usecase.favorite.ObserveFavoriteIdsUseCase
import com.example.cartapp.presentation.ui_state.HomeUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getProducts: GetProductsUseCase,
    private val searchProducts: SearchProductsUseCase,
    private val addToCart: AddToCartUseCase,
    private val addToFavorites: AddToFavoritesUseCase,
    private val removeFromFavorites: RemoveFromFavoritesUseCase,
    private val observeFavoriteIds: ObserveFavoriteIdsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUIState(isLoading = true))
    val uiState: StateFlow<HomeUIState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    init {
        fetchProducts()
        loadFilterData()
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
                    .map { products -> applyLocalFilters(products, currentState) }
                    .combine(
                        observeFavoriteIds().distinctUntilChanged()
                    ) { products, favIds ->
                        val favMap = products.associate { p -> p.id to favIds.contains(p.id) }
                        products to favMap
                    }
                    .onStart {
                        delay(500)
                    }
                    .catch { exception ->
                        _uiState.update { it.copy(isLoading = false, error = exception.message) }
                    }
                    .collect { (products, favMap) ->
                        _uiState.update {
                            it.copy(
                                products = products,
                                favoriteStates = favMap,
                                isLoading = false,
                                total = products.size,
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

                searchProducts.invoke(query                )
                    .map { products -> applyLocalFilters(products, _uiState.value) }
                    .combine(
                        observeFavoriteIds().distinctUntilChanged()
                    ) { products, favIds ->
                        val favMap = products.associate { p -> p.id to favIds.contains(p.id) }
                        products to favMap
                    }
                    .onStart {
                        delay(500)
                    }
                    .catch { exception ->
                        _uiState.update { it.copy(isLoading = false, error = exception.message) }
                    }
                    .collect { (products, favMap) ->
                        _uiState.update {
                            it.copy(
                                products = products,
                                favoriteStates = favMap,
                                isLoading = false,
                                total = products.size,
                                page = 1
                            )
                        }
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

    fun refreshHome() {
        _searchQuery.value = ""
        clearFilters()
    }

    private fun clearFilterViewModelFilters() {
    }

    fun addToCart(product: Product) {
        viewModelScope.launch {
            try {
                addToCart.invoke(product, 1)

                _uiState.update { it.copy(animatedCartProductId = product.id) }

                delay(2000)
                _uiState.update { it.copy(animatedCartProductId = null) }

            } catch (exception: Exception) {
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

                    _uiState.update { it.copy(animatedFavoriteProductId = product.id) }

                    delay(2000)
                    _uiState.update { it.copy(animatedFavoriteProductId = null) }
                }
            } catch (exception: Exception) {
                _uiState.update { it.copy(error = exception.message) }
            }
        }
    }

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

                    val brandModelMap = normalizedProducts.groupBy { it.brand }
                        .mapValues { (_, products) ->
                            products.map { it.model }.distinct().sorted()
                        }

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
                        filterError = e.message ?: R.string.error_failed_load_filter_data.toString()
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
            currentBrands.clear()
            currentBrands.add(brand)
        }

        _uiState.update {
            it.copy(
                selectedBrands = currentBrands,
                selectedModels = emptySet()
            )
        }
        updateFilteredModels()
    }

    fun toggleModel(model: String) {
        val currentModels = _uiState.value.selectedModels.toMutableSet()
        if (currentModels.contains(model)) {
            currentModels.remove(model)
        } else {
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

        val availableModels = if (currentState.selectedBrands.isNotEmpty()) {
            val selectedBrand = currentState.selectedBrands.first()
            currentState.brandModelMap[selectedBrand] ?: emptyList()
        } else {
            currentState.allModels
        }

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
                    .map { newProducts -> applyLocalFilters(newProducts, currentState) }
                    .combine(
                        observeFavoriteIds().distinctUntilChanged()
                    ) { newProducts, favIds ->
                        val favMap = newProducts.associate { p -> p.id to favIds.contains(p.id) }
                        newProducts to favMap
                    }
                    .catch { exception ->
                        _uiState.update {
                            it.copy(
                                isLoadingMore = false,
                                error = exception.message
                            )
                        }
                    }
                    .collect { (newProducts, favMap) ->
                        if (newProducts.isNotEmpty()) {
                            val allProducts = currentState.products + newProducts
                            val allFavMap = currentState.favoriteStates.toMutableMap()
                            allFavMap.putAll(favMap)

                            _uiState.update {
                                it.copy(
                                    products = allProducts,
                                    favoriteStates = allFavMap,
                                    isLoadingMore = false,
                                    page = nextPage,
                                    total = allProducts.size
                                )
                            }
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