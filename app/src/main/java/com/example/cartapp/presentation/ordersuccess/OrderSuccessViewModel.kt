package com.example.cartapp.presentation.ordersuccess

import androidx.lifecycle.ViewModel
import com.example.cartapp.presentation.ui_state.OrderSuccessUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OrderSuccessViewModel @Inject constructor() : ViewModel() {
    
    // This ViewModel is simple since it just displays order success
    // The order number comes from navigation args
} 