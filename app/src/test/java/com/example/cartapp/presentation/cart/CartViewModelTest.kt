package com.example.cartapp.presentation.cart

import com.example.cartapp.domain.model.CartItem
import com.example.cartapp.domain.usecase.cart.ClearCartUseCase
import com.example.cartapp.domain.usecase.cart.GetCartItemsUseCase
import com.example.cartapp.domain.usecase.cart.RemoveFromCartUseCase
import com.example.cartapp.domain.usecase.cart.UpdateCartItemQuantityUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
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
class CartViewModelTest {

    @Mock
    private lateinit var mockGetCartItems: GetCartItemsUseCase

    @Mock
    private lateinit var mockUpdateCartItemQuantity: UpdateCartItemQuantityUseCase

    @Mock
    private lateinit var mockRemoveFromCart: RemoveFromCartUseCase

    @Mock
    private lateinit var mockClearCart: ClearCartUseCase

    private lateinit var viewModel: CartViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        
        // Default mock return for init
        whenever(mockGetCartItems()).thenReturn(flowOf(emptyList()))
        
        viewModel = CartViewModel(
            mockGetCartItems,
            mockUpdateCartItemQuantity,
            mockRemoveFromCart,
            mockClearCart
        )
    }

    @Test
    fun `initial state should be loading`() = runTest {
        // Given - ViewModel is initialized
        
        // When - Check initial state
        val initialState = viewModel.uiState.value
        
        // Then
        assertTrue(initialState.cartItems.isEmpty())
        assertEquals(0.0, initialState.totalPrice, 0.0)
    }

    @Test
    fun `loadCartItems should update state with cart items`() = runTest {
        // Given
        val mockCartItems = listOf(
            CartItem(
                productId = "1",
                name = "Test Product 1",
                price = "99.99",
                image = "test1.jpg",
                description = "Test Description 1",
                model = "Test Model 1",
                brand = "Test Brand 1",
                quantity = 2
            )
        )
        whenever(mockGetCartItems()).thenReturn(flowOf(mockCartItems))

        // When
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(mockCartItems, state.cartItems)
        assertEquals(199.98, state.totalPrice, 0.01) // 99.99 * 2
    }

    @Test
    fun `updateQuantity should call use case with correct parameters`() = runTest {
        // Given
        val productId = "1"
        val newQuantity = 3

        // When
        viewModel.updateQuantity(productId, newQuantity)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        verify(mockUpdateCartItemQuantity).invoke(productId, newQuantity)
    }

    @Test
    fun `removeFromCart should call use case with correct product id`() = runTest {
        // Given
        val productId = "1"

        // When
        viewModel.removeFromCart(productId)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        verify(mockRemoveFromCart).invoke(productId)
    }

    @Test
    fun `clearCart should call clear cart use case`() = runTest {
        // When
        viewModel.clearCart()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        verify(mockClearCart).invoke()
    }

    @Test
    fun `calculateTotalPrice should sum all cart item prices`() = runTest {
        // Given
        val cartItems = listOf(
            CartItem(
                productId = "1",
                name = "Test Product 1",
                price = "50.00",
                image = "test1.jpg",
                description = "Test Description 1",
                model = "Test Model 1",
                brand = "Test Brand 1",
                quantity = 2
            ),
            CartItem(
                productId = "2",
                name = "Test Product 2",
                price = "25.00",
                image = "test2.jpg",
                description = "Test Description 2",
                model = "Test Model 2",
                brand = "Test Brand 2",
                quantity = 1
            )
        )
        whenever(mockGetCartItems()).thenReturn(flowOf(cartItems))

        // When
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertEquals(125.0, state.totalPrice, 0.01) // (50*2) + (25*1)
    }
} 