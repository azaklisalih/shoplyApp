package com.example.cartapp.data.home.repository

import com.example.cartapp.data.home.local.dao.FavoriteDao
import com.example.cartapp.data.home.local.entities.FavoriteEntity
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class FavoriteRepositoryImplTest {

    @Mock
    private lateinit var mockFavoriteDao: FavoriteDao

    private lateinit var favoriteRepository: FavoriteRepositoryImpl

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        favoriteRepository = FavoriteRepositoryImpl(mockFavoriteDao)
    }

    @Test
    fun `addFavorite should call dao insert method with correct entity`() = runTest {
        // Given
        val favoriteEntity = FavoriteEntity(
            productId = 1,
            title = "Test Product",
            images = listOf("https://example.com/image1.jpg", "https://example.com/image2.jpg"),
            description = "Test Description",
            price = 99.99,
            discountedPrice = 89.99
        )

        // When
        favoriteRepository.addFavorite(favoriteEntity)

        // Then
        verify(mockFavoriteDao).insert(favoriteEntity)
    }

    @Test
    fun `addFavorite should create new entity with same properties`() = runTest {
        // Given
        val originalEntity = FavoriteEntity(
            productId = 1,
            title = "Test Product",
            images = listOf("https://example.com/image1.jpg"),
            description = "Test Description",
            price = 99.99,
            discountedPrice = 89.99
        )

        // When
        favoriteRepository.addFavorite(originalEntity)

        // Then
        verify(mockFavoriteDao).insert(
            FavoriteEntity(
                productId = 1,
                title = "Test Product",
                images = listOf("https://example.com/image1.jpg"),
                description = "Test Description",
                price = 99.99,
                discountedPrice = 89.99
            )
        )
    }

    @Test
    fun `removeFavorite should call dao deleteById method`() = runTest {
        // Given
        val productId = 1

        // When
        favoriteRepository.removeFavorite(productId)

        // Then
        verify(mockFavoriteDao).deleteById(productId)
    }

    @Test
    fun `getFavorites should return flow of favorite entities`() = runTest {
        // Given
        val mockFavorites = listOf(
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

        whenever(mockFavoriteDao.getAll()).thenReturn(flowOf(mockFavorites))

        // When
        val result = favoriteRepository.getFavorites()

        // Then
        result.collect { favorites ->
            assertEquals(2, favorites.size)
            
            assertEquals(1, favorites[0].productId)
            assertEquals("Favorite Product 1", favorites[0].title)
            assertEquals(listOf("https://example.com/image1.jpg", "https://example.com/image2.jpg"), favorites[0].images)
            assertEquals("Description for favorite product 1", favorites[0].description)
            assertEquals(99.99, favorites[0].price, 0.01)
            assertEquals(89.99, favorites[0].discountedPrice, 0.01)

            assertEquals(2, favorites[1].productId)
            assertEquals("Favorite Product 2", favorites[1].title)
            assertEquals(listOf("https://example.com/image3.jpg"), favorites[1].images)
            assertEquals("Description for favorite product 2", favorites[1].description)
            assertEquals(149.99, favorites[1].price, 0.01)
            assertEquals(134.99, favorites[1].discountedPrice, 0.01)
        }

        verify(mockFavoriteDao).getAll()
    }

    @Test
    fun `getFavorites should return empty list when no favorites exist`() = runTest {
        // Given
        whenever(mockFavoriteDao.getAll()).thenReturn(flowOf(emptyList()))

        // When
        val result = favoriteRepository.getFavorites()

        // Then
        result.collect { favorites ->
            assertEquals(0, favorites.size)
        }

        verify(mockFavoriteDao).getAll()
    }

    @Test
    fun `isFavorite should return true when product exists in favorites`() = runTest {
        // Given
        val productId = 1
        whenever(mockFavoriteDao.countById(productId)).thenReturn(1)

        // When
        val result = favoriteRepository.isFavorite(productId)

        // Then
        assertTrue(result)
        verify(mockFavoriteDao).countById(productId)
    }

    @Test
    fun `isFavorite should return false when product does not exist in favorites`() = runTest {
        // Given
        val productId = 999
        whenever(mockFavoriteDao.countById(productId)).thenReturn(0)

        // When
        val result = favoriteRepository.isFavorite(productId)

        // Then
        assertFalse(result)
        verify(mockFavoriteDao).countById(productId)
    }

    @Test
    fun `isFavorite should return false when count is zero`() = runTest {
        // Given
        val productId = 1
        whenever(mockFavoriteDao.countById(productId)).thenReturn(0)

        // When
        val result = favoriteRepository.isFavorite(productId)

        // Then
        assertFalse(result)
        verify(mockFavoriteDao).countById(productId)
    }

    @Test
    fun `getFavorites should handle single favorite item`() = runTest {
        // Given
        val mockFavorite = listOf(
            FavoriteEntity(
                productId = 1,
                title = "Single Favorite",
                images = listOf("https://example.com/image.jpg"),
                description = "Single favorite description",
                price = 99.99,
                discountedPrice = 89.99
            )
        )

        whenever(mockFavoriteDao.getAll()).thenReturn(flowOf(mockFavorite))

        // When
        val result = favoriteRepository.getFavorites()

        // Then
        result.collect { favorites ->
            assertEquals(1, favorites.size)
            assertEquals(1, favorites[0].productId)
            assertEquals("Single Favorite", favorites[0].title)
            assertEquals(listOf("https://example.com/image.jpg"), favorites[0].images)
            assertEquals("Single favorite description", favorites[0].description)
            assertEquals(99.99, favorites[0].price, 0.01)
            assertEquals(89.99, favorites[0].discountedPrice, 0.01)
        }

        verify(mockFavoriteDao).getAll()
    }

    @Test
    fun `addFavorite should handle favorite with empty images list`() = runTest {
        // Given
        val favoriteEntity = FavoriteEntity(
            productId = 1,
            title = "Test Product",
            images = emptyList(),
            description = "Test Description",
            price = 99.99,
            discountedPrice = 89.99
        )

        // When
        favoriteRepository.addFavorite(favoriteEntity)

        // Then
        verify(mockFavoriteDao).insert(favoriteEntity)
    }

    @Test
    fun `addFavorite should handle favorite with multiple images`() = runTest {
        // Given
        val favoriteEntity = FavoriteEntity(
            productId = 1,
            title = "Test Product",
            images = listOf("image1.jpg", "image2.jpg", "image3.jpg"),
            description = "Test Description",
            price = 99.99,
            discountedPrice = 89.99
        )

        // When
        favoriteRepository.addFavorite(favoriteEntity)

        // Then
        verify(mockFavoriteDao).insert(favoriteEntity)
    }
} 