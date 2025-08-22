package com.example.cartapp.domain.usecase.home

import com.example.cartapp.data.home.remote.dto.DimensionsDto
import com.example.cartapp.data.home.remote.dto.MetaDto
import com.example.cartapp.data.home.remote.dto.ProductDto
import com.example.cartapp.data.home.remote.dto.ProductsResponse
import com.example.cartapp.domain.model.ProductPage
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

class SearchProductsUseCaseTest {

    @Mock
    private lateinit var mockRepository: ProductRepository

    private lateinit var searchProductsUseCase: SearchProductsUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        searchProductsUseCase = SearchProductsUseCase(mockRepository)
    }

    @Test
    fun `invoke should return ProductPage with search results`() = runTest {
        // Given
        val mockProductsResponse = ProductsResponse(
            products = listOf(
                ProductDto(
                    id = 1,
                    title = "iPhone Search Result",
                    description = "iPhone description",
                    category = "electronics",
                    price = 999.99,
                    discountPercentage = 5.0,
                    rating = 4.8,
                    stock = 10,
                    tags = listOf("electronics", "phone"),
                    brand = "Apple",
                    sku = "IPHONE-001",
                    weight = 200,
                    dimensions = DimensionsDto(7.0, 3.5, 0.8),
                    warrantyInformation = "1 year warranty",
                    shippingInformation = "Free shipping",
                    availabilityStatus = "In Stock",
                    reviews = emptyList(),
                    returnPolicy = "30 days return",
                    minimumOrderQuantity = 1,
                    meta = MetaDto("2024-01-01", "2024-01-01", "123456789", "QR123"),
                    thumbnail = "https://example.com/iphone.jpg",
                    images = listOf("https://example.com/iphone1.jpg")
                )
            ),
            total = 1,
            skip = 0,
            limit = 10
        )

        whenever(mockRepository.searchProductsFlow("iPhone"))
            .thenReturn(flowOf(mockProductsResponse))

        // When
        val result = searchProductsUseCase("iPhone")

        // Then
        result.collect { productPage ->
            assertEquals(1, productPage.products.size)
            assertEquals(1, productPage.total)
            assertEquals(1, productPage.products[0].id)
            assertEquals("iPhone Search Result", productPage.products[0].title)
            assertEquals("Apple", productPage.products[0].brand)
        }

        verify(mockRepository).searchProductsFlow("iPhone")
    }

    @Test
    fun `invoke should return empty ProductPage when no search results found`() = runTest {
        // Given
        val mockProductsResponse = ProductsResponse(
            products = emptyList(),
            total = 0,
            skip = 0,
            limit = 10
        )

        whenever(mockRepository.searchProductsFlow("nonexistent"))
            .thenReturn(flowOf(mockProductsResponse))

        // When
        val result = searchProductsUseCase("nonexistent")

        // Then
        result.collect { productPage ->
            assertEquals(0, productPage.products.size)
            assertEquals(0, productPage.total)
        }

        verify(mockRepository).searchProductsFlow("nonexistent")
    }

    @Test
    fun `invoke should handle empty search query`() = runTest {
        // Given
        val mockProductsResponse = ProductsResponse(
            products = emptyList(),
            total = 0,
            skip = 0,
            limit = 10
        )

        whenever(mockRepository.searchProductsFlow(""))
            .thenReturn(flowOf(mockProductsResponse))

        // When
        val result = searchProductsUseCase("")

        // Then
        result.collect { productPage ->
            assertEquals(0, productPage.products.size)
            assertEquals(0, productPage.total)
        }

        verify(mockRepository).searchProductsFlow("")
    }

    @Test
    fun `invoke should handle special characters in search query`() = runTest {
        // Given
        val mockProductsResponse = ProductsResponse(
            products = emptyList(),
            total = 0,
            skip = 0,
            limit = 10
        )

        whenever(mockRepository.searchProductsFlow("test@#$%"))
            .thenReturn(flowOf(mockProductsResponse))

        // When
        val result = searchProductsUseCase("test@#$%")

        // Then
        result.collect { productPage ->
            assertEquals(0, productPage.products.size)
        }

        verify(mockRepository).searchProductsFlow("test@#$%")
    }
} 