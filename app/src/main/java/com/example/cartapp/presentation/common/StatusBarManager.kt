package com.example.cartapp.presentation.common

import android.app.Activity
import androidx.core.view.WindowCompat
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StatusBarManager @Inject constructor() {
    
    fun setupStatusBar(activity: Activity) {
        val windowInsetsController = WindowCompat.getInsetsController(activity.window, activity.window.decorView)
        windowInsetsController.isAppearanceLightStatusBars = false
    }
    
    fun setLightStatusBar(activity: Activity, isLight: Boolean) {
        val windowInsetsController = WindowCompat.getInsetsController(activity.window, activity.window.decorView)
        windowInsetsController.isAppearanceLightStatusBars = isLight
    }
    
    fun setPrimaryColorStatusBar(activity: Activity) {
        val windowInsetsController = WindowCompat.getInsetsController(activity.window, activity.window.decorView)
        windowInsetsController.isAppearanceLightStatusBars = false
    }
    
    fun setTransparentStatusBar(activity: Activity) {
        val windowInsetsController = WindowCompat.getInsetsController(activity.window, activity.window.decorView)
        windowInsetsController.isAppearanceLightStatusBars = false
    }
} 