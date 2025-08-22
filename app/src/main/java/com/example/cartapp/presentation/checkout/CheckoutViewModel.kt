package com.example.cartapp.presentation.checkout

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

data class CheckoutUIState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val totalAmount: Double = 0.0,
    val isOrderPlaced: Boolean = false,
    val orderNumber: String = ""
)

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(CheckoutUIState())
    val uiState: StateFlow<CheckoutUIState> = _uiState.asStateFlow()
    
    fun placeOrder(name: String, email: String, phone: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            try {
                // TODO: Implement actual order placement logic
                // For now, simulate order placement
                kotlinx.coroutines.delay(2000)
                
                val orderNumber = generateOrderNumber()
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        isOrderPlaced = true,
                        orderNumber = orderNumber
                    )
                }
                
                // Clear cart after successful order
                clearCart()
                
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to place order"
                    )
                }
            }
        }
    }
    
    private fun generateOrderNumber(): String {
        return "ORD-${System.currentTimeMillis()}"
    }
    
    private suspend fun clearCart() {
        // TODO: Implement clear cart functionality
        // For now, we'll remove items one by one
        repository.getCartItems().collect { cartItems ->
            cartItems.forEach { item ->
                repository.removeFromCart(item.productId)
            }
        }
    }
} 