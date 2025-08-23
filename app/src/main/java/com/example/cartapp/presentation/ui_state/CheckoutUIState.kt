package com.example.cartapp.presentation.ui_state

data class CheckoutUIState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val totalAmount: Double = 0.0,
    val isOrderPlaced: Boolean = false,
    val orderNumber: String = ""
) 