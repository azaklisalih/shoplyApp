package com.example.cartapp.presentation.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cartapp.R
import com.example.cartapp.domain.model.CartItem
import com.example.cartapp.domain.repository.ProductRepository
import com.example.cartapp.domain.usecase.cart.GetCartItemsUseCase
import com.example.cartapp.domain.usecase.cart.RemoveFromCartUseCase
import com.example.cartapp.domain.usecase.cart.UpdateCartItemQuantityUseCase
import com.example.cartapp.domain.usecase.cart.ClearCartUseCase
import com.example.cartapp.presentation.ui_state.CartUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val getCartItems: GetCartItemsUseCase,
    private val updateCartItemQuantity: UpdateCartItemQuantityUseCase,
    private val removeFromCart: RemoveFromCartUseCase,
    private val clearCart: ClearCartUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(CartUIState())
    val uiState: StateFlow<CartUIState> = _uiState.asStateFlow()
    
    init {
        loadCartItems()
    }
    
    private fun loadCartItems() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            try {
                getCartItems().collect { cartItems ->
                    val totalPrice = calculateTotalPrice(cartItems)
                    _uiState.update { 
                        it.copy(
                            isLoading = false,
                            cartItems = cartItems,
                            totalPrice = totalPrice
                        )
                    }
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
    
    private fun calculateTotalPrice(cartItems: List<CartItem>): Double {
        return cartItems.sumOf { it.totalPrice }
    }
    
    fun updateQuantity(productId: String, newQuantity: Int) {
        viewModelScope.launch {
            try {
                updateCartItemQuantity(productId, newQuantity)
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message ?: R.string.error_failed_update_quantity.toString()) }
            }
        }
    }
    
    fun removeFromCart(productId: String) {
        viewModelScope.launch {
            try {
                removeFromCart(productId)
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message ?: R.string.error_failed_remove_cart.toString()) }
            }
        }
    }
    
    fun clearCart() {
        viewModelScope.launch {
            try {
                clearCart()
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message ?: R.string.error_failed_clear_cart.toString()) }
            }
        }
    }
} 