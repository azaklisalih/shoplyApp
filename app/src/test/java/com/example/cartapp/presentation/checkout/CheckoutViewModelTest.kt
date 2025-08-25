package com.example.cartapp.presentation.checkout

import com.example.cartapp.domain.repository.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class CheckoutViewModelTest {

    @Mock
    private lateinit var mockProductRepository: ProductRepository

    private lateinit var viewModel: CheckoutViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        
        viewModel = CheckoutViewModel(mockProductRepository)
    }

    @Test
    fun `initial state should have default values`() = runTest {
        // Given - ViewModel is initialized
        
        // When - Check initial state
        val initialState = viewModel.uiState.value
        
        // Then
        assertFalse(initialState.isLoading)
        assertFalse(initialState.isOrderPlaced)
        assertEquals(0.0, initialState.cartTotal, 0.0)
        assertEquals("", initialState.orderNumber)
    }

    @Test
    fun `placeOrder should update state to loading`() = runTest {
        // Given
        val name = "Test User"
        val email = "test@example.com"
        val phone = "1234567890"
        
        // When
        viewModel.placeOrder(name, email, phone)
        
        // Then
        val state = viewModel.uiState.value
        assertTrue(state.isLoading)
    }

    @Test
    fun `resetOrderState should reset order state`() = runTest {
        // Given - Initial state
        
        // When
        viewModel.resetOrderState()
        
        // Then
        val state = viewModel.uiState.value
        assertFalse(state.isOrderPlaced)
        assertEquals("", state.orderNumber)
    }
} 