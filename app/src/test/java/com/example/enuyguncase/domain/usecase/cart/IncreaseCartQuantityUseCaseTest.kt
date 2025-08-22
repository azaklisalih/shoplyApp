package com.example.cartapp.domain.usecase.cart

import com.example.cartapp.domain.repository.CartRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class IncreaseCartQuantityUseCaseTest {

    @Mock
    private lateinit var mockCartRepository: CartRepository

    private lateinit var increaseCartQuantityUseCase: IncreaseCartQuantityUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        increaseCartQuantityUseCase = IncreaseCartQuantityUseCase(mockCartRepository)
    }

    @Test
    fun `invoke should call repository increase method`() = runTest {
        // Given
        val productId = 1

        // When
        increaseCartQuantityUseCase(productId)

        // Then
        verify(mockCartRepository).increase(productId)
    }

    @Test
    fun `invoke should handle large product ID`() = runTest {
        // Given
        val productId = 999999

        // When
        increaseCartQuantityUseCase(productId)

        // Then
        verify(mockCartRepository).increase(productId)
    }

    @Test
    fun `invoke should handle zero product ID`() = runTest {
        // Given
        val productId = 0

        // When
        increaseCartQuantityUseCase(productId)

        // Then
        verify(mockCartRepository).increase(productId)
    }
} 