package com.example.cartapp.data.home.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.cartapp.data.home.local.entities.FavoriteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorites")
    fun getAll(): Flow<List<FavoriteEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(fav: FavoriteEntity)

    @Query("DELETE FROM favorites WHERE productId = :id")
    suspend fun deleteById(id: String)

    @Query("SELECT COUNT(*) FROM favorites WHERE productId = :id")
    suspend fun countById(id: String): Int
}
