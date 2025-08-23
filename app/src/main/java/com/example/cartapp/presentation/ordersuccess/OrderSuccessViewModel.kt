package com.example.cartapp.presentation.ordersuccess

import androidx.lifecycle.ViewModel
import com.example.cartapp.presentation.ui_state.OrderSuccessUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class OrderSuccessViewModel @Inject constructor() : ViewModel() {
    
    private val _uiState = MutableStateFlow(OrderSuccessUIState())
    val uiState: StateFlow<OrderSuccessUIState> = _uiState.asStateFlow()
    
    fun setOrderNumber(orderNumber: String) {
        _uiState.value = _uiState.value.copy(orderNumber = orderNumber)
    }
} 