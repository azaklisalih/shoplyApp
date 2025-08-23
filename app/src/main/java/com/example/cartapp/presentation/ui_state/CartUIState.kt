package com.example.cartapp.presentation.ui_state

import com.example.cartapp.domain.model.CartItem

data class CartUIState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val cartItems: List<CartItem> = emptyList(),
    val totalPrice: Double = 0.0
) 