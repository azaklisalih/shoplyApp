package com.example.cartapp.presentation.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cartapp.R
import com.example.cartapp.domain.repository.ProductRepository
import com.example.cartapp.presentation.ui_state.CheckoutUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(CheckoutUIState())
    val uiState: StateFlow<CheckoutUIState> = _uiState.asStateFlow()
    
    init {
        loadCartTotal()
    }
    
    private fun loadCartTotal() {
        viewModelScope.launch {
            repository.getCartItems().collect { cartItems ->
                val totalPrice = calculateTotalPrice(cartItems)
                _uiState.update { it.copy(cartTotal = totalPrice) }
            }
        }
    }
    
    private fun calculateTotalPrice(cartItems: List<com.example.cartapp.domain.model.CartItem>): Double {
        return cartItems.sumOf { it.totalPrice }
    }
    
    fun placeOrder(name: String, email: String, phone: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            try {
                kotlinx.coroutines.delay(2000)
                
                val orderNumber = generateOrderNumber()
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        isOrderPlaced = true,
                        orderNumber = orderNumber
                    )
                }
                
                clearCart()
                
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        error = e.message ?: R.string.error_failed_place_order.toString()
                    )
                }
            }
        }
    }
    
    fun resetOrderState() {
        _uiState.update { it.copy(isOrderPlaced = false, orderNumber = "") }
    }
    
    private fun generateOrderNumber(): String {
        return "ORD-${System.currentTimeMillis()}"
    }
    
    private suspend fun clearCart() {
        repository.getCartItems().collect { cartItems ->
            cartItems.forEach { item ->
                repository.removeFromCart(item.productId)
            }
        }
    }
} 