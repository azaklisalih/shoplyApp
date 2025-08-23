package com.example.cartapp.data.home.mapper

import com.example.cartapp.data.home.remote.dto.ProductDto
import com.example.cartapp.domain.model.Product
import org.junit.Assert.assertEquals
import org.junit.Test

class ProductMapperTest {

    @Test
    fun `ProductDto toDomain should map correctly`() {
        // Given
        val productDto = ProductDto(
            id = "1",
            name = "Test Product",
            image = "https://example.com/image.jpg",
            price = "99.99",
            description = "Test Description",
            model = "Test Model",
            brand = "Test Brand",
            createdAt = "2024-01-01"
        )

        // When
        val result = productDto.toDomain()

        // Then
        assertEquals("1", result.id)
        assertEquals("Test Product", result.name)
        assertEquals("https://example.com/image.jpg", result.image)
        assertEquals("99.99", result.price)
        assertEquals("Test Description", result.description)
        assertEquals("Test Model", result.model)
        assertEquals("Test Brand", result.brand)
        assertEquals("2024-01-01", result.createdAt)
    }

    @Test
    fun `ProductDto toDomain should handle empty strings`() {
        // Given
        val productDto = ProductDto(
            id = "",
            name = "",
            image = "",
            price = "",
            description = "",
            model = "",
            brand = "",
            createdAt = ""
        )

        // When
        val result = productDto.toDomain()

        // Then
        assertEquals("", result.id)
        assertEquals("", result.name)
        assertEquals("", result.image)
        assertEquals("", result.price)
        assertEquals("", result.description)
        assertEquals("", result.model)
        assertEquals("", result.brand)
        assertEquals("", result.createdAt)
    }

    @Test
    fun `List ProductDto toDomainList should map correctly`() {
        // Given
        val productDtos = listOf(
            ProductDto(
                id = "1",
                name = "Product 1",
                image = "image1.jpg",
                price = "10.99",
                description = "Description 1",
                model = "Model 1",
                brand = "Brand 1",
                createdAt = "2024-01-01"
            ),
            ProductDto(
                id = "2",
                name = "Product 2",
                image = "image2.jpg",
                price = "20.99",
                description = "Description 2",
                model = "Model 2",
                brand = "Brand 2",
                createdAt = "2024-01-02"
            )
        )

        // When
        val result = productDtos.toDomainList()

        // Then
        assertEquals(2, result.size)
        assertEquals("1", result[0].id)
        assertEquals("Product 1", result[0].name)
        assertEquals("2", result[1].id)
        assertEquals("Product 2", result[1].name)
    }

    @Test
    fun `Empty list toDomainList should return empty list`() {
        // Given
        val productDtos = emptyList<ProductDto>()

        // When
        val result = productDtos.toDomainList()

        // Then
        assertEquals(0, result.size)
    }
} 