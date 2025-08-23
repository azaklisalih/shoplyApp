package com.example.cartapp.presentation.settings

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cartapp.presentation.ui_state.SettingsUIState
import com.example.cartapp.util.LocaleHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(SettingsUIState())
    val uiState: StateFlow<SettingsUIState> = _uiState.asStateFlow()
    
    private val _currentLanguage = MutableLiveData<String>(LocaleHelper.getLanguage(context))
    val currentLanguage: LiveData<String> = _currentLanguage
    
    init {
        loadCurrentLanguage()
    }
    
    private fun loadCurrentLanguage() {
        _currentLanguage.value = LocaleHelper.getLanguage(context)
    }
    
    fun setLanguage(language: String): Boolean {
        if (_currentLanguage.value != language) {
            _currentLanguage.value = language
            LocaleHelper.setLocale(context, language)
            _uiState.value = _uiState.value.copy(currentLanguage = language)
            return true
        }
        return false
    }
} 