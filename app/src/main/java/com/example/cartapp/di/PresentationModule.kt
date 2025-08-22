package com.example.cartapp.di

import com.example.cartapp.presentation.common.navigation.NavigationCoordinator
import com.example.cartapp.presentation.common.navigation.NavigationRouter
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PresentationModule {

    @Binds
    @Singleton
    abstract fun bindNavigationRouter(
        navigationCoordinator: NavigationCoordinator
    ): NavigationRouter
} 