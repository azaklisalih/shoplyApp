package com.example.cartapp.presentation.settings

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {

    @Mock
    private lateinit var mockContext: Context

    private lateinit var viewModel: SettingsViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        
        viewModel = SettingsViewModel(mockContext)
    }

    @Test
    fun `initial state should have current language`() = runTest {
        // Given - ViewModel is initialized
        
        // When - Check initial state
        val initialState = viewModel.uiState.value
        
        // Then
        assertEquals("", initialState.currentLanguage) // Default empty string
    }

    @Test
    fun `setLanguage should update current language`() = runTest {
        // Given
        val newLanguage = "en"
        
        // When
        val result = viewModel.setLanguage(newLanguage)
        
        // Then
        assertTrue(result) // Should return true for new language
        assertEquals(newLanguage, viewModel.uiState.value.currentLanguage)
    }

    @Test
    fun `setLanguage with same language should return false`() = runTest {
        // Given
        val language = "tr"
        viewModel.setLanguage(language) // Set initial language
        
        // When
        val result = viewModel.setLanguage(language) // Set same language again
        
        // Then
        assertFalse(result) // Should return false for same language
    }

    @Test
    fun `currentLanguage LiveData should be initialized`() = runTest {
        // Given - ViewModel is initialized
        
        // When - Check LiveData
        val currentLanguage = viewModel.currentLanguage.value
        
        // Then
        // Should not be null (initialized in constructor)
        assertTrue(currentLanguage != null)
    }
} 