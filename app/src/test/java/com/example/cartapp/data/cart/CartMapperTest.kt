package com.example.cartapp.data.cart

import com.example.cartapp.data.cart.local.entities.CartItemEntity
import com.example.cartapp.data.cart.mapper.toDomain
import com.example.cartapp.data.cart.mapper.toEntity
import com.example.cartapp.data.cart.mapper.toDomainList
import com.example.cartapp.domain.model.CartItem
import org.junit.Assert.assertEquals
import org.junit.Test

class CartMapperTest {

    @Test
    fun `map CartItemEntity to CartItem`() {
        // Given
        val entity = CartItemEntity(
            productId = "test-id",
            name = "Test Product",
            price = "100.0",
            image = "test-image.jpg",
            description = "Test description",
            model = "Test Model",
            brand = "Test Brand",
            quantity = 2
        )

        // When
        val result = entity.toDomain()

        // Then
        assertEquals("test-id", result.productId)
        assertEquals("Test Product", result.name)
        assertEquals("100.0", result.price)
        assertEquals("test-image.jpg", result.image)
        assertEquals("Test description", result.description)
        assertEquals("Test Model", result.model)
        assertEquals("Test Brand", result.brand)
        assertEquals(2, result.quantity)
    }

    @Test
    fun `map CartItem to CartItemEntity`() {
        // Given
        val domain = CartItem(
            productId = "test-id",
            name = "Test Product",
            price = "100.0",
            image = "test-image.jpg",
            description = "Test description",
            model = "Test Model",
            brand = "Test Brand",
            quantity = 2
        )

        // When
        val result = domain.toEntity()

        // Then
        assertEquals("test-id", result.productId)
        assertEquals("Test Product", result.name)
        assertEquals("100.0", result.price)
        assertEquals("test-image.jpg", result.image)
        assertEquals("Test description", result.description)
        assertEquals("Test Model", result.model)
        assertEquals("Test Brand", result.brand)
        assertEquals(2, result.quantity)
    }

    @Test
    fun `map list of CartItemEntity to list of CartItem`() {
        // Given
        val entities = listOf(
            CartItemEntity(
                productId = "test-id-1",
                name = "Test Product 1",
                price = "100.0",
                image = "test-image-1.jpg",
                description = "Test description 1",
                model = "Test Model 1",
                brand = "Test Brand 1",
                quantity = 1
            ),
            CartItemEntity(
                productId = "test-id-2",
                name = "Test Product 2",
                price = "200.0",
                image = "test-image-2.jpg",
                description = "Test description 2",
                model = "Test Model 2",
                brand = "Test Brand 2",
                quantity = 3
            )
        )

        // When
        val result = entities.toDomainList()

        // Then
        assertEquals(2, result.size)
        assertEquals("test-id-1", result[0].productId)
        assertEquals("test-id-2", result[1].productId)
        assertEquals(1, result[0].quantity)
        assertEquals(3, result[1].quantity)
    }
} 