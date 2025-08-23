package com.example.cartapp

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import com.example.cartapp.databinding.ActivityMainBinding
import com.example.cartapp.presentation.cart.CartViewModel
import com.example.cartapp.presentation.common.navigation.NavigationRouter
import com.example.cartapp.presentation.home.HomeViewModel
import com.example.cartapp.util.LocaleHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val cartViewModel: CartViewModel by viewModels()
    private val homeViewModel: HomeViewModel by viewModels()
    
    @Inject
    lateinit var navigationRouter: NavigationRouter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val savedLanguage = LocaleHelper.getLanguage(this)
        LocaleHelper.setLocale(this, savedLanguage)

        // Allow fragments to control status bar colors
        WindowCompat.setDecorFitsSystemWindows(window, false)
        
        // Set default status bar color (will be overridden by fragments)
        window.statusBarColor = android.graphics.Color.TRANSPARENT

        window.setBackgroundDrawableResource(android.R.color.white)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNavigation()
        setupCartBadge()
    }

    private fun setupNavigation() {
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        binding.bottomNav.setOnItemSelectedListener { item ->
            // If Home tab is selected, refresh the data
            if (item.itemId == R.id.homeFragment) {
                homeViewModel.fetchProducts()
            }
            
            // Update status bar color based on selected fragment
            updateStatusBarColor(item.itemId)
            
            navigationRouter.handleBottomNavigation(navController, item.itemId)
        }

        binding.bottomNav.setOnItemReselectedListener { item ->
            // When the same tab is reselected, pop to root to refresh
            if (item.itemId == R.id.homeFragment) {
                homeViewModel.fetchProducts()
            }
            
            // Update status bar color based on reselected fragment
            updateStatusBarColor(item.itemId)
            
            navController.popBackStack(item.itemId, false)
        }
        
        // Set initial status bar color
        updateStatusBarColor(R.id.homeFragment)
    }
    
    private fun updateStatusBarColor(fragmentId: Int) {
        val statusBarColor = when (fragmentId) {
            R.id.homeFragment -> R.color.primary_blue      // 🔵 AppBar gradient start color
            R.id.favoriteFragment -> R.color.primary_blue  // 🔵 AppBar gradient start color
            R.id.cartFragment -> R.color.primary_blue      // 🔵 AppBar gradient start color
            else -> R.color.primary_blue
        }
        
        window.statusBarColor = ContextCompat.getColor(this, statusBarColor)
        
        // Set status bar icons to light (white) for dark backgrounds
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.isAppearanceLightStatusBars = false
    }

    private fun setupCartBadge() {
        val bottomNav = binding.bottomNav

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                cartViewModel.uiState.collect { uiState ->
                    val count = uiState.cartItems.sumOf { it.quantity }
                    val badge = bottomNav.getOrCreateBadge(R.id.cartFragment)
                    badge.isVisible = count > 0
                    badge.number = count
                }
            }
        }
    }
    
    fun hideBottomNavigation() {
        binding.bottomNav.visibility = android.view.View.GONE
    }
    
    fun showBottomNavigation() {
        binding.bottomNav.visibility = android.view.View.VISIBLE
    }
} 