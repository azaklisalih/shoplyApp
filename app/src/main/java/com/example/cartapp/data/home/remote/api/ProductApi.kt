package com.example.cartapp.data.home.remote.api

import com.example.cartapp.domain.model.Product
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductApi {

    @GET("products")
    suspend fun getProducts(
        @Query("page") page: Int? = null,
        @Query("limit") limit: Int? = null,
        @Query("brand") brand: String? = null,
        @Query("model") model: String? = null,
        @Query("sortBy") sortBy: String? = null,
        @Query("order") order: String? = null
    ): Response<List<Product>>

    @GET("products/{id}")
    suspend fun getProductById(
        @Path("id") id: String
    ): Response<Product>

    @GET("products")
    suspend fun searchProducts(
        @Query("search") query: String
    ): Response<List<Product>>

}