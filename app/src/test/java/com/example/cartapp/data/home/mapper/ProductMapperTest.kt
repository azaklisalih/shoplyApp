package com.example.cartapp.data.home.mapper

import com.example.cartapp.data.home.remote.dto.ProductDto
import com.example.cartapp.data.home.mapper.toDomain
import com.example.cartapp.data.home.mapper.toDomainList
import com.example.cartapp.domain.model.Product
import org.junit.Assert.assertEquals
import org.junit.Test

class ProductMapperTest {

    @Test
    fun `map ProductDto to Product`() {
        // Given
        val dto = ProductDto(
            id = "test-id",
            name = "Test Product",
            image = "test-image.jpg",
            price = "100.0",
            description = "Test description",
            model = "Test Model",
            brand = "Test Brand",
            createdAt = "2024-01-01"
        )

        // When
        val result = dto.toDomain()

        // Then
        assertEquals("test-id", result.id)
        assertEquals("Test Product", result.name)
        assertEquals("test-image.jpg", result.image)
        assertEquals("100.0", result.price)
        assertEquals("Test description", result.description)
        assertEquals("Test Model", result.model)
        assertEquals("Test Brand", result.brand)
        assertEquals("2024-01-01", result.createdAt)
    }

    @Test
    fun `map list of ProductDto to list of Product`() {
        // Given
        val dtos = listOf(
            ProductDto(
                id = "test-id-1",
                name = "Test Product 1",
                image = "test-image-1.jpg",
                price = "100.0",
                description = "Test description 1",
                model = "Test Model 1",
                brand = "Test Brand 1",
                createdAt = "2024-01-01"
            ),
            ProductDto(
                id = "test-id-2",
                name = "Test Product 2",
                image = "test-image-2.jpg",
                price = "200.0",
                description = "Test description 2",
                model = "Test Model 2",
                brand = "Test Brand 2",
                createdAt = "2024-01-02"
            )
        )

        // When
        val result = dtos.toDomainList()

        // Then
        assertEquals(2, result.size)
        assertEquals("test-id-1", result[0].id)
        assertEquals("test-id-2", result[1].id)
        assertEquals("Test Product 1", result[0].name)
        assertEquals("Test Product 2", result[1].name)
    }
} 