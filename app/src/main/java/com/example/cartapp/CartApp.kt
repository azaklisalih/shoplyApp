package com.example.cartapp

import android.app.Application
import android.content.Context
import com.example.cartapp.util.LocaleHelper
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CartApp : Application() {

    override fun onCreate() {
        super.onCreate()
        val savedLanguage = LocaleHelper.getLanguage(this)
        LocaleHelper.setLocale(this, savedLanguage)
    }

    override fun attachBaseContext(base: Context) {
        val savedLanguage = LocaleHelper.getLanguage(base)
        val context = LocaleHelper.setLocale(base, savedLanguage)
        super.attachBaseContext(context)
    }
} 