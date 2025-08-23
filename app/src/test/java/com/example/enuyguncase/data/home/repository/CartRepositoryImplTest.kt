package com.example.cartapp.data.home.repository

import com.example.cartapp.data.home.local.dao.CartDao
import com.example.cartapp.data.home.local.entities.CartItemEntity
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class CartRepositoryImplTest {

    @Mock
    private lateinit var mockDao: CartDao

    private lateinit var cartRepository: CartRepositoryImpl

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        cartRepository = CartRepositoryImpl(mockDao)
    }

    @Test
    fun `getCartItems should return flow of cart items`() = runTest {
        // Given
        val mockCartItems = listOf(
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

        whenever(mockDao.getAllCartItems()).thenReturn(flowOf(mockCartItems))

        // When
        val result = cartRepository.getCartItems()

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

        verify(mockDao).getAllCartItems()
    }

    @Test
    fun `getCartItems should return empty list when no items exist`() = runTest {
        // Given
        whenever(mockDao.getAllCartItems()).thenReturn(flowOf(emptyList()))

        // When
        val result = cartRepository.getCartItems()

        // Then
        result.collect { cartItems ->
            assertEquals(0, cartItems.size)
        }

        verify(mockDao).getAllCartItems()
    }

    @Test
    fun `addOrUpdate should call dao upsert method`() = runTest {
        // Given
        val cartItem = CartItemEntity(
            productId = 1,
            title = "Test Product",
            price = 99.99,
            discountPrice = 89.99,
            thumbnail = "https://example.com/thumbnail.jpg",
            quantity = 1
        )

        // When
        cartRepository.addOrUpdate(cartItem)

        // Then
        verify(mockDao).upsert(cartItem)
    }

    @Test
    fun `remove should call dao deleteByProductId method`() = runTest {
        // Given
        val productId = 1

        // When
        cartRepository.remove(productId)

        // Then
        verify(mockDao).deleteByProductId(productId)
    }

    @Test
    fun `increase should call dao increaseQuantity method`() = runTest {
        // Given
        val productId = 1

        // When
        cartRepository.increase(productId)

        // Then
        verify(mockDao).increaseQuantity(productId)
    }

    @Test
    fun `decrease should call dao decreaseQuantity method`() = runTest {
        // Given
        val productId = 1

        // When
        cartRepository.decrease(productId)

        // Then
        verify(mockDao).decreaseQuantity(productId)
    }

    @Test
    fun `getByProductId should return cart item when exists`() = runTest {
        // Given
        val mockCartItem = CartItemEntity(
            productId = 1,
            title = "Test Product",
            price = 99.99,
            discountPrice = 89.99,
            thumbnail = "https://example.com/thumbnail.jpg",
            quantity = 2
        )

        whenever(mockDao.getItemByProductId(1)).thenReturn(mockCartItem)

        // When
        val result = cartRepository.getByProductId(1)

        // Then
        assertEquals(mockCartItem, result)
        verify(mockDao).getItemByProductId(1)
    }

    @Test
    fun `getByProductId should return null when item does not exist`() = runTest {
        // Given
        whenever(mockDao.getItemByProductId(999)).thenReturn(null)

        // When
        val result = cartRepository.getByProductId(999)

        // Then
        assertEquals(null, result)
        verify(mockDao).getItemByProductId(999)
    }

    @Test
    fun `getCartItems should handle single cart item`() = runTest {
        // Given
        val mockCartItem = listOf(
            CartItemEntity(
                productId = 1,
                title = "Single Product",
                price = 99.99,
                discountPrice = 89.99,
                thumbnail = "https://example.com/thumbnail.jpg",
                quantity = 1
            )
        )

        whenever(mockDao.getAllCartItems()).thenReturn(flowOf(mockCartItem))

        // When
        val result = cartRepository.getCartItems()

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

        verify(mockDao).getAllCartItems()
    }
    
    @Test
    fun `clearCart should call dao clearCart method`() = runTest {
        // When
        cartRepository.clearCart()
        
        // Then
        verify(mockDao).clearCart()
    }
} 