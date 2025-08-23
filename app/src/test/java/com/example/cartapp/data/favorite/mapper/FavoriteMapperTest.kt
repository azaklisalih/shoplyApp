package com.example.cartapp.data.favorite.mapper

import com.example.cartapp.data.favorite.local.entities.FavoriteEntity
import com.example.cartapp.data.favorite.mapper.toDomain
import com.example.cartapp.data.favorite.mapper.toEntity
import com.example.cartapp.data.favorite.mapper.toDomainList
import com.example.cartapp.domain.model.Favorite
import org.junit.Assert.assertEquals
import org.junit.Test

class FavoriteMapperTest {

    @Test
    fun `map FavoriteEntity to Favorite`() {
        // Given
        val entity = FavoriteEntity(
            productId = "test-id",
            name = "Test Product",
            price = "100.0",
            image = "test-image.jpg",
            description = "Test description",
            model = "Test Model",
            brand = "Test Brand",
            createdAt = "2024-01-01"
        )

        // When
        val result = entity.toDomain()

        // Then
        assertEquals("test-id", result.productId)
        assertEquals("Test Product", result.name)
        assertEquals("100.0", result.price)
        assertEquals("test-image.jpg", result.image)
        assertEquals("Test description", result.description)
        assertEquals("Test Model", result.model)
        assertEquals("Test Brand", result.brand)
        assertEquals("2024-01-01", result.createdAt)
    }

    @Test
    fun `map Favorite to FavoriteEntity`() {
        // Given
        val domain = Favorite(
            productId = "test-id",
            name = "Test Product",
            price = "100.0",
            image = "test-image.jpg",
            description = "Test description",
            model = "Test Model",
            brand = "Test Brand",
            createdAt = "2024-01-01"
        )

        // When
        val result = domain.toEntity()

        // Then
        assertEquals("test-id", result.productId)
        assertEquals("Test Product", result.name)
        assertEquals("100.0", result.price)
        assertEquals("test-image.jpg", result.image)
        assertEquals("Test description", result.description)
        assertEquals("Test Model", result.model)
        assertEquals("Test Brand", result.brand)
        assertEquals("2024-01-01", result.createdAt)
    }

    @Test
    fun `map list of FavoriteEntity to list of Favorite`() {
        // Given
        val entities = listOf(
            FavoriteEntity(
                productId = "test-id-1",
                name = "Test Product 1",
                price = "100.0",
                image = "test-image-1.jpg",
                description = "Test description 1",
                model = "Test Model 1",
                brand = "Test Brand 1",
                createdAt = "2024-01-01"
            ),
            FavoriteEntity(
                productId = "test-id-2",
                name = "Test Product 2",
                price = "200.0",
                image = "test-image-2.jpg",
                description = "Test description 2",
                model = "Test Model 2",
                brand = "Test Brand 2",
                createdAt = "2024-01-02"
            )
        )

        // When
        val result = entities.toDomainList()

        // Then
        assertEquals(2, result.size)
        assertEquals("test-id-1", result[0].productId)
        assertEquals("test-id-2", result[1].productId)
        assertEquals("2024-01-01", result[0].createdAt)
        assertEquals("2024-01-02", result[1].createdAt)
    }
} 