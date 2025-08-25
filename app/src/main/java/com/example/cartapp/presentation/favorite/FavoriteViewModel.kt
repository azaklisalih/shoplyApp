package com.example.cartapp.presentation.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cartapp.domain.model.Favorite
import com.example.cartapp.domain.model.ErrorMessage
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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
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

    private val favoritesFlow: StateFlow<List<Favorite>> =
        getFavoritesUseCase()
            .distinctUntilChanged()
            .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    
    init {
        loadFavorites()
        observeFavorites()
    }

    private fun observeFavorites() {
        viewModelScope.launch {
            favoritesFlow.collect { favs ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        favorites = favs,
                        error = null
                    )
                }
            }
        }
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
                        error = e.message ?: ErrorMessage.UNKNOWN.key
                    )
                }
            }
        }
    }
    
    fun removeFromFavorites(productId: String) {
        viewModelScope.launch {
            val prev = _uiState.value.favorites
            _uiState.update { it.copy(favorites = prev.filterNot { f -> f.productId == productId }) }

            runCatching {
                removeFromFavoritesUseCase(productId)
            }.onFailure { e ->
                _uiState.update {
                    it.copy(
                        favorites = prev,
                        error = e.message ?: ErrorMessage.FAILED_REMOVE_FAVORITES.key
                    )
                }
            }
        }
    }
    
    fun addToCart(favorite: Favorite) {
        viewModelScope.launch {
            try {
                val product = convertFavoriteToProduct(favorite)
                addToCartUseCase(product, 1)

                _uiState.update { it.copy(animatedCartProductId = favorite.productId) }
                
                delay(2000)
                _uiState.update { it.copy(animatedCartProductId = null) }
                
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message ?: ErrorMessage.FAILED_ADD_CART.key) }
            }
        }
    }
} 