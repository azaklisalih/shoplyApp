package com.example.cartapp.presentation.ui_state

data class ProfileUIState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val userName: String = "Kullanıcı",
    val userEmail: String = "kullanici@example.com",
    val avatarUrl: String? = null
) 