package com.example.cartapp.presentation.common

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.Lifecycle
import com.example.cartapp.domain.usecase.cart.GetCartItemsUseCase
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CartBadgeManager @Inject constructor(
    private val getCartItemsUseCase: GetCartItemsUseCase
) {
    
    fun setupCartBadge(
        context: Context,
        lifecycleOwner: LifecycleOwner,
        bottomNav: BottomNavigationView
    ) {
        lifecycleOwner.lifecycleScope.launch {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                getCartItemsUseCase().collect { cartItems ->
                    val count = cartItems.sumOf { it.quantity }
                    val badge = bottomNav.getOrCreateBadge(com.example.cartapp.R.id.cartFragment)
                    badge.isVisible = count > 0
                    badge.number = count
                }
            }
        }
    }
} 