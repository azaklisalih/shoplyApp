package com.example.cartapp.presentation.productdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cartapp.domain.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.cartapp.domain.usecase.home.GetProductByIdUseCase
import com.example.cartapp.domain.usecase.cart.AddToCartUseCase
import com.example.cartapp.domain.usecase.favorite.AddToFavoritesUseCase
import com.example.cartapp.domain.usecase.favorite.RemoveFromFavoritesUseCase
import com.example.cartapp.domain.usecase.favorite.IsFavoriteUseCase
import com.example.cartapp.presentation.ui_state.ProductDetailUIState

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val getProductById: GetProductByIdUseCase,
    private val addToCart: AddToCartUseCase,
    private val addToFavorites: AddToFavoritesUseCase,
    private val removeFromFavorites: RemoveFromFavoritesUseCase,
    private val isFavorite: IsFavoriteUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProductDetailUIState())
    val uiState: StateFlow<ProductDetailUIState> = _uiState.asStateFlow()
    
    fun loadProduct(productId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            try {
                println("🔍 Loading product with ID: $productId")
                
                // Load product data
                getProductById(productId)
                    .collect { product ->
                        println("✅ Product loaded: ${product.name}")
                        _uiState.update { 
                            it.copy(
                                isLoading = false,
                                product = product
                            )
                        }
                        // Check if product is in favorites
                        checkFavoriteStatus(productId)
                    }
            } catch (e: Exception) {
                println("❌ Error loading product: ${e.message}")
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Unknown error"
                    )
                }
            }
        }
    }
    
    private fun checkFavoriteStatus(productId: String) {
        viewModelScope.launch {
            isFavorite(productId).collect { isFavorite ->
                _uiState.update { it.copy(isFavorite = isFavorite) }
            }
        }
    }
    
    fun addToCart() {
        viewModelScope.launch {
            try {
                val product = _uiState.value.product
                if (product != null) {
                    addToCart(product, 1)
                    _uiState.update { it.copy(isInCart = true) }
                    println("✅ Added to cart: ${product.name}")
                }
            } catch (e: Exception) {
                println("❌ Error adding to cart: ${e.message}")
                _uiState.update { it.copy(error = e.message ?: "Failed to add to cart") }
            }
        }
    }
    
    fun toggleFavorite() {
        viewModelScope.launch {
            try {
                val product = _uiState.value.product
                val currentState = _uiState.value.isFavorite

                if (product != null) {
                    if (currentState) {
                        removeFromFavorites(product.id)
                        println("❌ Removed from favorites: ${product.name}")
                    } else {
                        addToFavorites(product)
                        println("⭐ Added to favorites: ${product.name}")
                    }

                    _uiState.update { it.copy(isFavorite = !currentState) }
                }
            } catch (e: Exception) {
                println("❌ Error toggling favorite: ${e.message}")
                _uiState.update { it.copy(error = e.message ?: "Failed to toggle favorite") }
            }
        }
    }
}
