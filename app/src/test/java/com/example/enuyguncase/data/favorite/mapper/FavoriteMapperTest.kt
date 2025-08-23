package com.example.cartapp.data.favorite.mapper

import com.example.cartapp.data.home.local.entities.FavoriteEntity
import com.example.cartapp.domain.model.Favorite
import org.junit.Assert.assertEquals
import org.junit.Test

class FavoriteMapperTest {

    @Test
    fun `FavoriteEntity toDomainModel should map correctly`() {
        // Given
        val favoriteEntity = FavoriteEntity(
            productId = 1,
            title = "Test Product",
            images = listOf("https://example.com/image1.jpg", "https://example.com/image2.jpg"),
            description = "Test Description",
            price = 99.99,
            discountedPrice = 89.99
        )

        // When
        val result = favoriteEntity.toDomainModel()

        // Then
        assertEquals(1, result.productId)
        assertEquals("Test Product", result.title)
        assertEquals(listOf("https://example.com/image1.jpg", "https://example.com/image2.jpg"), result.images)
        assertEquals("Test Description", result.description)
        assertEquals(99.99, result.price, 0.01)
        assertEquals(89.99, result.discountedPrice, 0.01)
    }

    @Test
    fun `FavoriteEntity toDomainModel should handle empty images list`() {
        // Given
        val favoriteEntity = FavoriteEntity(
            productId = 1,
            title = "Test Product",
            images = emptyList(),
            description = "Test Description",
            price = 99.99,
            discountedPrice = 89.99
        )

        // When
        val result = favoriteEntity.toDomainModel()

        // Then
        assertEquals(emptyList<String>(), result.images)
        assertEquals("", result.thumbnailUrl)
    }

    @Test
    fun `FavoriteEntity toDomainModel should handle single image`() {
        // Given
        val favoriteEntity = FavoriteEntity(
            productId = 1,
            title = "Test Product",
            images = listOf("https://example.com/image1.jpg"),
            description = "Test Description",
            price = 99.99,
            discountedPrice = 89.99
        )

        // When
        val result = favoriteEntity.toDomainModel()

        // Then
        assertEquals(listOf("https://example.com/image1.jpg"), result.images)
        assertEquals("https://example.com/image1.jpg", result.thumbnailUrl)
    }

    @Test
    fun `FavoriteEntity toDomainModel should handle multiple images`() {
        // Given
        val favoriteEntity = FavoriteEntity(
            productId = 1,
            title = "Test Product",
            images = listOf("image1.jpg", "image2.jpg", "image3.jpg"),
            description = "Test Description",
            price = 99.99,
            discountedPrice = 89.99
        )

        // When
        val result = favoriteEntity.toDomainModel()

        // Then
        assertEquals(listOf("image1.jpg", "image2.jpg", "image3.jpg"), result.images)
        assertEquals("image1.jpg", result.thumbnailUrl)
    }

    @Test
    fun `FavoriteEntity toDomainModel should handle zero price`() {
        // Given
        val favoriteEntity = FavoriteEntity(
            productId = 1,
            title = "Free Product",
            images = listOf("https://example.com/image1.jpg"),
            description = "Free product description",
            price = 0.0,
            discountedPrice = 0.0
        )

        // When
        val result = favoriteEntity.toDomainModel()

        // Then
        assertEquals(0.0, result.price, 0.01)
        assertEquals(0.0, result.discountedPrice, 0.01)
    }

    @Test
    fun `FavoriteEntity toDomainModel should handle large prices`() {
        // Given
        val favoriteEntity = FavoriteEntity(
            productId = 1,
            title = "Expensive Product",
            images = listOf("https://example.com/image1.jpg"),
            description = "Expensive product description",
            price = 999999.99,
            discountedPrice = 899999.99
        )

        // When
        val result = favoriteEntity.toDomainModel()

        // Then
        assertEquals(999999.99, result.price, 0.01)
        assertEquals(899999.99, result.discountedPrice, 0.01)
    }

    @Test
    fun `FavoriteEntity toDomainModel should handle empty description`() {
        // Given
        val favoriteEntity = FavoriteEntity(
            productId = 1,
            title = "Test Product",
            images = listOf("https://example.com/image1.jpg"),
            description = "",
            price = 99.99,
            discountedPrice = 89.99
        )

        // When
        val result = favoriteEntity.toDomainModel()

        // Then
        assertEquals("", result.description)
    }

    @Test
    fun `FavoriteEntity toDomainModel should handle special characters in title`() {
        // Given
        val favoriteEntity = FavoriteEntity(
            productId = 1,
            title = "Test Product with Special Chars: !@#$%^&*()",
            images = listOf("https://example.com/image1.jpg"),
            description = "Test Description",
            price = 99.99,
            discountedPrice = 89.99
        )

        // When
        val result = favoriteEntity.toDomainModel()

        // Then
        assertEquals("Test Product with Special Chars: !@#$%^&*()", result.title)
    }

    @Test
    fun `FavoriteEntity toDomainModel should handle large product ID`() {
        // Given
        val favoriteEntity = FavoriteEntity(
            productId = 999999,
            title = "Test Product",
            images = listOf("https://example.com/image1.jpg"),
            description = "Test Description",
            price = 99.99,
            discountedPrice = 89.99
        )

        // When
        val result = favoriteEntity.toDomainModel()

        // Then
        assertEquals(999999, result.productId)
    }

    @Test
    fun `FavoriteEntity toDomainModel should handle high precision prices`() {
        // Given
        val favoriteEntity = FavoriteEntity(
            productId = 1,
            title = "Test Product",
            images = listOf("https://example.com/image1.jpg"),
            description = "Test Description",
            price = 99.999999,
            discountedPrice = 89.999999
        )

        // When
        val result = favoriteEntity.toDomainModel()

        // Then
        assertEquals(99.999999, result.price, 0.000001)
        assertEquals(89.999999, result.discountedPrice, 0.000001)
    }

    @Test
    fun `FavoriteEntity toDomainModel should handle long description`() {
        // Given
        val longDescription = "This is a very long description that contains many words and should be handled properly by the mapper. It includes various details about the product and its features."
        val favoriteEntity = FavoriteEntity(
            productId = 1,
            title = "Test Product",
            images = listOf("https://example.com/image1.jpg"),
            description = longDescription,
            price = 99.99,
            discountedPrice = 89.99
        )

        // When
        val result = favoriteEntity.toDomainModel()

        // Then
        assertEquals(longDescription, result.description)
    }
} 