package com.example.cartapp.util

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.core.content.edit

object AppLocaleManager {
    private const val PREFS = "settings"
    private const val KEY = "Locale.Helper.Selected.Language"
    private const val CODE_SYSTEM = "system"

    fun applySavedLocale(context: Context) {
        val tag = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getString(KEY, CODE_SYSTEM) ?: CODE_SYSTEM
        setLocale(context, tag, persist = false)
    }

    fun setLocale(context: Context, languageTag: String?, persist: Boolean = true) {
        val tag = languageTag?.ifBlank { CODE_SYSTEM } ?: CODE_SYSTEM
        val locales = if (tag == CODE_SYSTEM) {
            LocaleListCompat.getEmptyLocaleList()
        } else {
            LocaleListCompat.forLanguageTags(tag)
        }
        AppCompatDelegate.setApplicationLocales(locales)
        if (persist) {
            context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
                .edit { putString(KEY, tag) }
        }
    }

    fun getCurrentLanguageTag(): String {
        val tags = AppCompatDelegate.getApplicationLocales().toLanguageTags()
        return if (tags.isNullOrBlank()) CODE_SYSTEM else tags
    }

    fun isTurkish(): Boolean = getCurrentLanguageTag().startsWith("tr", ignoreCase = true)
    fun isEnglish(): Boolean = getCurrentLanguageTag().startsWith("en", ignoreCase = true)

    fun toggle(context: Context): String {
        val now = getCurrentLanguageTag()
        val next = if (now.startsWith("tr", true)) "en" else "tr"
        setLocale(context, next)
        return next
    }

    fun getAvailableLanguages(): List<Language> = listOf(
        Language("tr", "Türkçe", "Turkish"),
        Language("en", "English", "English")
    )

    data class Language(val code: String, val nativeName: String, val englishName: String)
} 