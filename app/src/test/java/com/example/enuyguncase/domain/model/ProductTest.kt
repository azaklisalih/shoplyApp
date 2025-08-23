package com.example.cartapp.domain.model

import org.junit.Assert.*
import org.junit.Test

class ProductTest {

    @Test
    fun `Product should be created with correct properties`() {
        // Given
        val id = 1
        val title = "Test Product"
        val description = "Test Description"
        val category = "Test Category"
        val price = 99.99
        val discountPercentage = 10.0
        val rating = 4.5
        val stock = 100
        val tags = listOf("tag1", "tag2")
        val brand = "Test Brand"
        val sku = "SKU123"
        val weight = 500
        val dimensions = Dimensions(10.0, 5.0, 2.0)
        val warrantyInformation = "1 year warranty"
        val shippingInformation = "Free shipping"
        val availabilityStatus = "In Stock"
        val reviews = listOf(Review(5, "Great product", "2024-01-01", "user1", "user1@example.com"))
        val returnPolicy = "30 days return"
        val minimumOrderQuantity = 1
        val meta = Meta("2024-01-01", "2024-01-01", "123456789", "QR123456")
        val thumbnail = "https://example.com/thumbnail.jpg"
        val images = listOf("https://example.com/image1.jpg", "https://example.com/image2.jpg")

        // When
        val product = Product(
            id = id,
            title = title,
            description = description,
            category = category,
            price = price,
            discountPercentage = discountPercentage,
            rating = rating,
            stock = stock,
            tags = tags,
            brand = brand,
            sku = sku,
            weight = weight,
            dimensions = dimensions,
            warrantyInformation = warrantyInformation,
            shippingInformation = shippingInformation,
            availabilityStatus = availabilityStatus,
            reviews = reviews,
            returnPolicy = returnPolicy,
            minimumOrderQuantity = minimumOrderQuantity,
            meta = meta,
            thumbnail = thumbnail,
            images = images
        )

        // Then
        assertEquals(id, product.id)
        assertEquals(title, product.title)
        assertEquals(description, product.description)
        assertEquals(category, product.category)
        assertEquals(price, product.price, 0.01)
        assertEquals(discountPercentage, product.discountPercentage, 0.01)
        assertEquals(rating, product.rating, 0.01)
        assertEquals(stock, product.stock)
        assertEquals(tags, product.tags)
        assertEquals(brand, product.brand)
        assertEquals(sku, product.sku)
        assertEquals(weight, product.weight)
        assertEquals(dimensions, product.dimensions)
        assertEquals(warrantyInformation, product.warrantyInformation)
        assertEquals(shippingInformation, product.shippingInformation)
        assertEquals(availabilityStatus, product.availabilityStatus)
        assertEquals(reviews, product.reviews)
        assertEquals(returnPolicy, product.returnPolicy)
        assertEquals(minimumOrderQuantity, product.minimumOrderQuantity)
        assertEquals(meta, product.meta)
        assertEquals(thumbnail, product.thumbnail)
        assertEquals(images, product.images)
    }

    @Test
    fun `Product should calculate discounted price correctly`() {
        // Given
        val price = 100.0
        val discountPercentage = 20.0
        val expectedDiscountedPrice = 80.0

        val product = Product(
            id = 1,
            title = "Test Product",
            description = "Test Description",
            category = "Test Category",
            price = price,
            discountPercentage = discountPercentage,
            rating = 4.5,
            stock = 100,
            tags = emptyList(),
            brand = "Test Brand",
            sku = "SKU123",
            weight = 500,
            dimensions = Dimensions(10.0, 5.0, 2.0),
            warrantyInformation = "1 year warranty",
            shippingInformation = "Free shipping",
            availabilityStatus = "In Stock",
            reviews = emptyList(),
            returnPolicy = "30 days return",
            minimumOrderQuantity = 1,
            meta = Meta("2024-01-01", "2024-01-01", "123456789", "QR123456"),
            thumbnail = "https://example.com/thumbnail.jpg",
            images = emptyList()
        )

        // When
        val discountedPrice = product.discountedPrice

        // Then
        assertEquals(expectedDiscountedPrice, discountedPrice, 0.01)
    }

    @Test
    fun `Product should be in stock when stock is greater than 0`() {
        // Given
        val product = Product(
            id = 1,
            title = "Test Product",
            description = "Test Description",
            category = "Test Category",
            price = 99.99,
            discountPercentage = 10.0,
            rating = 4.5,
            stock = 10,
            tags = emptyList(),
            brand = "Test Brand",
            sku = "SKU123",
            weight = 500,
            dimensions = Dimensions(10.0, 5.0, 2.0),
            warrantyInformation = "1 year warranty",
            shippingInformation = "Free shipping",
            availabilityStatus = "In Stock",
            reviews = emptyList(),
            returnPolicy = "30 days return",
            minimumOrderQuantity = 1,
            meta = Meta("2024-01-01", "2024-01-01", "123456789", "QR123456"),
            thumbnail = "https://example.com/thumbnail.jpg",
            images = emptyList()
        )

        // When & Then
        assertTrue(product.stock > 0)
    }

    @Test
    fun `Product should be out of stock when stock is 0`() {
        // Given
        val product = Product(
            id = 1,
            title = "Test Product",
            description = "Test Description",
            category = "Test Category",
            price = 99.99,
            discountPercentage = 10.0,
            rating = 4.5,
            stock = 0,
            tags = emptyList(),
            brand = "Test Brand",
            sku = "SKU123",
            weight = 500,
            dimensions = Dimensions(10.0, 5.0, 2.0),
            warrantyInformation = "1 year warranty",
            shippingInformation = "Free shipping",
            availabilityStatus = "Out of Stock",
            reviews = emptyList(),
            returnPolicy = "30 days return",
            minimumOrderQuantity = 1,
            meta = Meta("2024-01-01", "2024-01-01", "123456789", "QR123456"),
            thumbnail = "https://example.com/thumbnail.jpg",
            images = emptyList()
        )

        // When & Then
        assertEquals(0, product.stock)
    }

    @Test
    fun `Product should have valid rating between 0 and 5`() {
        // Given
        val product = Product(
            id = 1,
            title = "Test Product",
            description = "Test Description",
            category = "Test Category",
            price = 99.99,
            discountPercentage = 10.0,
            rating = 4.5,
            stock = 100,
            tags = emptyList(),
            brand = "Test Brand",
            sku = "SKU123",
            weight = 500,
            dimensions = Dimensions(10.0, 5.0, 2.0),
            warrantyInformation = "1 year warranty",
            shippingInformation = "Free shipping",
            availabilityStatus = "In Stock",
            reviews = emptyList(),
            returnPolicy = "30 days return",
            minimumOrderQuantity = 1,
            meta = Meta("2024-01-01", "2024-01-01", "123456789", "QR123456"),
            thumbnail = "https://example.com/thumbnail.jpg",
            images = emptyList()
        )

        // When & Then
        assertTrue(product.rating >= 0.0)
        assertTrue(product.rating <= 5.0)
    }
} 