package com.example.cartapp.util

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import java.util.Locale

/**
 * Locale helper utility for managing app language changes
 */
object LocaleHelper {
    
    private const val SELECTED_LANGUAGE = "Locale.Helper.Selected.Language"
    
    /**
     * Set the app locale to the specified language
     */
    fun setLocale(context: Context, language: String): Context {
        persist(context, language)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            updateResources(context, language)
        } else {
            updateResourcesLegacy(context, language)
        }
    }
    
    /**
     * Get the current app locale
     */
    fun getLocale(resources: Resources): Locale {
        val config = resources.configuration
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.locales[0]
        } else {
            @Suppress("DEPRECATION")
            config.locale
        }
    }
    
    /**
     * Get the current language code
     */
    fun getLanguage(context: Context): String {
        val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        return prefs.getString(SELECTED_LANGUAGE, "tr") ?: "tr"
    }
    
    /**
     * Check if the current language is Turkish
     */
    fun isTurkish(context: Context): Boolean {
        return getLanguage(context) == "tr"
    }
    
    /**
     * Check if the current language is English
     */
    fun isEnglish(context: Context): Boolean {
        return getLanguage(context) == "en"
    }
    
    /**
     * Toggle between Turkish and English
     */
    fun toggleLanguage(context: Context): String {
        val currentLanguage = getLanguage(context)
        val newLanguage = if (currentLanguage == "tr") "en" else "tr"
        setLocale(context, newLanguage)
        return newLanguage
    }
    
    private fun persist(context: Context, language: String) {
        val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        prefs.edit().putString(SELECTED_LANGUAGE, language).apply()
    }
    
    @Suppress("DEPRECATION")
    private fun updateResources(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)
        
        val configuration = Configuration(context.resources.configuration)
        configuration.setLocale(locale)
        
        return context.createConfigurationContext(configuration)
    }
    
    @Suppress("DEPRECATION")
    private fun updateResourcesLegacy(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)
        
        val resources = context.resources
        val configuration = Configuration(resources.configuration)
        configuration.locale = locale
        resources.updateConfiguration(configuration, resources.displayMetrics)
        
        return context
    }
    
    /**
     * Get available languages
     */
    fun getAvailableLanguages(): List<Language> {
        return listOf(
            Language("tr", "Türkçe", "Turkish"),
            Language("en", "English", "English")
        )
    }
    
    /**
     * Language data class
     */
    data class Language(
        val code: String,
        val nativeName: String,
        val englishName: String
    )
} 