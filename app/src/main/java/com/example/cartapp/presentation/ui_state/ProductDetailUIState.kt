package com.example.cartapp.presentation.ui_state

import com.example.cartapp.domain.model.Product

data class ProductDetailUIState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val product: Product? = null,
    val isFavorite: Boolean = false,
    val isInCart: Boolean = false,
    val showSuccessAnimation: Boolean = false
) 