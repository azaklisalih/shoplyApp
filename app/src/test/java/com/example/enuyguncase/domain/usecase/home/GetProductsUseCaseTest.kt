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

class GetProductsUseCaseTest {

    @Mock
    private lateinit var mockRepository: ProductRepository

    private lateinit var getProductsUseCase: GetProductsUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        getProductsUseCase = GetProductsUseCase(mockRepository)
    }

    @Test
    fun `invoke should return products when repository returns data`() = runTest {
        // Given
        val mockProducts = listOf(
            Product(
                id = "1",
                name = "Test Product 1",
                image = "image1.jpg",
                price = "99.99",
                description = "Description 1",
                model = "Model 1",
                brand = "Brand 1",
                createdAt = "2024-01-01"
            ),
            Product(
                id = "2",
                name = "Test Product 2",
                image = "image2.jpg",
                price = "49.99",
                description = "Description 2",
                model = "Model 2",
                brand = "Brand 2",
                createdAt = "2024-01-02"
            )
        )

        whenever(mockRepository.getProductsFlow(10, 0, "price", "desc"))
            .thenReturn(flowOf(mockProducts))

        // When
        val result = getProductsUseCase(10, 0, "price", "desc")

        // Then
        result.collect { products ->
            assertEquals(2, products.size)
            assertEquals("1", products[0].id)
            assertEquals("Test Product 1", products[0].name)
            assertEquals("2", products[1].id)
            assertEquals("Test Product 2", products[1].name)
        }

        verify(mockRepository).getProductsFlow(10, 0, "price", "desc")
    }

    @Test
    fun `invoke should return empty list when repository returns empty data`() = runTest {
        // Given
        val mockProducts = emptyList<Product>()

        whenever(mockRepository.getProductsFlow(null, null, null, null))
            .thenReturn(flowOf(mockProducts))

        // When
        val result = getProductsUseCase()

        // Then
        result.collect { products ->
            assertEquals(0, products.size)
        }

        verify(mockRepository).getProductsFlow(null, null, null, null)
    }

    @Test
    fun `invoke should return single product when repository returns one product`() = runTest {
        // Given
        val mockProducts = listOf(
            Product(
                id = "1",
                name = "Single Product",
                image = "image.jpg",
                price = "99.99",
                description = "Single product description",
                model = "Model 1",
                brand = "Brand 1",
                createdAt = "2024-01-01"
            )
        )

        whenever(mockRepository.getProductsFlow(5, 10, "name", "asc"))
            .thenReturn(flowOf(mockProducts))

        // When
        val result = getProductsUseCase(5, 10, "name", "asc")

        // Then
        result.collect { products ->
            assertEquals(1, products.size)
            assertEquals("1", products[0].id)
            assertEquals("Single Product", products[0].name)
        }

        verify(mockRepository).getProductsFlow(5, 10, "name", "asc")
    }

    @Test
    fun `invoke should handle pagination parameters correctly`() = runTest {
        // Given
        val mockProducts = emptyList<Product>()

        whenever(mockRepository.getProductsFlow(10, 20, "brand", "desc"))
            .thenReturn(flowOf(mockProducts))

        // When
        val result = getProductsUseCase(10, 20, "brand", "desc")

        // Then
        result.collect { products ->
            assertEquals(0, products.size)
        }

        verify(mockRepository).getProductsFlow(10, 20, "brand", "desc")
    }
} 