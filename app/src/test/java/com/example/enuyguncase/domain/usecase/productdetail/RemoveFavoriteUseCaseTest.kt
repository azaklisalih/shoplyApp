package com.example.cartapp.domain.usecase.productdetail

import com.example.cartapp.domain.repository.FavoriteRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify

class RemoveFavoriteUseCaseTest {

    @Mock
    private lateinit var mockFavoriteRepository: FavoriteRepository

    private lateinit var removeFavoriteUseCase: RemoveFavoriteUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        removeFavoriteUseCase = RemoveFavoriteUseCase(mockFavoriteRepository)
    }

    @Test
    fun `invoke should call repository removeFavorite method`() = runTest {
        // Given
        val productId = 1

        // When
        removeFavoriteUseCase(productId)

        // Then
        verify(mockFavoriteRepository).removeFavorite(productId)
    }

    @Test
    fun `invoke should handle large product ID`() = runTest {
        // Given
        val productId = 999999

        // When
        removeFavoriteUseCase(productId)

        // Then
        verify(mockFavoriteRepository).removeFavorite(productId)
    }

    @Test
    fun `invoke should handle zero product ID`() = runTest {
        // Given
        val productId = 0

        // When
        removeFavoriteUseCase(productId)

        // Then
        verify(mockFavoriteRepository).removeFavorite(productId)
    }
} 