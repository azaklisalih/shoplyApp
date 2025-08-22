package com.example.cartapp.domain.usecase.cart

import com.example.cartapp.domain.repository.CartRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify

class DecreaseCartQuantityUseCaseTest {

    @Mock
    private lateinit var mockCartRepository: CartRepository

    private lateinit var decreaseCartQuantityUseCase: DecreaseCartQuantityUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        decreaseCartQuantityUseCase = DecreaseCartQuantityUseCase(mockCartRepository)
    }

    @Test
    fun `invoke should call repository decrease method`() = runTest {
        // Given
        val productId = 1

        // When
        decreaseCartQuantityUseCase(productId)

        // Then
        verify(mockCartRepository).decrease(productId)
    }

    @Test
    fun `invoke should handle large product ID`() = runTest {
        // Given
        val productId = 999999

        // When
        decreaseCartQuantityUseCase(productId)

        // Then
        verify(mockCartRepository).decrease(productId)
    }

    @Test
    fun `invoke should handle zero product ID`() = runTest {
        // Given
        val productId = 0

        // When
        decreaseCartQuantityUseCase(productId)

        // Then
        verify(mockCartRepository).decrease(productId)
    }
} 