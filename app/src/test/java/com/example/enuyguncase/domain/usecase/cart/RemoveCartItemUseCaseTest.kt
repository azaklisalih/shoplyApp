package com.example.cartapp.domain.usecase.cart

import com.example.cartapp.domain.repository.CartRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify

class RemoveCartItemUseCaseTest {

    @Mock
    private lateinit var mockCartRepository: CartRepository

    private lateinit var removeCartItemUseCase: RemoveCartItemUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        removeCartItemUseCase = RemoveCartItemUseCase(mockCartRepository)
    }

    @Test
    fun `invoke should call repository remove method`() = runTest {
        // Given
        val productId = 1

        // When
        removeCartItemUseCase(productId)

        // Then
        verify(mockCartRepository).remove(productId)
    }

    @Test
    fun `invoke should handle large product ID`() = runTest {
        // Given
        val productId = 999999

        // When
        removeCartItemUseCase(productId)

        // Then
        verify(mockCartRepository).remove(productId)
    }

    @Test
    fun `invoke should handle zero product ID`() = runTest {
        // Given
        val productId = 0

        // When
        removeCartItemUseCase(productId)

        // Then
        verify(mockCartRepository).remove(productId)
    }
} 