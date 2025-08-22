package com.example.cartapp.presentation.common.navigation

import androidx.navigation.NavController

/**
 * Navigation Router Interface
 * Follows Clean Architecture principles by defining navigation contracts
 */
interface NavigationRouter {

    fun handleBottomNavigation(navController: NavController, itemId: Int): Boolean

    fun navigateToProductDetail(navController: NavController, productId: Int)

    fun navigateBack(navController: NavController): Boolean
} 