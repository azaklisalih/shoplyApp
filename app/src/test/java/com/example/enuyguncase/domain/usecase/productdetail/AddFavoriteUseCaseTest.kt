package com.example.cartapp.domain.usecase.productdetail

import com.example.cartapp.data.home.local.entities.FavoriteEntity
import com.example.cartapp.domain.model.Dimensions
import com.example.cartapp.domain.model.Meta
import com.example.cartapp.domain.model.Product
import com.example.cartapp.domain.repository.FavoriteRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import org.mockito.kotlin.any

class AddFavoriteUseCaseTest {

    @Mock
    private lateinit var mockFavoriteRepository: FavoriteRepository

    private lateinit var addFavoriteUseCase: AddFavoriteUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        addFavoriteUseCase = AddFavoriteUseCase(mockFavoriteRepository)
    }

    @Test
    fun `invoke should call repository addFavorite method`() = runTest {
        // Given
        val product = Product(
            id = 1,
            title = "Test Product",
            description = "Test Description",
            category = "electronics",
            price = 99.99,
            discountPercentage = 10.0,
            rating = 4.0,
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
            images = listOf("https://example.com/image1.jpg", "https://example.com/image2.jpg")
        )

        // When
        addFavoriteUseCase(product)

        // Then
        verify(mockFavoriteRepository).addFavorite(any())
    }

    @Test
    fun `invoke should handle product with single image`() = runTest {
        // Given
        val product = Product(
            id = 1,
            title = "Test Product",
            description = "Test Description",
            category = "electronics",
            price = 99.99,
            discountPercentage = 10.0,
            rating = 4.0,
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

        // When
        addFavoriteUseCase(product)

        // Then
        verify(mockFavoriteRepository).addFavorite(any())
    }

    @Test
    fun `invoke should handle product with empty images list`() = runTest {
        // Given
        val product = Product(
            id = 1,
            title = "Test Product",
            description = "Test Description",
            category = "electronics",
            price = 99.99,
            discountPercentage = 10.0,
            rating = 4.0,
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
            images = emptyList()
        )

        // When
        addFavoriteUseCase(product)

        // Then
        verify(mockFavoriteRepository).addFavorite(any())
    }
} 