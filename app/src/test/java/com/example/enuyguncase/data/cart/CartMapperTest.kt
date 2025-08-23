package com.example.cartapp.data.cart

import com.example.cartapp.data.home.local.entities.CartItemEntity
import com.example.cartapp.domain.model.CartItem
import org.junit.Assert.assertEquals
import org.junit.Test

class CartMapperTest {

    @Test
    fun `CartItemEntity toDomainList should map correctly`() {
        // Given
        val cartItemEntity = CartItemEntity(
            productId = 1,
            title = "Test Product",
            price = 99.99,
            discountPrice = 89.99,
            thumbnail = "https://example.com/thumbnail.jpg",
            quantity = 2
        )

        // When
        val result = cartItemEntity.toDomainList()

        // Then
        assertEquals(1, result.productId)
        assertEquals("Test Product", result.title)
        assertEquals(99.99, result.price, 0.01)
        assertEquals(89.99, result.discountPrice, 0.01)
        assertEquals("https://example.com/thumbnail.jpg", result.thumbnail)
        assertEquals(2, result.quantity)
    }

    @Test
    fun `CartItemEntity toDomainList should handle zero quantity`() {
        // Given
        val cartItemEntity = CartItemEntity(
            productId = 1,
            title = "Test Product",
            price = 99.99,
            discountPrice = 89.99,
            thumbnail = "https://example.com/thumbnail.jpg",
            quantity = 0
        )

        // When
        val result = cartItemEntity.toDomainList()

        // Then
        assertEquals(0, result.quantity)
    }

    @Test
    fun `CartItemEntity toDomainList should handle large quantity`() {
        // Given
        val cartItemEntity = CartItemEntity(
            productId = 1,
            title = "Test Product",
            price = 99.99,
            discountPrice = 89.99,
            thumbnail = "https://example.com/thumbnail.jpg",
            quantity = 999
        )

        // When
        val result = cartItemEntity.toDomainList()

        // Then
        assertEquals(999, result.quantity)
    }

    @Test
    fun `CartItemEntity toDomainList should handle zero price`() {
        // Given
        val cartItemEntity = CartItemEntity(
            productId = 1,
            title = "Free Product",
            price = 0.0,
            discountPrice = 0.0,
            thumbnail = "https://example.com/thumbnail.jpg",
            quantity = 1
        )

        // When
        val result = cartItemEntity.toDomainList()

        // Then
        assertEquals(0.0, result.price, 0.01)
        assertEquals(0.0, result.discountPrice, 0.01)
    }

    @Test
    fun `CartItemEntity toDomainList should handle empty thumbnail`() {
        // Given
        val cartItemEntity = CartItemEntity(
            productId = 1,
            title = "Test Product",
            price = 99.99,
            discountPrice = 89.99,
            thumbnail = "",
            quantity = 1
        )

        // When
        val result = cartItemEntity.toDomainList()

        // Then
        assertEquals("", result.thumbnail)
    }

    @Test
    fun `CartItemEntity toDomainList should handle special characters in title`() {
        // Given
        val cartItemEntity = CartItemEntity(
            productId = 1,
            title = "Test Product with Special Chars: !@#$%^&*()",
            price = 99.99,
            discountPrice = 89.99,
            thumbnail = "https://example.com/thumbnail.jpg",
            quantity = 1
        )

        // When
        val result = cartItemEntity.toDomainList()

        // Then
        assertEquals("Test Product with Special Chars: !@#$%^&*()", result.title)
    }

    @Test
    fun `CartItemEntity toDomainList should handle large product ID`() {
        // Given
        val cartItemEntity = CartItemEntity(
            productId = 999999,
            title = "Test Product",
            price = 99.99,
            discountPrice = 89.99,
            thumbnail = "https://example.com/thumbnail.jpg",
            quantity = 1
        )

        // When
        val result = cartItemEntity.toDomainList()

        // Then
        assertEquals(999999, result.productId)
    }

    @Test
    fun `CartItemEntity toDomainList should handle high precision prices`() {
        // Given
        val cartItemEntity = CartItemEntity(
            productId = 1,
            title = "Test Product",
            price = 99.999999,
            discountPrice = 89.999999,
            thumbnail = "https://example.com/thumbnail.jpg",
            quantity = 1
        )

        // When
        val result = cartItemEntity.toDomainList()

        // Then
        assertEquals(99.999999, result.price, 0.000001)
        assertEquals(89.999999, result.discountPrice, 0.000001)
    }
} 