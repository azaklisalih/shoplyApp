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
    fun `invoke should return ProductPage with category products`() = runTest {
        // Given
        val mockProductsResponse = ProductsResponse(
            products = listOf(
                ProductDto(
                    id = 1,
                    title = "Electronics Product",
                    description = "Electronics description",
                    category = "electronics",
                    price = 99.99,
                    discountPercentage = 10.0,
                    rating = 4.5,
                    stock = 50,
                    tags = listOf("electronics"),
                    brand = "Electronics Brand",
                    sku = "ELEC-001",
                    weight = 500,
                    dimensions = DimensionsDto(10.0, 5.0, 2.0),
                    warrantyInformation = "1 year warranty",
                    shippingInformation = "Free shipping",
                    availabilityStatus = "In Stock",
                    reviews = emptyList(),
                    returnPolicy = "30 days return",
                    minimumOrderQuantity = 1,
                    meta = MetaDto("2024-01-01", "2024-01-01", "123456789", "QR123"),
                    thumbnail = "https://example.com/electronics.jpg",
                    images = listOf("https://example.com/electronics1.jpg")
                )
            ),
            total = 1,
            skip = 0,
            limit = 10
        )

        whenever(mockRepository.getProductsByCategoryFlow("electronics", 10, 0, "price", "desc"))
            .thenReturn(flowOf(mockProductsResponse))

        // When
        val result = getProductsByCategoryUseCase("electronics", 10, 0, "price", "desc")

        // Then
        result.collect { productPage ->
            assertEquals(1, productPage.products.size)
            assertEquals(1, productPage.total)
            assertEquals(1, productPage.products[0].id)
            assertEquals("Electronics Product", productPage.products[0].title)
            assertEquals("electronics", productPage.products[0].category)
        }

        verify(mockRepository).getProductsByCategoryFlow("electronics", 10, 0, "price", "desc")
    }

    @Test
    fun `invoke should return empty ProductPage when category has no products`() = runTest {
        // Given
        val mockProductsResponse = ProductsResponse(
            products = emptyList(),
            total = 0,
            skip = 0,
            limit = 10
        )

        whenever(mockRepository.getProductsByCategoryFlow("nonexistent", null, null, null, null))
            .thenReturn(flowOf(mockProductsResponse))

        // When
        val result = getProductsByCategoryUseCase("nonexistent")

        // Then
        result.collect { productPage ->
            assertEquals(0, productPage.products.size)
            assertEquals(0, productPage.total)
        }

        verify(mockRepository).getProductsByCategoryFlow("nonexistent", null, null, null, null)
    }

    @Test
    fun `invoke should handle empty category string`() = runTest {
        // Given
        val mockProductsResponse = ProductsResponse(
            products = emptyList(),
            total = 0,
            skip = 0,
            limit = 10
        )

        whenever(mockRepository.getProductsByCategoryFlow("", null, null, null, null))
            .thenReturn(flowOf(mockProductsResponse))

        // When
        val result = getProductsByCategoryUseCase("")

        // Then
        result.collect { productPage ->
            assertEquals(0, productPage.products.size)
            assertEquals(0, productPage.total)
        }

        verify(mockRepository).getProductsByCategoryFlow("", null, null, null, null)
    }
} 