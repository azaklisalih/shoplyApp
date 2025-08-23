package com.example.cartapp.presentation.productdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cartapp.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import javax.inject.Inject
import com.example.cartapp.domain.usecase.home.GetProductByIdUseCase
import com.example.cartapp.domain.usecase.cart.AddToCartUseCase
import com.example.cartapp.domain.usecase.favorite.AddToFavoritesUseCase
import com.example.cartapp.domain.usecase.favorite.RemoveFromFavoritesUseCase
import com.example.cartapp.domain.usecase.favorite.IsFavoriteUseCase
import com.example.cartapp.domain.usecase.cart.GetCartItemsUseCase
import com.example.cartapp.presentation.ui_state.ProductDetailUIState

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val getProductById: GetProductByIdUseCase,
    private val addToCart: AddToCartUseCase,
    private val getCartItems: GetCartItemsUseCase,
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

                getProductById(productId)
                    .collect { product ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                product = product
                            )
                        }
                        checkFavoriteStatus(productId)
                        checkCartStatus(productId)
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
    
    private fun checkFavoriteStatus(productId: String) {
        viewModelScope.launch {
            isFavorite(productId).collect { isFavorite ->
                _uiState.update { it.copy(isFavorite = isFavorite) }
            }
        }
    }
    
    private fun checkCartStatus(productId: String) {
        viewModelScope.launch {
            getCartItems().collect { cartItems ->
                val isInCart = cartItems.any { it.productId == productId }
                _uiState.update { it.copy(isInCart = isInCart) }
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
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message ?: R.string.error_failed_add_cart.toString()) }
            }
        }
    }
    
    fun removeFromCart() {
        viewModelScope.launch {
            try {
                val product = _uiState.value.product
                if (product != null) {
                    _uiState.update { it.copy(isInCart = false) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message ?: R.string.error_failed_remove_cart.toString()) }
            }
        }
    }
    
    fun addToCartWithAnimation() {
        viewModelScope.launch {
            try {
                val product = _uiState.value.product

                if (product != null) {
                    addToCart(product, 1)
                    _uiState.update { it.copy(isInCart = true) }

                    _uiState.update { it.copy(showSuccessAnimation = true) }
                    
                    delay(2000)
                    _uiState.update { it.copy(showSuccessAnimation = false) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message ?: R.string.error_failed_add_cart.toString()) }
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
                    } else {
                        addToFavorites(product)
                    }

                    _uiState.update { it.copy(isFavorite = !currentState) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message ?: R.string.error_failed_toggle_favorite.toString()) }
            }
        }
    }
}
