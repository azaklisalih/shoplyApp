package com.example.cartapp.domain.usecase.home

import com.example.cartapp.domain.model.Brand
import com.example.cartapp.domain.model.Model
import com.example.cartapp.domain.model.Product
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class LocalSearchUseCaseTest {

    private lateinit var localSearchUseCase: LocalSearchUseCase

    private val testProducts = listOf(
        Product(
            id = "1",
            name = "iPhone 15",
            image = "iphone15.jpg",
            price = "999.99",
            description = "Latest iPhone model",
            model = "15",
            brand = "Apple",
            createdAt = "2024-01-01"
        ),
        Product(
            id = "2",
            name = "Samsung Galaxy S24",
            image = "galaxy.jpg",
            price = "899.99",
            description = "Latest Samsung model",
            model = "S24",
            brand = "Samsung",
            createdAt = "2024-01-02"
        ),
        Product(
            id = "3",
            name = "Google Pixel 8",
            image = "pixel.jpg",
            price = "799.99",
            description = "Latest Google model",
            model = "8",
            brand = "Google",
            createdAt = "2024-01-03"
        )
    )

    @Before
    fun setUp() {
        localSearchUseCase = LocalSearchUseCase()
    }

    @Test
    fun `search with empty query should return all products`() {
        // When
        val result = localSearchUseCase(testProducts, "", emptySet(), emptySet())

        // Then
        assertEquals(3, result.size)
        assertEquals(testProducts, result)
    }

    @Test
    fun `search with product name should return matching products`() {
        // When
        val result = localSearchUseCase(testProducts, "iPhone", emptySet(), emptySet())

        // Then
        assertEquals(1, result.size)
        assertEquals("iPhone 15", result[0].name)
    }

    @Test
    fun `search with brand name should return matching products`() {
        // When
        val result = localSearchUseCase(testProducts, "Apple", emptySet(), emptySet())

        // Then
        assertEquals(1, result.size)
        assertEquals("Apple", result[0].brand)
    }

    @Test
    fun `search with model name should return matching products`() {
        // When
        val result = localSearchUseCase(testProducts, "S24", emptySet(), emptySet())

        // Then
        assertEquals(1, result.size)
        assertEquals("S24", result[0].model)
    }

    @Test
    fun `search with description should return matching products`() {
        // When
        val result = localSearchUseCase(testProducts, "Latest", emptySet(), emptySet())

        // Then
        assertEquals(3, result.size)
    }

    @Test
    fun `search with case insensitive should work`() {
        // When
        val result = localSearchUseCase(testProducts, "iphone", emptySet(), emptySet())

        // Then
        assertEquals(1, result.size)
        assertEquals("iPhone 15", result[0].name)
    }

    @Test
    fun `search with brand filter should return filtered results`() {
        // When
        val result = localSearchUseCase(
            testProducts, 
            "Latest", 
            setOf("Apple"), 
            emptySet()
        )

        // Then
        assertEquals(1, result.size)
        assertEquals("Apple", result[0].brand)
    }

    @Test
    fun `search with model filter should return filtered results`() {
        // When
        val result = localSearchUseCase(
            testProducts, 
            "Latest", 
            emptySet(), 
            setOf("15")
        )

        // Then
        assertEquals(1, result.size)
        assertEquals("15", result[0].model)
    }

    @Test
    fun `search with both brand and model filters should return filtered results`() {
        // When
        val result = localSearchUseCase(
            testProducts, 
            "Latest", 
            setOf("Apple"), 
            setOf("15")
        )

        // Then
        assertEquals(1, result.size)
        assertEquals("Apple", result[0].brand)
        assertEquals("15", result[0].model)
    }

    @Test
    fun `search with no matches should return empty list`() {
        // When
        val result = localSearchUseCase(testProducts, "NonExistent", emptySet(), emptySet())

        // Then
        assertTrue(result.isEmpty())
    }

    @Test
    fun `search with filters but no search query should return filtered products`() {
        // When
        val result = localSearchUseCase(
            testProducts, 
            "", 
            setOf("Apple"), 
            emptySet()
        )

        // Then
        assertEquals(1, result.size)
        assertEquals("Apple", result[0].brand)
    }
} 