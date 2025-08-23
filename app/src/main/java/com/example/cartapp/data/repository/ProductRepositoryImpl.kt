package com.example.cartapp.data.repository

import com.example.cartapp.data.cart.local.dao.CartDao
import com.example.cartapp.data.favorite.local.dao.FavoriteDao
import com.example.cartapp.data.cart.local.entities.CartItemEntity
import com.example.cartapp.data.favorite.local.entities.FavoriteEntity
import com.example.cartapp.data.cart.mapper.toDomain
import com.example.cartapp.data.cart.mapper.toDomainList
import com.example.cartapp.data.cart.mapper.toEntity
import com.example.cartapp.data.favorite.mapper.toEntity
import com.example.cartapp.data.favorite.mapper.toDomain
import com.example.cartapp.data.home.mapper.toDomain
import com.example.cartapp.data.home.mapper.toDomainList
import com.example.cartapp.data.home.remote.api.ProductApi
import com.example.cartapp.domain.model.CartItem
import com.example.cartapp.domain.model.Favorite
import com.example.cartapp.domain.model.Product
import com.example.cartapp.domain.repository.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepositoryImpl @Inject constructor(
    private val api: ProductApi,
    private val cartDao: CartDao,
    private val favoriteDao: FavoriteDao
) : ProductRepository {

    override fun getProductsFlow(
        limit: Int?,
        skip: Int?,
        sortBy: String?,
        order: String?,
        brand: String?,
        model: String?
    ): Flow<List<Product>> = flow {
        // Calculate page from skip
        val page = if (skip != null && limit != null && limit > 0) {
            (skip / limit) + 1
        } else {
            null
        }
        
        val response = api.getProducts(
            page = page, 
            limit = limit,
            brand = brand,
            model = model,
            sortBy = sortBy,
            order = order
        )
        when (response.code()) {
            in 200..299 -> {
                val body = response.body()
                    ?: throw Exception("Empty response")
                emit(body.toDomainList())
            }
            in 400..499 -> {
                throw Exception("Client error: ${response.message()}")
            }
            in 500..599 -> {
                throw Exception("Server error: ${response.message()}")
            }
            else -> {
                throw Exception("Unexpected HTTP: ${response.code()}")
            }
        }
    }.flowOn(Dispatchers.IO)

    override fun getProductByIdFlow(id: String): Flow<Product> = flow {
        val response = api.getProductById(id)
        when (response.code()) {
            in 200..299 -> {
                val body = response.body() ?: throw Exception("Empty response")
                emit(body.toDomain())
            }
            in 400..499 -> throw Exception("Client error: ${response.message()}")
            in 500..599 -> throw Exception("Server error: ${response.message()}")
            else -> throw Exception("Unexpected HTTP: ${response.code()}")
        }
    }.flowOn(Dispatchers.IO)

    override fun searchProductsFlow(query: String): Flow<List<Product>> = flow {
        val response = api.searchProducts(query)
        when (response.code()) {
            in 200..299 -> {
                val body = response.body() ?: throw Exception("Empty response")
                emit(body.toDomainList())
            }
            in 400..499 -> throw Exception("Client error: ${response.message()}")
            in 500..599 -> throw Exception("Server error: ${response.message()}")
            else -> throw Exception("Unexpected HTTP: ${response.code()}")
        }
    }.flowOn(Dispatchers.IO)

    // Cart Operations
    override suspend fun addToCart(product: Product, quantity: Int) {
        val cartItem = CartItem(
            productId = product.id,
            name = product.name,
            price = product.price,
            image = product.image,
            description = product.description,
            model = product.model,
            brand = product.brand,
            quantity = quantity
        ).toEntity()
        cartDao.upsert(cartItem)
    }

    override suspend fun removeFromCart(productId: String) {
        cartDao.deleteByProductId(productId)
    }

    override fun getCartItems(): Flow<List<CartItem>> = 
        cartDao.getAllCartItems().map { entities -> entities.toDomainList() }

    override suspend fun updateCartItemQuantity(productId: String, quantity: Int) {
        if (quantity <= 0) {
            cartDao.deleteByProductId(productId)
        } else {
            cartDao.updateQuantity(productId, quantity)
        }
    }

    // Favorite Operations
    override suspend fun addToFavorites(product: Product) {
        val favoriteEntity = Favorite(
            productId = product.id,
            name = product.name,
            price = product.price,
            image = product.image,
            description = product.description,
            model = product.model,
            brand = product.brand,
            createdAt = product.createdAt
        ).toEntity()
        favoriteDao.insert(favoriteEntity)
    }

    override suspend fun removeFromFavorites(productId: String) {
        favoriteDao.deleteById(productId)
    }

    override fun getFavorites(): Flow<List<Favorite>> = 
        favoriteDao.getAll().map { entities -> entities.map { it.toDomain() } }

    override fun isFavorite(productId: String): Flow<Boolean> = flow {
        val count = favoriteDao.countById(productId)
        emit(count > 0)
    }.flowOn(Dispatchers.IO)

    override fun observeFavoriteIds(): Flow<Set<String>> = 
        favoriteDao.getAll().map { favorites ->
            favorites.map { it.productId }.toSet()
        }.flowOn(Dispatchers.IO)

}
