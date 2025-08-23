package com.example.cartapp.domain.usecase.home

import com.example.cartapp.data.home.remote.dto.ProductDto
import com.example.cartapp.domain.model.Product
import com.example.cartapp.domain.repository.ProductRepository
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class GetProductsByCategoryUseCaseTest {

    @Mock
    private lateinit var mockRepository: ProductRepository

    private lateinit var getProductsByCategoryUseCase: GetProductsByCategoryUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        getProductsByCategoryUseCase = GetProductsByCategoryUseCase(mockRepository)
    }

    @Test
    fun `invoke should return products when repository returns category data`() = runTest {
        // Given
        val mockProducts = listOf(
            Product(
                id = "1",
                name = "Electronics Product 1",
                image = "image1.jpg",
                price = "99.99",
                description = "Description 1",
                model = "Model 1",
                brand = "Brand 1",
                createdAt = "2024-01-01"
            ),
            Product(
                id = "2",
                name = "Electronics Product 2",
                image = "image2.jpg",
                price = "49.99",
                description = "Description 2",
                model = "Model 2",
                brand = "Brand 2",
                createdAt = "2024-01-02"
            )
        )

        whenever(mockRepository.getProductsFlow(null, null, null, null, "electronics", null))
            .thenReturn(flowOf(mockProducts))

        // When
        val result = getProductsByCategoryUseCase("electronics")

        // Then
        result.collect { products ->
            assertEquals(2, products.size)
            assertEquals("1", products[0].id)
            assertEquals("Electronics Product 1", products[0].name)
            assertEquals("2", products[1].id)
            assertEquals("Electronics Product 2", products[1].name)
        }

        verify(mockRepository).getProductsFlow(null, null, null, null, "electronics", null)
    }

    @Test
    fun `invoke should return empty list when no category products found`() = runTest {
        // Given
        val mockProducts = emptyList<Product>()

        whenever(mockRepository.getProductsFlow(null, null, null, null, "nonexistent", null))
            .thenReturn(flowOf(mockProducts))

        // When
        val result = getProductsByCategoryUseCase("nonexistent")

        // Then
        result.collect { products ->
            assertEquals(0, products.size)
        }

        verify(mockRepository).getProductsFlow(null, null, null, null, "nonexistent", null)
    }

    @Test
    fun `invoke should handle empty category`() = runTest {
        // Given
        val mockProducts = emptyList<Product>()

        whenever(mockRepository.getProductsFlow(null, null, null, null, "", null))
            .thenReturn(flowOf(mockProducts))

        // When
        val result = getProductsByCategoryUseCase("")

        // Then
        result.collect { products ->
            assertEquals(0, products.size)
        }

        verify(mockRepository).getProductsFlow(null, null, null, null, "", null)
    }
} 