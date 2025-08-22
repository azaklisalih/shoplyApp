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
    fun `invoke should return ProductPage with products when repository returns data`() = runTest {
        // Given
        val mockProductsResponse = ProductsResponse(
            products = listOf(
                ProductDto(
                    id = 1,
                    title = "Test Product 1",
                    description = "Description 1",
                    category = "electronics",
                    price = 99.99,
                    discountPercentage = 10.0,
                    rating = 4.5,
                    stock = 50,
                    tags = listOf("electronics"),
                    brand = "Brand 1",
                    sku = "SKU-001",
                    weight = 500,
                    dimensions = DimensionsDto(10.0, 5.0, 2.0),
                    warrantyInformation = "1 year warranty",
                    shippingInformation = "Free shipping",
                    availabilityStatus = "In Stock",
                    reviews = emptyList(),
                    returnPolicy = "30 days return",
                    minimumOrderQuantity = 1,
                    meta = MetaDto("2024-01-01", "2024-01-01", "123456789", "QR123"),
                    thumbnail = "https://example.com/thumbnail1.jpg",
                    images = listOf("https://example.com/image1.jpg")
                ),
                ProductDto(
                    id = 2,
                    title = "Test Product 2",
                    description = "Description 2",
                    category = "clothing",
                    price = 49.99,
                    discountPercentage = 5.0,
                    rating = 4.0,
                    stock = 25,
                    tags = listOf("clothing"),
                    brand = "Brand 2",
                    sku = "SKU-002",
                    weight = 200,
                    dimensions = DimensionsDto(5.0, 3.0, 1.0),
                    warrantyInformation = "No warranty",
                    shippingInformation = "Standard shipping",
                    availabilityStatus = "In Stock",
                    reviews = emptyList(),
                    returnPolicy = "14 days return",
                    minimumOrderQuantity = 1,
                    meta = MetaDto("2024-01-01", "2024-01-01", "987654321", "QR456"),
                    thumbnail = "https://example.com/thumbnail2.jpg",
                    images = listOf("https://example.com/image2.jpg")
                )
            ),
            total = 2,
            skip = 0,
            limit = 10
        )

        whenever(mockRepository.getProductsFlow(10, 0, "price", "desc"))
            .thenReturn(flowOf(mockProductsResponse))

        // When
        val result = getProductsUseCase(10, 0, "price", "desc")

        // Then
        result.collect { productPage ->
            assertEquals(2, productPage.products.size)
            assertEquals(2, productPage.total)
            assertEquals(1, productPage.products[0].id)
            assertEquals("Test Product 1", productPage.products[0].title)
            assertEquals(2, productPage.products[1].id)
            assertEquals("Test Product 2", productPage.products[1].title)
        }

        verify(mockRepository).getProductsFlow(10, 0, "price", "desc")
    }

    @Test
    fun `invoke should return empty ProductPage when repository returns empty data`() = runTest {
        // Given
        val mockProductsResponse = ProductsResponse(
            products = emptyList(),
            total = 0,
            skip = 0,
            limit = 10
        )

        whenever(mockRepository.getProductsFlow(null, null, null, null))
            .thenReturn(flowOf(mockProductsResponse))

        // When
        val result = getProductsUseCase()

        // Then
        result.collect { productPage ->
            assertEquals(0, productPage.products.size)
            assertEquals(0, productPage.total)
        }

        verify(mockRepository).getProductsFlow(null, null, null, null)
    }

    @Test
    fun `invoke should return ProductPage with single product when repository returns one product`() = runTest {
        // Given
        val mockProductsResponse = ProductsResponse(
            products = listOf(
                ProductDto(
                    id = 1,
                    title = "Single Product",
                    description = "Single product description",
                    category = "electronics",
                    price = 99.99,
                    discountPercentage = 10.0,
                    rating = 4.5,
                    stock = 50,
                    tags = listOf("electronics"),
                    brand = "Brand 1",
                    sku = "SKU-001",
                    weight = 500,
                    dimensions = DimensionsDto(10.0, 5.0, 2.0),
                    warrantyInformation = "1 year warranty",
                    shippingInformation = "Free shipping",
                    availabilityStatus = "In Stock",
                    reviews = emptyList(),
                    returnPolicy = "30 days return",
                    minimumOrderQuantity = 1,
                    meta = MetaDto("2024-01-01", "2024-01-01", "123456789", "QR123"),
                    thumbnail = "https://example.com/thumbnail.jpg",
                    images = listOf("https://example.com/image.jpg")
                )
            ),
            total = 1,
            skip = 0,
            limit = 10
        )

        whenever(mockRepository.getProductsFlow(5, 10, "title", "asc"))
            .thenReturn(flowOf(mockProductsResponse))

        // When
        val result = getProductsUseCase(5, 10, "title", "asc")

        // Then
        result.collect { productPage ->
            assertEquals(1, productPage.products.size)
            assertEquals(1, productPage.total)
            assertEquals(1, productPage.products[0].id)
            assertEquals("Single Product", productPage.products[0].title)
        }

        verify(mockRepository).getProductsFlow(5, 10, "title", "asc")
    }

    @Test
    fun `invoke should handle pagination parameters correctly`() = runTest {
        // Given
        val mockProductsResponse = ProductsResponse(
            products = emptyList(),
            total = 100,
            skip = 20,
            limit = 10
        )

        whenever(mockRepository.getProductsFlow(10, 20, "rating", "desc"))
            .thenReturn(flowOf(mockProductsResponse))

        // When
        val result = getProductsUseCase(10, 20, "rating", "desc")

        // Then
        result.collect { productPage ->
            assertEquals(100, productPage.total)
        }

        verify(mockRepository).getProductsFlow(10, 20, "rating", "desc")
    }
} 