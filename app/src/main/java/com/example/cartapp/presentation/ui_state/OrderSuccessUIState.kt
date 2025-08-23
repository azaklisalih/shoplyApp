package com.example.cartapp.presentation.ui_state

data class OrderSuccessUIState(
    val orderNumber: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
) 