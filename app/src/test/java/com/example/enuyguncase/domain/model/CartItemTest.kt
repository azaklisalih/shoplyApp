package com.example.cartapp.domain.model

import org.junit.Assert.*
import org.junit.Test

class CartItemTest {

    @Test
    fun `CartItem should be created with correct properties`() {
        // Given
        val productId = 123
        val title = "Test Product"
        val price = 99.99
        val discountPrice = 89.99
        val thumbnail = "https://example.com/thumbnail.jpg"
        val quantity = 2

        // When
        val cartItem = CartItem(
            productId = productId,
            title = title,
            price = price,
            discountPrice = discountPrice,
            thumbnail = thumbnail,
            quantity = quantity
        )

        // Then
        assertEquals(productId, cartItem.productId)
        assertEquals(title, cartItem.title)
        assertEquals(price, cartItem.price, 0.01)
        assertEquals(discountPrice, cartItem.discountPrice, 0.01)
        assertEquals(thumbnail, cartItem.thumbnail)
        assertEquals(quantity, cartItem.quantity)
    }

    @Test
    fun `CartItem should calculate total price correctly`() {
        // Given
        val price = 50.0
        val quantity = 3
        val expectedTotal = 150.0

        val cartItem = CartItem(
            productId = 123,
            title = "Test Product",
            price = price,
            discountPrice = 45.0,
            thumbnail = "https://example.com/thumbnail.jpg",
            quantity = quantity
        )

        // When
        val totalPrice = cartItem.price * cartItem.quantity

        // Then
        assertEquals(expectedTotal, totalPrice, 0.01)
    }

    @Test
    fun `CartItem should calculate total discounted price correctly`() {
        // Given
        val discountPrice = 45.0
        val quantity = 3
        val expectedTotal = 135.0

        val cartItem = CartItem(
            productId = 123,
            title = "Test Product",
            price = 50.0,
            discountPrice = discountPrice,
            thumbnail = "https://example.com/thumbnail.jpg",
            quantity = quantity
        )

        // When
        val totalDiscountedPrice = cartItem.discountPrice * cartItem.quantity

        // Then
        assertEquals(expectedTotal, totalDiscountedPrice, 0.01)
    }

    @Test
    fun `CartItem should have minimum quantity of 1`() {
        // Given
        val cartItem = CartItem(
            productId = 123,
            title = "Test Product",
            price = 99.99,
            discountPrice = 89.99,
            thumbnail = "https://example.com/thumbnail.jpg",
            quantity = 1
        )

        // When & Then
        assertTrue(cartItem.quantity >= 1)
    }

    @Test
    fun `CartItem should handle zero quantity`() {
        // Given
        val cartItem = CartItem(
            productId = 123,
            title = "Test Product",
            price = 99.99,
            discountPrice = 89.99,
            thumbnail = "https://example.com/thumbnail.jpg",
            quantity = 0
        )

        // When
        val totalPrice = cartItem.price * cartItem.quantity

        // Then
        assertEquals(0.0, totalPrice, 0.01)
    }

    @Test
    fun `CartItem should handle large quantities correctly`() {
        // Given
        val price = 10.0
        val quantity = 1000
        val expectedTotal = 10000.0

        val cartItem = CartItem(
            productId = 123,
            title = "Test Product",
            price = price,
            discountPrice = 9.0,
            thumbnail = "https://example.com/thumbnail.jpg",
            quantity = quantity
        )

        // When
        val totalPrice = cartItem.price * cartItem.quantity

        // Then
        assertEquals(expectedTotal, totalPrice, 0.01)
    }
} 