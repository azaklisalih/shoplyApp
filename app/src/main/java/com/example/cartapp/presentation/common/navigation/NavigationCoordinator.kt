package com.example.cartapp.presentation.common.navigation

import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.navOptions
import com.example.cartapp.R
import javax.inject.Inject

class NavigationCoordinator @Inject constructor() : NavigationRouter {

    override fun handleBottomNavigation(navController: NavController, itemId: Int): Boolean {
        if (navController.graph.findNode(itemId) == null) return false

        val alreadyOnTab = navController.currentDestination
            ?.hierarchy
            ?.any { it.id == itemId } == true
        
        if (alreadyOnTab) {
            // If already on the tab, pop to root of that tab to refresh
            navController.popBackStack(itemId, false)
            return true
        }

        val options = navOptions {
            launchSingleTop = true
            restoreState = false
            popUpTo(navController.graph.startDestinationId) {
                inclusive = false
                saveState = false
            }
        }

        return runCatching {
            navController.navigate(itemId, null, options)
            true
        }.getOrDefault(false)
    }

    override fun navigateToProductDetail(navController: NavController, productId: Int) {
        val destId = R.id.productDetailFragment
        if (navController.graph.findNode(destId) != null) {
            navController.navigate(destId, bundleOf("productId" to productId))
        }
    }

    override fun navigateBack(navController: NavController): Boolean =
        navController.popBackStack()
}
