package com.example.cartapp.presentation.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cartapp.domain.model.Favorite
import com.example.cartapp.domain.usecase.favorite.GetFavoritesUseCase
import com.example.cartapp.domain.usecase.favorite.RemoveFromFavoritesUseCase
import com.example.cartapp.domain.usecase.cart.AddToCartUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FavoriteUIState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val favorites: List<Favorite> = emptyList()
)

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val getFavoritesUseCase: GetFavoritesUseCase,
    private val removeFromFavoritesUseCase: RemoveFromFavoritesUseCase,
    private val addToCartUseCase: AddToCartUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(FavoriteUIState())
    val uiState: StateFlow<FavoriteUIState> = _uiState.asStateFlow()
    
    init {
        loadFavorites()
    }
    
    private fun loadFavorites() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            try {
                // Get favorites once
                val favorites = getFavoritesUseCase().first()
                println("📋 Loaded ${favorites.size} favorites")
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        favorites = favorites,
                        error = null
                    )
                }
            } catch (e: Exception) {
                println("❌ Error loading favorites: ${e.message}")
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Unknown error"
                    )
                }
            }
        }
    }
    
    fun removeFromFavorites(productId: String) {
        viewModelScope.launch {
            try {
                println("🔄 Removing from favorites: $productId")
                removeFromFavoritesUseCase(productId)
                println("✅ Successfully removed from favorites: $productId")
                
                // Reload favorites list after removal
                val updatedFavorites = getFavoritesUseCase().first()
                _uiState.update { 
                    it.copy(
                        favorites = updatedFavorites,
                        error = null
                    )
                }
                println("📋 Updated favorites list: ${updatedFavorites.size} items")
            } catch (e: Exception) {
                println("❌ Error removing from favorites: ${e.message}")
                _uiState.update { it.copy(error = e.message ?: "Failed to remove from favorites") }
            }
        }
    }
    
    fun addToCart(product: com.example.cartapp.domain.model.Product) {
        viewModelScope.launch {
            try {
                addToCartUseCase(product, 1)
                println("✅ Added to cart from favorites: ${product.name}")
            } catch (e: Exception) {
                println("❌ Error adding to cart from favorites: ${e.message}")
            }
        }
    }
} 