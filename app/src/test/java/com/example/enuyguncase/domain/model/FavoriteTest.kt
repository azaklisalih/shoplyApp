package com.example.cartapp.domain.model

import org.junit.Assert.*
import org.junit.Test

class FavoriteTest {

    @Test
    fun `Favorite should be created with correct properties`() {
        // Given
        val productId = 123
        val title = "Test Product"
        val images = listOf("https://example.com/image1.jpg", "https://example.com/image2.jpg")
        val description = "Test Description"
        val price = 99.99
        val discountedPrice = 89.99

        // When
        val favorite = Favorite(
            productId = productId,
            title = title,
            images = images,
            description = description,
            price = price,
            discountedPrice = discountedPrice
        )

        // Then
        assertEquals(productId, favorite.productId)
        assertEquals(title, favorite.title)
        assertEquals(images, favorite.images)
        assertEquals(description, favorite.description)
        assertEquals(price, favorite.price, 0.01)
        assertEquals(discountedPrice, favorite.discountedPrice, 0.01)
    }

    @Test
    fun `Favorite should return first image as thumbnail when images exist`() {
        // Given
        val images = listOf("https://example.com/image1.jpg", "https://example.com/image2.jpg")
        val favorite = Favorite(
            productId = 123,
            title = "Test Product",
            images = images,
            description = "Test Description",
            price = 99.99,
            discountedPrice = 89.99
        )

        // When
        val thumbnail = favorite.thumbnailUrl

        // Then
        assertEquals("https://example.com/image1.jpg", thumbnail)
    }

    @Test
    fun `Favorite should return empty string as thumbnail when no images exist`() {
        // Given
        val favorite = Favorite(
            productId = 123,
            title = "Test Product",
            images = emptyList(),
            description = "Test Description",
            price = 99.99,
            discountedPrice = 89.99
        )

        // When
        val thumbnail = favorite.thumbnailUrl

        // Then
        assertEquals("", thumbnail)
    }

    @Test
    fun `Favorite should handle zero price correctly`() {
        // Given
        val favorite = Favorite(
            productId = 123,
            title = "Free Product",
            images = emptyList(),
            description = "Test Description",
            price = 0.0,
            discountedPrice = 0.0
        )

        // When & Then
        assertEquals(0.0, favorite.price, 0.01)
        assertEquals(0.0, favorite.discountedPrice, 0.01)
    }

    @Test
    fun `Favorite should handle large prices correctly`() {
        // Given
        val price = 999999.99
        val discountedPrice = 899999.99
        val favorite = Favorite(
            productId = 123,
            title = "Expensive Product",
            images = emptyList(),
            description = "Test Description",
            price = price,
            discountedPrice = discountedPrice
        )

        // When & Then
        assertEquals(price, favorite.price, 0.01)
        assertEquals(discountedPrice, favorite.discountedPrice, 0.01)
    }

    @Test
    fun `Favorite should handle special characters in strings`() {
        // Given
        val title = "Test Product with special chars: !@#$%^&*()"
        val description = "Test Description with special chars: !@#$%^&*()"
        val favorite = Favorite(
            productId = 123,
            title = title,
            images = emptyList(),
            description = description,
            price = 99.99,
            discountedPrice = 89.99
        )

        // When & Then
        assertEquals(title, favorite.title)
        assertEquals(description, favorite.description)
    }
} 