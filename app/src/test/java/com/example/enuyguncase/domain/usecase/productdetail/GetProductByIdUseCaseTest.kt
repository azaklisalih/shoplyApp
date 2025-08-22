package com.example.cartapp.domain.usecase.productdetail

import com.example.cartapp.domain.model.Dimensions
import com.example.cartapp.domain.model.Meta
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

class GetProductByIdUseCaseTest {

    @Mock
    private lateinit var mockProductRepository: ProductRepository

    private lateinit var getProductByIdUseCase: GetProductByIdUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        getProductByIdUseCase = GetProductByIdUseCase(mockProductRepository)
    }

    @Test
    fun `invoke should return product when repository returns data`() = runTest {
        // Given
        val mockProduct = Product(
            id = 1,
            title = "Test Product",
            description = "Test Description",
            category = "electronics",
            price = 99.99,
            discountPercentage = 10.0,
            rating = 4.5,
            stock = 50,
            tags = listOf("electronics"),
            brand = "Test Brand",
            sku = "TEST-001",
            weight = 500,
            dimensions = Dimensions(10.0, 5.0, 2.0),
            warrantyInformation = "1 year warranty",
            shippingInformation = "Free shipping",
            availabilityStatus = "In Stock",
            reviews = emptyList(),
            returnPolicy = "30 days return",
            minimumOrderQuantity = 1,
            meta = Meta("2024-01-01", "2024-01-01", "123456789", "QR123"),
            thumbnail = "https://example.com/thumbnail.jpg",
            images = listOf("https://example.com/image1.jpg")
        )

        whenever(mockProductRepository.getProductByIdFlow(1))
            .thenReturn(flowOf(mockProduct))

        // When
        val result = getProductByIdUseCase(1)

        // Then
        result.collect { product ->
            assertEquals(1, product.id)
            assertEquals("Test Product", product.title)
            assertEquals("Test Description", product.description)
            assertEquals("electronics", product.category)
            assertEquals(99.99, product.price, 0.01)
            assertEquals(89.99, product.discountedPrice, 0.01)
        }

        verify(mockProductRepository).getProductByIdFlow(1)
    }

    @Test
    fun `invoke should handle large product ID`() = runTest {
        // Given
        val mockProduct = Product(
            id = 999999,
            title = "Large ID Product",
            description = "Description",
            category = "electronics",
            price = 99.99,
            discountPercentage = 10.0,
            rating = 4.5,
            stock = 50,
            tags = listOf("electronics"),
            brand = "Test Brand",
            sku = "TEST-001",
            weight = 500,
            dimensions = Dimensions(10.0, 5.0, 2.0),
            warrantyInformation = "1 year warranty",
            shippingInformation = "Free shipping",
            availabilityStatus = "In Stock",
            reviews = emptyList(),
            returnPolicy = "30 days return",
            minimumOrderQuantity = 1,
            meta = Meta("2024-01-01", "2024-01-01", "123456789", "QR123"),
            thumbnail = "https://example.com/thumbnail.jpg",
            images = listOf("https://example.com/image1.jpg")
        )

        whenever(mockProductRepository.getProductByIdFlow(999999))
            .thenReturn(flowOf(mockProduct))

        // When
        val result = getProductByIdUseCase(999999)

        // Then
        result.collect { product ->
            assertEquals(999999, product.id)
            assertEquals("Large ID Product", product.title)
        }

        verify(mockProductRepository).getProductByIdFlow(999999)
    }
} 