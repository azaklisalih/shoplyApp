package com.example.cartapp.presentation.home

import com.example.cartapp.domain.model.Dimensions
import com.example.cartapp.domain.model.Meta
import com.example.cartapp.domain.model.Product
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class HomeUIStateTest {

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

    @Test
    fun `HomeUIState should be created with default values`() {
        val state = HomeUIState()

        assertTrue(state.products.isEmpty())
        assertFalse(state.isLoading)
        assertNull(state.error)
        assertEquals(0, state.total)
        assertEquals(0, state.page)
        assertEquals(30, state.pageSize)
        assertNull(state.selectedCategory)
        assertNull(state.selectedSortBy)
        assertNull(state.selectedSortOrder)
    }

    @Test
    fun `HomeUIState should be created with custom values`() {
        val products = listOf(testProduct)
        val state = HomeUIState(
            products = products,
            isLoading = true,
            error = "Test error",
            total = 100,
            page = 2,
            pageSize = 20,
            selectedCategory = "smartphones",
            selectedSortBy = "price",
            selectedSortOrder = "asc"
        )

        assertEquals(products, state.products)
        assertTrue(state.isLoading)
        assertEquals("Test error", state.error)
        assertEquals(100, state.total)
        assertEquals(2, state.page)
        assertEquals(20, state.pageSize)
        assertEquals("smartphones", state.selectedCategory)
        assertEquals("price", state.selectedSortBy)
        assertEquals("asc", state.selectedSortOrder)
    }

    @Test
    fun `HomeUIState copy should create new instance with updated values`() {
        val originalState = HomeUIState()
        val updatedState = originalState.copy(
            products = listOf(testProduct),
            isLoading = true,
            error = "Updated error"
        )

        assertEquals(listOf(testProduct), updatedState.products)
        assertTrue(updatedState.isLoading)
        assertEquals("Updated error", updatedState.error)
        
        // Original state should remain unchanged
        assertTrue(originalState.products.isEmpty())
        assertFalse(originalState.isLoading)
        assertNull(originalState.error)
    }

    @Test
    fun `HomeUIState should handle empty products list`() {
        val state = HomeUIState(products = emptyList())

        assertTrue(state.products.isEmpty())
        assertEquals(0, state.products.size)
    }

    @Test
    fun `HomeUIState should handle single product`() {
        val state = HomeUIState(products = listOf(testProduct))

        assertEquals(1, state.products.size)
        assertEquals(testProduct, state.products[0])
    }

    @Test
    fun `HomeUIState should handle multiple products`() {
        val product2 = testProduct.copy(id = 2, title = "Test Product 2")
        val product3 = testProduct.copy(id = 3, title = "Test Product 3")
        val products = listOf(testProduct, product2, product3)
        val state = HomeUIState(products = products)

        assertEquals(3, state.products.size)
        assertEquals(testProduct, state.products[0])
        assertEquals(product2, state.products[1])
        assertEquals(product3, state.products[2])
    }

    @Test
    fun `HomeUIState should handle zero total`() {
        val state = HomeUIState(total = 0)

        assertEquals(0, state.total)
    }

    @Test
    fun `HomeUIState should handle large total`() {
        val state = HomeUIState(total = 999999)

        assertEquals(999999, state.total)
    }

    @Test
    fun `HomeUIState should handle zero page`() {
        val state = HomeUIState(page = 0)

        assertEquals(0, state.page)
    }

    @Test
    fun `HomeUIState should handle large page number`() {
        val state = HomeUIState(page = 999)

        assertEquals(999, state.page)
    }

    @Test
    fun `HomeUIState should handle custom page size`() {
        val state = HomeUIState(pageSize = 50)

        assertEquals(50, state.pageSize)
    }

    @Test
    fun `HomeUIState should handle null category`() {
        val state = HomeUIState(selectedCategory = null)

        assertNull(state.selectedCategory)
    }

    @Test
    fun `HomeUIState should handle empty category string`() {
        val state = HomeUIState(selectedCategory = "")

        assertEquals("", state.selectedCategory)
    }

    @Test
    fun `HomeUIState should handle category with special characters`() {
        val category = "smartphones & tablets"
        val state = HomeUIState(selectedCategory = category)

        assertEquals(category, state.selectedCategory)
    }

    @Test
    fun `HomeUIState should handle null sort parameters`() {
        val state = HomeUIState(selectedSortBy = null, selectedSortOrder = null)

        assertNull(state.selectedSortBy)
        assertNull(state.selectedSortOrder)
    }

    @Test
    fun `HomeUIState should handle sort parameters`() {
        val sortBy = "price"
        val order = "desc"
        val state = HomeUIState(selectedSortBy = sortBy, selectedSortOrder = order)

        assertEquals(sortBy, state.selectedSortBy)
        assertEquals(order, state.selectedSortOrder)
    }

    @Test
    fun `HomeUIState should handle error message`() {
        val errorMessage = "Network error occurred"
        val state = HomeUIState(error = errorMessage)

        assertEquals(errorMessage, state.error)
    }

    @Test
    fun `HomeUIState should handle empty error message`() {
        val state = HomeUIState(error = "")

        assertEquals("", state.error)
    }

    @Test
    fun `HomeUIState should handle loading state`() {
        val state = HomeUIState(isLoading = true)

        assertTrue(state.isLoading)
    }

    @Test
    fun `HomeUIState should handle not loading state`() {
        val state = HomeUIState(isLoading = false)

        assertFalse(state.isLoading)
    }
} 