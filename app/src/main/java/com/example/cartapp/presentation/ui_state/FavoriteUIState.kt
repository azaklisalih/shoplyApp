package com.example.cartapp.presentation.ui_state

import com.example.cartapp.domain.model.Favorite

data class FavoriteUIState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val favorites: List<Favorite> = emptyList()
) 