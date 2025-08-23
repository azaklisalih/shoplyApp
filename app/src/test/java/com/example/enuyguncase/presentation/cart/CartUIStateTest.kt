package com.example.cartapp.presentation.cart

import com.example.cartapp.domain.model.CartItem
import com.example.cartapp.domain.model.Dimensions
import com.example.cartapp.domain.model.Meta
import com.example.cartapp.domain.model.Product
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class CartUIStateTest {

    private val testProduct = Product(
        id = 1,
        title = "Test Product",
        description = "Test Description",
        price = 100.0,
        discountPercentage = 10.0,
        rating = 4.5,
        stock = 50,
        brand = "Test Brand",
        category = "smartphones",
        thumbnail = "thumbnail.jpg",
        images = listOf("image1.jpg"),
        tags = listOf("test"),
        sku = "TEST123",
        weight = 150,
        dimensions = Dimensions(7.0, 14.0, 0.7),
        warrantyInformation = "1 year",
        shippingInformation = "Free shipping",
        availabilityStatus = "In Stock",
        reviews = emptyList(),
        returnPolicy = "30 days",
        minimumOrderQuantity = 1,
        meta = Meta("2023-01-01", "2023-01-01", "123456", "QR123")
    )

    private val testCartItem = CartItem(
        productId = testProduct.id,
        title = testProduct.title,
        price = testProduct.price,
        discountPrice = testProduct.discountedPrice,
        thumbnail = testProduct.thumbnail,
        quantity = 2
    )

    @Test
    fun `CartUIState should be created with default values`() {
        val state = CartUIState()

        assertTrue(state.cartItems.isEmpty())
        assertTrue(state.isLoading)
        assertNull(state.error)
    }

    @Test
    fun `CartUIState should be created with custom values`() {
        val cartItems = listOf(testCartItem)
        val state = CartUIState(
            cartItems = cartItems,
            isLoading = false,
            error = "Test error"
        )

        assertEquals(cartItems, state.cartItems)
        assertFalse(state.isLoading)
        assertEquals("Test error", state.error)
    }

    @Test
    fun `CartUIState copy should create new instance with updated values`() {
        val originalState = CartUIState()
        val updatedState = originalState.copy(
            cartItems = listOf(testCartItem),
            isLoading = false,
            error = "Updated error"
        )

        assertEquals(listOf(testCartItem), updatedState.cartItems)
        assertFalse(updatedState.isLoading)
        assertEquals("Updated error", updatedState.error)
        
        // Original state should remain unchanged
        assertTrue(originalState.cartItems.isEmpty())
        assertTrue(originalState.isLoading)
        assertNull(originalState.error)
    }

    @Test
    fun `CartUIState should handle empty cart items list`() {
        val state = CartUIState(cartItems = emptyList())

        assertTrue(state.cartItems.isEmpty())
        assertEquals(0, state.cartItems.size)
    }

    @Test
    fun `CartUIState should handle single cart item`() {
        val state = CartUIState(cartItems = listOf(testCartItem))

        assertEquals(1, state.cartItems.size)
        assertEquals(testCartItem, state.cartItems[0])
    }

    @Test
    fun `CartUIState should handle multiple cart items`() {
        val cartItem2 = testCartItem.copy(quantity = 3)
        val cartItem3 = testCartItem.copy(quantity = 1)
        val cartItems = listOf(testCartItem, cartItem2, cartItem3)
        val state = CartUIState(cartItems = cartItems)

        assertEquals(3, state.cartItems.size)
        assertEquals(testCartItem, state.cartItems[0])
        assertEquals(cartItem2, state.cartItems[1])
        assertEquals(cartItem3, state.cartItems[2])
    }

    @Test
    fun `CartUIState should handle loading state`() {
        val state = CartUIState(isLoading = true)

        assertTrue(state.isLoading)
    }

    @Test
    fun `CartUIState should handle not loading state`() {
        val state = CartUIState(isLoading = false)

        assertFalse(state.isLoading)
    }

    @Test
    fun `CartUIState should handle error message`() {
        val errorMessage = "Database error occurred"
        val state = CartUIState(error = errorMessage)

        assertEquals(errorMessage, state.error)
    }

    @Test
    fun `CartUIState should handle empty error message`() {
        val state = CartUIState(error = "")

        assertEquals("", state.error)
    }

    @Test
    fun `CartUIState should handle null error`() {
        val state = CartUIState(error = null)

        assertNull(state.error)
    }

    @Test
    fun `CartUIState should handle cart items with zero quantity`() {
        val cartItemWithZeroQuantity = testCartItem.copy(quantity = 0)
        val state = CartUIState(cartItems = listOf(cartItemWithZeroQuantity))

        assertEquals(0, state.cartItems[0].quantity)
    }

    @Test
    fun `CartUIState should handle cart items with large quantity`() {
        val cartItemWithLargeQuantity = testCartItem.copy(quantity = 999)
        val state = CartUIState(cartItems = listOf(cartItemWithLargeQuantity))

        assertEquals(999, state.cartItems[0].quantity)
    }

    @Test
    fun `CartUIState should handle cart items with different products`() {
        val product2 = testProduct.copy(id = 2, title = "Test Product 2")
        val cartItem2 = CartItem(
            productId = product2.id,
            title = product2.title,
            price = product2.price,
            discountPrice = product2.discountedPrice,
            thumbnail = product2.thumbnail,
            quantity = 1
        )
        val cartItems = listOf(testCartItem, cartItem2)
        val state = CartUIState(cartItems = cartItems)

        assertEquals(2, state.cartItems.size)
        assertEquals(testProduct.id, state.cartItems[0].productId)
        assertEquals(product2.id, state.cartItems[1].productId)
    }

    @Test
    fun `CartUIState should handle special characters in error message`() {
        val errorMessage = "Error @#$%^&*() occurred"
        val state = CartUIState(error = errorMessage)

        assertEquals(errorMessage, state.error)
    }

    @Test
    fun `CartUIState should handle long error message`() {
        val longErrorMessage = "This is a very long error message that contains many characters and should be handled properly by the CartUIState class without any issues"
        val state = CartUIState(error = longErrorMessage)

        assertEquals(longErrorMessage, state.error)
    }
} 