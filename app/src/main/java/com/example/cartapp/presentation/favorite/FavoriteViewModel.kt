package com.example.cartapp.presentation.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cartapp.R
import com.example.cartapp.domain.model.Favorite
import com.example.cartapp.domain.usecase.favorite.GetFavoritesUseCase
import com.example.cartapp.domain.usecase.favorite.RemoveFromFavoritesUseCase
import com.example.cartapp.domain.usecase.favorite.ConvertFavoriteToProductUseCase
import com.example.cartapp.domain.usecase.cart.AddToCartUseCase
import com.example.cartapp.presentation.ui_state.FavoriteUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val getFavoritesUseCase: GetFavoritesUseCase,
    private val removeFromFavoritesUseCase: RemoveFromFavoritesUseCase,
    private val convertFavoriteToProduct: ConvertFavoriteToProductUseCase,
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
                val favorites = getFavoritesUseCase().first()
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        favorites = favorites,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: R.string.error_unknown.toString()
                    )
                }
            }
        }
    }
    
    fun removeFromFavorites(productId: String) {
        viewModelScope.launch {
            try {
                removeFromFavoritesUseCase(productId)

                val updatedFavorites = getFavoritesUseCase().first()
                _uiState.update { 
                    it.copy(
                        favorites = updatedFavorites,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message ?: R.string.error_failed_remove_favorites.toString()) }
            }
        }
    }
    
    fun addToCart(favorite: com.example.cartapp.domain.model.Favorite) {
        viewModelScope.launch {
            try {
                val product = convertFavoriteToProduct(favorite)
                addToCartUseCase(product, 1)

                _uiState.update { it.copy(animatedCartProductId = favorite.productId) }
                
                delay(2000)
                _uiState.update { it.copy(animatedCartProductId = null) }
                
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message ?: R.string.error_failed_add_cart.toString()) }
            }
        }
    }
} 