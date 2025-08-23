package com.example.cartapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.cartapp.data.common.converters.Converters
import com.example.cartapp.data.cart.local.dao.CartDao
import com.example.cartapp.data.favorite.local.dao.FavoriteDao
import com.example.cartapp.data.cart.local.entities.CartItemEntity
import com.example.cartapp.data.favorite.local.entities.FavoriteEntity

@Database(entities = [FavoriteEntity::class, CartItemEntity::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
    abstract fun cartDao(): CartDao

}