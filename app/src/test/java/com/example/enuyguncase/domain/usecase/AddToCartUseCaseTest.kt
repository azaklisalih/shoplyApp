package com.example.cartapp.domain.usecase

import com.example.cartapp.data.home.local.entities.CartItemEntity
import com.example.cartapp.domain.model.CartItem
import com.example.cartapp.domain.repository.CartRepository
import com.example.cartapp.domain.usecase.cart.AddToCartUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class AddToCartUseCaseTest {

    @Mock
    private lateinit var mockCartRepository: CartRepository

    private lateinit var addToCartUseCase: AddToCartUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        addToCartUseCase = AddToCartUseCase(mockCartRepository)
    }

    @Test
    fun `invoke should add new cart item when product not exists`() = runTest {
        // Given
        val cartItem = CartItem(
            productId = 123,
            title = "Test Product",
            price = 99.99,
            discountPrice = 89.99,
            thumbnail = "https://example.com/thumbnail.jpg",
            quantity = 1
        )
        
        whenever(mockCartRepository.getByProductId(123)).thenReturn(null)

        // When
        addToCartUseCase(cartItem)

        // Then
        verify(mockCartRepository).addOrUpdate(
            CartItemEntity(
                productId = 123,
                title = "Test Product",
                price = 99.99,
                discountPrice = 89.99,
                thumbnail = "https://example.com/thumbnail.jpg",
                quantity = 1
            )
        )
    }

    @Test
    fun `invoke should update existing cart item quantity when product exists`() = runTest {
        // Given
        val cartItem = CartItem(
            productId = 123,
            title = "Test Product",
            price = 99.99,
            discountPrice = 89.99,
            thumbnail = "https://example.com/thumbnail.jpg",
            quantity = 1
        )
        
        val existingCartItem = CartItemEntity(
            productId = 123,
            title = "Test Product",
            price = 99.99,
            discountPrice = 89.99,
            thumbnail = "https://example.com/thumbnail.jpg",
            quantity = 2
        )
        
        whenever(mockCartRepository.getByProductId(123)).thenReturn(existingCartItem)

        // When
        addToCartUseCase(cartItem)

        // Then
        verify(mockCartRepository).addOrUpdate(
            CartItemEntity(
                productId = 123,
                title = "Test Product",
                price = 99.99,
                discountPrice = 89.99,
                thumbnail = "https://example.com/thumbnail.jpg",
                quantity = 3
            )
        )
    }

    @Test
    fun `invoke should handle zero quantity existing item`() = runTest {
        // Given
        val cartItem = CartItem(
            productId = 123,
            title = "Test Product",
            price = 99.99,
            discountPrice = 89.99,
            thumbnail = "https://example.com/thumbnail.jpg",
            quantity = 1
        )
        
        val existingCartItem = CartItemEntity(
            productId = 123,
            title = "Test Product",
            price = 99.99,
            discountPrice = 89.99,
            thumbnail = "https://example.com/thumbnail.jpg",
            quantity = 0
        )
        
        whenever(mockCartRepository.getByProductId(123)).thenReturn(existingCartItem)

        // When
        addToCartUseCase(cartItem)

        // Then
        verify(mockCartRepository).addOrUpdate(
            CartItemEntity(
                productId = 123,
                title = "Test Product",
                price = 99.99,
                discountPrice = 89.99,
                thumbnail = "https://example.com/thumbnail.jpg",
                quantity = 1
            )
        )
    }
} 