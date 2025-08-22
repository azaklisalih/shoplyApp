package com.example.cartapp.domain.usecase.favorite

import com.example.cartapp.data.home.local.entities.FavoriteEntity
import com.example.cartapp.domain.model.Favorite
import com.example.cartapp.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class GetAllFavoritesUseCaseTest {

    @Mock
    private lateinit var mockFavoriteRepository: FavoriteRepository

    private lateinit var getAllFavoritesUseCase: GetAllFavoritesUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        getAllFavoritesUseCase = GetAllFavoritesUseCase(mockFavoriteRepository)
    }

    @Test
    fun `invoke should return list of favorites`() = runTest {
        // Given
        val mockFavoriteEntities = listOf(
            FavoriteEntity(
                productId = 1,
                title = "Favorite Product 1",
                images = listOf("https://example.com/image1.jpg", "https://example.com/image2.jpg"),
                description = "Description for favorite product 1",
                price = 99.99,
                discountedPrice = 89.99
            ),
            FavoriteEntity(
                productId = 2,
                title = "Favorite Product 2",
                images = listOf("https://example.com/image3.jpg"),
                description = "Description for favorite product 2",
                price = 149.99,
                discountedPrice = 134.99
            )
        )

        whenever(mockFavoriteRepository.getFavorites()).thenReturn(flowOf(mockFavoriteEntities))

        // When
        val result = getAllFavoritesUseCase()

        // Then
        result.collect { favorites ->
            assertEquals(2, favorites.size)
            
            assertEquals(1, favorites[0].productId)
            assertEquals("Favorite Product 1", favorites[0].title)
            assertEquals(listOf("https://example.com/image1.jpg", "https://example.com/image2.jpg"), favorites[0].images)
            assertEquals("Description for favorite product 1", favorites[0].description)
            assertEquals(99.99, favorites[0].price, 0.01)
            assertEquals(89.99, favorites[0].discountedPrice, 0.01)
            assertEquals("https://example.com/image1.jpg", favorites[0].thumbnailUrl)

            assertEquals(2, favorites[1].productId)
            assertEquals("Favorite Product 2", favorites[1].title)
            assertEquals(listOf("https://example.com/image3.jpg"), favorites[1].images)
            assertEquals("Description for favorite product 2", favorites[1].description)
            assertEquals(149.99, favorites[1].price, 0.01)
            assertEquals(134.99, favorites[1].discountedPrice, 0.01)
            assertEquals("https://example.com/image3.jpg", favorites[1].thumbnailUrl)
        }

        verify(mockFavoriteRepository).getFavorites()
    }

    @Test
    fun `invoke should handle empty favorites list`() = runTest {
        // Given
        val mockFavoriteEntities = emptyList<FavoriteEntity>()

        whenever(mockFavoriteRepository.getFavorites()).thenReturn(flowOf(mockFavoriteEntities))

        // When
        val result = getAllFavoritesUseCase()

        // Then
        result.collect { favorites ->
            assertEquals(0, favorites.size)
        }

        verify(mockFavoriteRepository).getFavorites()
    }

    @Test
    fun `invoke should handle favorite with empty images list`() = runTest {
        // Given
        val mockFavoriteEntities = listOf(
            FavoriteEntity(
                productId = 1,
                title = "Favorite with no images",
                images = emptyList(),
                description = "Description",
                price = 99.99,
                discountedPrice = 89.99
            )
        )

        whenever(mockFavoriteRepository.getFavorites()).thenReturn(flowOf(mockFavoriteEntities))

        // When
        val result = getAllFavoritesUseCase()

        // Then
        result.collect { favorites ->
            assertEquals(1, favorites.size)
            assertEquals(emptyList<String>(), favorites[0].images)
            assertEquals("", favorites[0].thumbnailUrl)
        }

        verify(mockFavoriteRepository).getFavorites()
    }
} 