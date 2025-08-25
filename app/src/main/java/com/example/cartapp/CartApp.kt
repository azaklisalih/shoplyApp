package com.example.cartapp

import android.app.Application
import com.example.cartapp.util.AppLocaleManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CartApp : Application() {

    override fun onCreate() {
        super.onCreate()
        AppLocaleManager.applySavedLocale(this)
    }
} 