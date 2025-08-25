package com.example.cartapp

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import com.example.cartapp.databinding.ActivityMainBinding
import com.example.cartapp.presentation.common.navigation.NavigationRouter
import com.example.cartapp.presentation.common.CartBadgeManager
import com.example.cartapp.presentation.common.ReselectCallback
import com.example.cartapp.presentation.common.StatusBarManager
import com.example.cartapp.util.AppLocaleManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var navigationRouter: NavigationRouter

    @Inject
    lateinit var cartBadgeManager: CartBadgeManager

    @Inject
    lateinit var statusBarManager: StatusBarManager

    private var currentReselectCallback: ReselectCallback? = null

    override fun attachBaseContext(newBase: Context) {
        AppLocaleManager.applySavedLocale(newBase)
        super.attachBaseContext(newBase)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        // Setup status bar centrally
        statusBarManager.setupStatusBar(this)

        window.setBackgroundDrawableResource(android.R.color.white)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNavigation()
        setupCartBadge()
    }

    private fun setupNavigation() {
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        binding.bottomNav.setOnItemSelectedListener { item ->
            updateStatusBarColor()
            navigationRouter.handleBottomNavigation(navController, item.itemId)
        }

        binding.bottomNav.setOnItemReselectedListener { item ->
            currentReselectCallback?.onReselect()
            updateStatusBarColor()
        }

        updateStatusBarColor()
    }

    private fun updateStatusBarColor() {
        // Use primary color for status bar
        statusBarManager.setPrimaryColorStatusBar(this)
    }

    private fun setupCartBadge() {
        cartBadgeManager.setupCartBadge(
            context = this,
            lifecycleOwner = this,
            bottomNav = binding.bottomNav
        )
    }

    fun hideBottomNavigation() {
        binding.bottomNav.visibility = View.GONE
    }

    fun showBottomNavigation() {
        binding.bottomNav.visibility = View.VISIBLE
    }

    fun setReselectCallback(callback: ReselectCallback?) {
        currentReselectCallback = callback
    }
} 