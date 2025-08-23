package com.example.cartapp.presentation.favorite

import com.example.cartapp.domain.model.Favorite
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class FavoriteUIStateTest {

    private val testFavorite = Favorite(
        productId = 1,
        title = "Test Product",
        description = "Test Description",
        price = 100.0,
        discountedPrice = 90.0,

        images = listOf("image1.jpg", "image2.jpg")
    )

    @Test
    fun `FavoriteUIState should be created with default values`() {
        val state = FavoriteUIState()

        assertTrue(state.favorites.isEmpty())
        assertTrue(state.isLoading)
        assertNull(state.error)
    }

    @Test
    fun `FavoriteUIState should be created with custom values`() {
        val favorites = listOf(testFavorite)
        val state = FavoriteUIState(
            favorites = favorites,
            isLoading = false,
            error = "Test error"
        )

        assertEquals(favorites, state.favorites)
        assertFalse(state.isLoading)
        assertEquals("Test error", state.error)
    }

    @Test
    fun `FavoriteUIState copy should create new instance with updated values`() {
        val originalState = FavoriteUIState()
        val updatedState = originalState.copy(
            favorites = listOf(testFavorite),
            isLoading = false,
            error = "Updated error"
        )

        assertEquals(listOf(testFavorite), updatedState.favorites)
        assertFalse(updatedState.isLoading)
        assertEquals("Updated error", updatedState.error)
        
        // Original state should remain unchanged
        assertTrue(originalState.favorites.isEmpty())
        assertTrue(originalState.isLoading)
        assertNull(originalState.error)
    }

    @Test
    fun `FavoriteUIState should handle empty favorites list`() {
        val state = FavoriteUIState(favorites = emptyList())

        assertTrue(state.favorites.isEmpty())
        assertEquals(0, state.favorites.size)
    }

    @Test
    fun `FavoriteUIState should handle single favorite`() {
        val state = FavoriteUIState(favorites = listOf(testFavorite))

        assertEquals(1, state.favorites.size)
        assertEquals(testFavorite, state.favorites[0])
    }

    @Test
    fun `FavoriteUIState should handle multiple favorites`() {
        val favorite2 = testFavorite.copy(productId = 2, title = "Test Product 2")
        val favorite3 = testFavorite.copy(productId = 3, title = "Test Product 3")
        val favorites = listOf(testFavorite, favorite2, favorite3)
        val state = FavoriteUIState(favorites = favorites)

        assertEquals(3, state.favorites.size)
        assertEquals(testFavorite, state.favorites[0])
        assertEquals(favorite2, state.favorites[1])
        assertEquals(favorite3, state.favorites[2])
    }

    @Test
    fun `FavoriteUIState should handle loading state`() {
        val state = FavoriteUIState(isLoading = true)

        assertTrue(state.isLoading)
    }

    @Test
    fun `FavoriteUIState should handle not loading state`() {
        val state = FavoriteUIState(isLoading = false)

        assertFalse(state.isLoading)
    }

    @Test
    fun `FavoriteUIState should handle error message`() {
        val errorMessage = "Database error occurred"
        val state = FavoriteUIState(error = errorMessage)

        assertEquals(errorMessage, state.error)
    }

    @Test
    fun `FavoriteUIState should handle empty error message`() {
        val state = FavoriteUIState(error = "")

        assertEquals("", state.error)
    }

    @Test
    fun `FavoriteUIState should handle null error`() {
        val state = FavoriteUIState(error = null)

        assertNull(state.error)
    }

    @Test
    fun `FavoriteUIState should handle favorites with zero price`() {
        val favoriteWithZeroPrice = testFavorite.copy(price = 0.0, discountedPrice = 0.0)
        val state = FavoriteUIState(favorites = listOf(favoriteWithZeroPrice))

        assertEquals(0.0, state.favorites[0].price, 0.01)
        assertEquals(0.0, state.favorites[0].discountedPrice, 0.01)
    }

    @Test
    fun `FavoriteUIState should handle favorites with large price`() {
        val favoriteWithLargePrice = testFavorite.copy(price = 999999.99, discountedPrice = 899999.99)
        val state = FavoriteUIState(favorites = listOf(favoriteWithLargePrice))

        assertEquals(999999.99, state.favorites[0].price, 0.01)
        assertEquals(899999.99, state.favorites[0].discountedPrice, 0.01)
    }

    @Test
    fun `FavoriteUIState should handle favorites with empty images list`() {
        val favoriteWithEmptyImages = testFavorite.copy(images = emptyList())
        val state = FavoriteUIState(favorites = listOf(favoriteWithEmptyImages))

        assertTrue(state.favorites[0].images.isEmpty())
    }

    @Test
    fun `FavoriteUIState should handle favorites with single image`() {
        val favoriteWithSingleImage = testFavorite.copy(images = listOf("single.jpg"))
        val state = FavoriteUIState(favorites = listOf(favoriteWithSingleImage))

        assertEquals(1, state.favorites[0].images.size)
        assertEquals("single.jpg", state.favorites[0].images[0])
    }

    @Test
    fun `FavoriteUIState should handle favorites with multiple images`() {
        val favoriteWithMultipleImages = testFavorite.copy(images = listOf("img1.jpg", "img2.jpg", "img3.jpg"))
        val state = FavoriteUIState(favorites = listOf(favoriteWithMultipleImages))

        assertEquals(3, state.favorites[0].images.size)
        assertEquals("img1.jpg", state.favorites[0].images[0])
        assertEquals("img2.jpg", state.favorites[0].images[1])
        assertEquals("img3.jpg", state.favorites[0].images[2])
    }

    @Test
    fun `FavoriteUIState should handle favorites with special characters in title`() {
        val favoriteWithSpecialChars = testFavorite.copy(title = "Test Product @#$%^&*()")
        val state = FavoriteUIState(favorites = listOf(favoriteWithSpecialChars))

        assertEquals("Test Product @#$%^&*()", state.favorites[0].title)
    }

    @Test
    fun `FavoriteUIState should handle favorites with long description`() {
        val longDescription = "This is a very long description that contains many characters and should be handled properly by the FavoriteUIState class without any issues"
        val favoriteWithLongDescription = testFavorite.copy(description = longDescription)
        val state = FavoriteUIState(favorites = listOf(favoriteWithLongDescription))

        assertEquals(longDescription, state.favorites[0].description)
    }

    @Test
    fun `FavoriteUIState should handle favorites with empty description`() {
        val favoriteWithEmptyDescription = testFavorite.copy(description = "")
        val state = FavoriteUIState(favorites = listOf(favoriteWithEmptyDescription))

        assertEquals("", state.favorites[0].description)
    }

    @Test
    fun `FavoriteUIState should handle favorites with large product ID`() {
        val favoriteWithLargeId = testFavorite.copy(productId = 999999)
        val state = FavoriteUIState(favorites = listOf(favoriteWithLargeId))

        assertEquals(999999, state.favorites[0].productId)
    }

    @Test
    fun `FavoriteUIState should handle favorites with zero product ID`() {
        val favoriteWithZeroId = testFavorite.copy(productId = 0)
        val state = FavoriteUIState(favorites = listOf(favoriteWithZeroId))

        assertEquals(0, state.favorites[0].productId)
    }

    @Test
    fun `FavoriteUIState should handle special characters in error message`() {
        val errorMessage = "Error @#$%^&*() occurred"
        val state = FavoriteUIState(error = errorMessage)

        assertEquals(errorMessage, state.error)
    }

    @Test
    fun `FavoriteUIState should handle long error message`() {
        val longErrorMessage = "This is a very long error message that contains many characters and should be handled properly by the FavoriteUIState class without any issues"
        val state = FavoriteUIState(error = longErrorMessage)

        assertEquals(longErrorMessage, state.error)
    }
} 