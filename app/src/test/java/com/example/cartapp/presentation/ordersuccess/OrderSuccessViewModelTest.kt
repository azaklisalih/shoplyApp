package com.example.cartapp.presentation.ordersuccess

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class OrderSuccessViewModelTest {

    private lateinit var viewModel: OrderSuccessViewModel

    @Before
    fun setup() {
        viewModel = OrderSuccessViewModel()
    }

    @Test
    fun `setOrderNumber updates uiState with correct order number`() = runTest {
        // Given
        val orderNumber = "ORD-123456789"

        // When
        viewModel.setOrderNumber(orderNumber)

        // Then
        assertEquals(orderNumber, viewModel.uiState.value.orderNumber)
    }

    @Test
    fun `initial uiState has empty order number`() = runTest {
        // Then
        assertEquals("", viewModel.uiState.value.orderNumber)
    }

    @Test
    fun `setOrderNumber multiple times updates to latest value`() = runTest {
        // Given
        val firstOrderNumber = "ORD-111111111"
        val secondOrderNumber = "ORD-222222222"

        // When
        viewModel.setOrderNumber(firstOrderNumber)
        viewModel.setOrderNumber(secondOrderNumber)

        // Then
        assertEquals(secondOrderNumber, viewModel.uiState.value.orderNumber)
    }
} 