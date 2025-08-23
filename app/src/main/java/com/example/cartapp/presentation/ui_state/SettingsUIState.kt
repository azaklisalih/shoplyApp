package com.example.cartapp.presentation.ui_state

data class SettingsUIState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val currentLanguage: String = "tr"
) 