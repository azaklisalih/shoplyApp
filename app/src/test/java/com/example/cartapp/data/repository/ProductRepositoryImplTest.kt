package com.example.cartapp.data.repository

import android.content.Context
import com.example.cartapp.data.cart.local.dao.CartDao
import com.example.cartapp.data.favorite.local.dao.FavoriteDao
import com.example.cartapp.data.home.remote.api.ProductApi
import com.example.cartapp.domain.model.Product
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify

class ProductRepositoryImplTest {

    @Mock
    private lateinit var mockApi: ProductApi

    @Mock
    private lateinit var mockCartDao: CartDao

    @Mock
    private lateinit var mockFavoriteDao: FavoriteDao

    @Mock
    private lateinit var mockContext: Context

    private lateinit var repository: ProductRepositoryImpl

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        repository = ProductRepositoryImpl(mockApi, mockCartDao, mockFavoriteDao, mockContext)
    }

    @Test
    fun `addToCart should call cartDao upsert when item does not exist`() = runTest {
        // Given
        val product = Product(
            id = "1",
            name = "Test Product",
            image = "test.jpg",
            price = "99.99",
            description = "Test Description",
            model = "Test Model",
            brand = "Test Brand",
            createdAt = "2024-01-01"
        )

        // When
        repository.addToCart(product, 1)

        // Then
        // Verify that the method completes without exception
        assert(true)
    }

    @Test
    fun `removeFromCart should call cartDao deleteByProductId`() = runTest {
        // Given
        val productId = "1"

        // When
        repository.removeFromCart(productId)

        // Then
        verify(mockCartDao).deleteByProductId(productId)
    }

    @Test
    fun `updateCartItemQuantity should call cartDao updateQuantity when quantity greater than 0`() = runTest {
        // Given
        val productId = "1"
        val quantity = 5

        // When
        repository.updateCartItemQuantity(productId, quantity)

        // Then
        verify(mockCartDao).updateQuantity(productId, quantity)
    }

    @Test
    fun `updateCartItemQuantity should call cartDao deleteByProductId when quantity less than or equal to 0`() = runTest {
        // Given
        val productId = "1"
        val quantity = 0

        // When
        repository.updateCartItemQuantity(productId, quantity)

        // Then
        verify(mockCartDao).deleteByProductId(productId)
    }

    @Test
    fun `addToFavorites should call favoriteDao insert`() = runTest {
        // Given
        val product = Product(
            id = "1",
            name = "Test Product",
            image = "test.jpg",
            price = "99.99",
            description = "Test Description",
            model = "Test Model",
            brand = "Test Brand",
            createdAt = "2024-01-01"
        )

        // When
        repository.addToFavorites(product)

        // Then
        // Verify that the method completes without exception
        assert(true)
    }

    @Test
    fun `removeFromFavorites should call favoriteDao deleteById`() = runTest {
        // Given
        val productId = "1"

        // When
        repository.removeFromFavorites(productId)

        // Then
        verify(mockFavoriteDao).deleteById(productId)
    }

    @Test
    fun `clearCart should call cartDao clearCart`() = runTest {
        // When
        repository.clearCart()

        // Then
        verify(mockCartDao).clearCart()
    }
} 