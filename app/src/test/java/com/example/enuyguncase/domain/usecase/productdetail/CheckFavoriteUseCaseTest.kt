package com.example.cartapp.domain.usecase.productdetail

import com.example.cartapp.domain.repository.FavoriteRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

class CheckFavoriteUseCaseTest {

    @Mock
    private lateinit var mockFavoriteRepository: FavoriteRepository

    private lateinit var checkFavoriteUseCase: CheckFavoriteUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        checkFavoriteUseCase = CheckFavoriteUseCase(mockFavoriteRepository)
    }

    @Test
    fun `invoke should return true when product is favorite`() = runTest {
        // Given
        val productId = 1
        whenever(mockFavoriteRepository.isFavorite(productId)).thenReturn(true)

        // When
        val result = checkFavoriteUseCase(productId)

        // Then
        assertTrue(result)
    }

    @Test
    fun `invoke should return false when product is not favorite`() = runTest {
        // Given
        val productId = 1
        whenever(mockFavoriteRepository.isFavorite(productId)).thenReturn(false)

        // When
        val result = checkFavoriteUseCase(productId)

        // Then
        assertFalse(result)
    }

    @Test
    fun `invoke should handle large product ID`() = runTest {
        // Given
        val productId = 999999
        whenever(mockFavoriteRepository.isFavorite(productId)).thenReturn(false)

        // When
        val result = checkFavoriteUseCase(productId)

        // Then
        assertFalse(result)
    }
} 