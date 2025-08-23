package com.example.cartapp.di

import android.content.Context
import androidx.room.Room
import com.example.cartapp.data.local.AppDatabase
import com.example.cartapp.data.cart.local.dao.CartDao
import com.example.cartapp.data.favorite.local.dao.FavoriteDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext ctx: Context): AppDatabase {
        return Room.databaseBuilder(
            ctx,
            AppDatabase::class.java,
            "enuyguncase-db"
        ).build()
    }

    @Provides
    fun provideFavoritesDao(db: AppDatabase): FavoriteDao {
        return db.favoriteDao()
    }

    @Provides
    fun provideCartDao(db: AppDatabase): CartDao {
        return db.cartDao()
    }
}