package com.example.cartapp.presentation.ordersuccess

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class OrderSuccessUIState(
    val orderNumber: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class OrderSuccessViewModel @Inject constructor() : ViewModel() {
    
    // This ViewModel is simple since it just displays order success
    // The order number comes from navigation args
} 