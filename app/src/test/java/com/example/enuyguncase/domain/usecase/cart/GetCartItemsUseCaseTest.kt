package com.example.cartapp.domain.usecase.cart

import com.example.cartapp.data.home.local.entities.CartItemEntity
import com.example.cartapp.domain.model.CartItem
import com.example.cartapp.domain.repository.CartRepository
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class GetCartItemsUseCaseTest {

    @Mock
    private lateinit var mockCartRepository: CartRepository

    private lateinit var getCartItemsUseCase: GetCartItemsUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        getCartItemsUseCase = GetCartItemsUseCase(mockCartRepository)
    }

    @Test
    fun `invoke should return list of cart items`() = runTest {
        // Given
        val mockCartEntities = listOf(
            CartItemEntity(
                productId = 1,
                title = "Test Product 1",
                price = 99.99,
                discountPrice = 89.99,
                thumbnail = "https://example.com/thumbnail1.jpg",
                quantity = 2
            ),
            CartItemEntity(
                productId = 2,
                title = "Test Product 2",
                price = 149.99,
                discountPrice = 134.99,
                thumbnail = "https://example.com/thumbnail2.jpg",
                quantity = 1
            )
        )

        whenever(mockCartRepository.getCartItems()).thenReturn(flowOf(mockCartEntities))

        // When
        val result = getCartItemsUseCase()

        // Then
        result.collect { cartItems ->
            assertEquals(2, cartItems.size)
            assertEquals(1, cartItems[0].productId)
            assertEquals("Test Product 1", cartItems[0].title)
            assertEquals(99.99, cartItems[0].price, 0.01)
            assertEquals(89.99, cartItems[0].discountPrice, 0.01)
            assertEquals("https://example.com/thumbnail1.jpg", cartItems[0].thumbnail)
            assertEquals(2, cartItems[0].quantity)

            assertEquals(2, cartItems[1].productId)
            assertEquals("Test Product 2", cartItems[1].title)
            assertEquals(149.99, cartItems[1].price, 0.01)
            assertEquals(134.99, cartItems[1].discountPrice, 0.01)
            assertEquals("https://example.com/thumbnail2.jpg", cartItems[1].thumbnail)
            assertEquals(1, cartItems[1].quantity)
        }

        verify(mockCartRepository).getCartItems()
    }

    @Test
    fun `invoke should handle empty cart`() = runTest {
        // Given
        val mockCartEntities = emptyList<CartItemEntity>()

        whenever(mockCartRepository.getCartItems()).thenReturn(flowOf(mockCartEntities))

        // When
        val result = getCartItemsUseCase()

        // Then
        result.collect { cartItems ->
            assertEquals(0, cartItems.size)
        }

        verify(mockCartRepository).getCartItems()
    }

    @Test
    fun `invoke should handle single cart item`() = runTest {
        // Given
        val mockCartEntities = listOf(
            CartItemEntity(
                productId = 1,
                title = "Single Product",
                price = 99.99,
                discountPrice = 89.99,
                thumbnail = "https://example.com/thumbnail.jpg",
                quantity = 1
            )
        )

        whenever(mockCartRepository.getCartItems()).thenReturn(flowOf(mockCartEntities))

        // When
        val result = getCartItemsUseCase()

        // Then
        result.collect { cartItems ->
            assertEquals(1, cartItems.size)
            assertEquals(1, cartItems[0].productId)
            assertEquals("Single Product", cartItems[0].title)
            assertEquals(99.99, cartItems[0].price, 0.01)
            assertEquals(89.99, cartItems[0].discountPrice, 0.01)
            assertEquals("https://example.com/thumbnail.jpg", cartItems[0].thumbnail)
            assertEquals(1, cartItems[0].quantity)
        }

        verify(mockCartRepository).getCartItems()
    }
} 