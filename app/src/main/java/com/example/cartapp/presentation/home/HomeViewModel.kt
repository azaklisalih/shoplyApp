package com.example.cartapp.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cartapp.domain.model.Product
import com.example.cartapp.domain.model.ErrorMessage
import com.example.cartapp.domain.model.ErrorType
import com.example.cartapp.domain.usecase.home.GetProductsUseCase
import com.example.cartapp.domain.usecase.home.SearchProductsUseCase
import com.example.cartapp.domain.usecase.home.LocalSearchUseCase
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
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    private val searchProductsUseCase: SearchProductsUseCase,
    private val localSearchUseCase: LocalSearchUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val addToFavoritesUseCase: AddToFavoritesUseCase,
    private val removeFromFavoritesUseCase: RemoveFromFavoritesUseCase,
    private val observeFavoriteIdsUseCase: ObserveFavoriteIdsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUIState(isLoading = true))
    val uiState: StateFlow<HomeUIState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private var originalProducts: List<Product> = emptyList()

    init {
        fetchProducts()
        loadFilterData()
    }

    fun fetchProducts() {
        viewModelScope.launch {
            try {
                _uiState.update {
                    it.copy(
                        isLoading = true,
                        error = null,
                        page = 0,
                        isLocalSearchActive = false,
                        localSearchQuery = ""
                    )
                }

                val st = _uiState.value
                val selectedBrand = st.selectedBrands.firstOrNull()
                val selectedModel = st.selectedModels.firstOrNull()

                getProductsUseCase(
                    limit = st.pageSize,
                    skip = 0,
                    sortBy = st.selectedSortBy,
                    order = st.selectedSortOrder,
                    brand = selectedBrand,
                    model = selectedModel
                )
                    .combine(observeFavoriteIdsUseCase().distinctUntilChanged()) { products, favIds ->
                        val favMap = products.associate { p -> p.id to favIds.contains(p.id) }
                        products to favMap
                    }
                    .onStart { delay(500) }
                    .catch { e ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = e.message ?: ErrorMessage.UNKNOWN.key,
                                errorType = ErrorType.NetworkError
                            )
                        }
                    }
                    .collect { (products, favMap) ->
                        originalProducts = products
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
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: ErrorMessage.UNKNOWN.key,
                        errorType = ErrorType.UnknownError
                    )
                }
            }
        }
    }

    fun performSearch(query: String) {
        _searchQuery.value = query
        val st = _uiState.value
        if (hasActiveFilters(st)) {
            performLocalSearch(query)
        } else {
            performRemoteSearch(query)
        }
    }

    private fun hasActiveFilters(state: HomeUIState): Boolean =
        state.selectedBrands.isNotEmpty() || state.selectedModels.isNotEmpty()

    private fun performLocalSearch(query: String) {
        viewModelScope.launch {
            try {
                _uiState.update {
                    it.copy(
                        isLoading = true,
                        error = null,
                        page = 0,
                        isLocalSearchActive = query.isNotBlank(),
                        localSearchQuery = query
                    )
                }

                val base = originalProducts
                val filteredProducts = localSearchUseCase(
                    products = base,
                    query = query,
                    selectedBrands = _uiState.value.selectedBrands,
                    selectedModels = _uiState.value.selectedModels
                )

                val favIds = observeFavoriteIdsUseCase().first()
                val favMap = filteredProducts.associate { p -> p.id to favIds.contains(p.id) }

                _uiState.update {
                    it.copy(
                        products = filteredProducts,
                        favoriteStates = favMap,
                        isLoading = false,
                        total = filteredProducts.size,
                        page = 1
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: ErrorMessage.UNKNOWN.key,
                        errorType = ErrorType.UnknownError
                    )
                }
            }
        }
    }

    private fun performRemoteSearch(query: String) {
        if (query.isBlank()) {
            fetchProducts()
            return
        }

        viewModelScope.launch {
            try {
                _uiState.update {
                    it.copy(
                        isLoading = true,
                        error = null,
                        page = 0,
                        isLocalSearchActive = false,
                        localSearchQuery = ""
                    )
                }

                searchProductsUseCase.invoke(query)
                    .combine(observeFavoriteIdsUseCase().distinctUntilChanged()) { products, favIds ->
                        val favMap = products.associate { p -> p.id to favIds.contains(p.id) }
                        products to favMap
                    }
                    .onStart { delay(500) }
                    .catch { e ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = e.message ?: ErrorMessage.UNKNOWN.key,
                                errorType = ErrorType.NetworkError
                            )
                        }
                    }
                    .collect { (products, favMap) ->
                        _uiState.update {
                            it.copy(
                                products = products,
                                favoriteStates = favMap,
                                isLoading = false,
                                total = products.size,
                                page = 1,
                                isLocalSearchActive = false,
                                localSearchQuery = ""
                            )
                        }
                    }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: ErrorMessage.UNKNOWN.key,
                        errorType = ErrorType.UnknownError
                    )
                }
            }
        }
    }

    fun clearFilters() {
        _uiState.update {
            it.copy(
                selectedSortBy = null,
                selectedSortOrder = null,
                selectedBrands = emptySet(),
                selectedModels = emptySet(),
                page = 0,
                isLocalSearchActive = false,
                localSearchQuery = ""
            )
        }
        fetchProducts()
    }

    fun refreshHome() {
        _searchQuery.value = ""
        clearFilters()
    }

    fun addToCart(product: Product) {
        viewModelScope.launch {
            try {
                addToCartUseCase.invoke(product, 1)
                _uiState.update { it.copy(animatedCartProductId = product.id) }
                delay(2000)
                _uiState.update { it.copy(animatedCartProductId = null) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        error = e.message ?: ErrorMessage.FAILED_ADD_CART.key,
                        errorType = ErrorType.FailedAddCart
                    )
                }
            }
        }
    }

    fun toggleFavorite(product: Product) {
        viewModelScope.launch {
            try {
                val isFav = _uiState.value.favoriteStates[product.id] ?: false
                if (isFav) {
                    removeFromFavoritesUseCase.invoke(product.id)
                } else {
                    addToFavoritesUseCase.invoke(product)
                    _uiState.update { it.copy(animatedFavoriteProductId = product.id) }
                    delay(2000)
                    _uiState.update { it.copy(animatedFavoriteProductId = null) }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        error = e.message ?: ErrorMessage.FAILED_TOGGLE_FAVORITE.key,
                        errorType = ErrorType.FailedToggleFavorite
                    )
                }
            }
        }
    }

    private fun loadFilterData() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isFilterDataLoading = true, filterError = null) }

                val products = getProductsUseCase().first()

                val normalized = products.map { p ->
                    p.copy(brand = p.brand.trim(), model = p.model.trim())
                }

                val uniqueBrands = normalized.map { it.brand }.distinct().sorted()
                val uniqueModels = normalized.map { it.model }.distinct().sorted()

                val brandModelMap = normalized.groupBy { it.brand }
                    .mapValues { (_, list) -> list.map { it.model }.distinct().sorted() }

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
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isFilterDataLoading = false,
                        filterError = e.message ?: ErrorMessage.FAILED_LOAD_FILTER_DATA.key,
                        errorType = ErrorType.FailedLoadFilterData
                    )
                }
            }
        }
    }

    fun toggleBrand(brand: String) {
        val current = _uiState.value.selectedBrands.toMutableSet()
        if (current.contains(brand)) current.remove(brand)
        else {
            current.clear()
            current.add(brand)
        }
        _uiState.update { it.copy(selectedBrands = current, selectedModels = emptySet()) }
        updateFilteredModels()
    }

    fun toggleModel(model: String) {
        val current = _uiState.value.selectedModels.toMutableSet()
        if (current.contains(model)) current.remove(model)
        else {
            current.clear()
            current.add(model)
        }
        _uiState.update { it.copy(selectedModels = current) }
    }

    fun setSorting(sortBy: String?, order: String?) {
        _uiState.update { it.copy(selectedSortBy = sortBy, selectedSortOrder = order) }
    }

    fun updateBrandSearch(query: String) {
        val filtered = if (query.isEmpty()) {
            _uiState.value.allBrands
        } else {
            _uiState.value.allBrands.filter { it.contains(query, ignoreCase = true) }
        }
        _uiState.update { it.copy(brandSearchQuery = query, filteredBrands = filtered) }
    }

    fun updateModelSearch(query: String) {
        _uiState.update { it.copy(modelSearchQuery = query) }
        updateFilteredModels()
    }

    private fun updateFilteredModels() {
        val st = _uiState.value
        val availableModels = if (st.selectedBrands.isNotEmpty()) {
            val selectedBrand = st.selectedBrands.first()
            st.brandModelMap[selectedBrand] ?: emptyList()
        } else st.allModels

        val filtered = if (st.modelSearchQuery.isEmpty()) {
            availableModels
        } else {
            availableModels.filter { it.contains(st.modelSearchQuery, ignoreCase = true) }
        }
        _uiState.update { it.copy(filteredModels = filtered) }
    }

    fun applyFilters() {
        fetchProducts()
    }

    fun loadMoreProducts() {
        val snap = _uiState.value
        if (snap.isLoadingMore || snap.isLoading || snap.isLocalSearchActive) return

        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoadingMore = true) }

                val nextPage = snap.page + 1
                val skip = (nextPage - 1) * snap.pageSize
                val brand = snap.selectedBrands.firstOrNull()
                val model = snap.selectedModels.firstOrNull()

                getProductsUseCase(
                    limit = snap.pageSize,
                    skip = skip,
                    sortBy = snap.selectedSortBy,
                    order = snap.selectedSortOrder,
                    brand = brand,
                    model = model
                )
                    .combine(observeFavoriteIdsUseCase().distinctUntilChanged()) { newProducts, favIds ->
                        val favMap = newProducts.associate { p -> p.id to favIds.contains(p.id) }
                        newProducts to favMap
                    }
                    .catch { e ->
                        _uiState.update { it.copy(isLoadingMore = false, error = e.message) }
                    }
                    .collect { (newProducts, favMap) ->
                        _uiState.update { st ->
                            if (newProducts.isEmpty()) {
                                st.copy(isLoadingMore = false)
                            } else {
                                val merged = (st.products + newProducts).distinctBy { it.id }
                                val mergedFav =
                                    st.favoriteStates.toMutableMap().apply { putAll(favMap) }
                                originalProducts =
                                    (originalProducts + newProducts).distinctBy { it.id }
                                st.copy(
                                    products = merged,
                                    favoriteStates = mergedFav,
                                    isLoadingMore = false,
                                    page = nextPage,
                                    total = merged.size
                                )
                            }
                        }
                    }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoadingMore = false, error = e.message) }
            }
        }
    }
}
