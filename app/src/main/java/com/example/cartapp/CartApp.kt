package com.example.cartapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CartApp : Application() {

    override fun onCreate() {
        super.onCreate()
    }
} 