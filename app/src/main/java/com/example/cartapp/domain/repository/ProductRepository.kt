package com.example.cartapp.domain.repository

import com.example.cartapp.domain.model.CartItem
import com.example.cartapp.domain.model.Favorite
import com.example.cartapp.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {

    fun getProductsFlow(
        limit: Int? = null,
        skip: Int? = null,
        sortBy: String? = null,
        order: String? = null,
        brand: String? = null,
        model: String? = null
    ): Flow<List<Product>>

    fun getProductByIdFlow(id: String): Flow<Product>
    fun searchProductsFlow(query: String): Flow<List<Product>>

    suspend fun addToCart(product: Product, quantity: Int = 1)
    suspend fun removeFromCart(productId: String)
    fun getCartItems(): Flow<List<CartItem>>
    suspend fun updateCartItemQuantity(productId: String, quantity: Int)
    suspend fun clearCart()

    suspend fun addToFavorites(product: Product)
    suspend fun removeFromFavorites(productId: String)
    fun getFavorites(): Flow<List<Favorite>>
    fun isFavorite(productId: String): Flow<Boolean>
    fun observeFavoriteIds(): Flow<Set<String>>
}