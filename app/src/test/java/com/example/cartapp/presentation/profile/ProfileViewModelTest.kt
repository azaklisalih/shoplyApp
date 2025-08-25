package com.example.cartapp.presentation.profile

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileViewModelTest {

    private lateinit var viewModel: ProfileViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = ProfileViewModel()
    }

    @Test
    fun `viewModel should be initialized correctly`() = runTest {
        // Then
        assert(viewModel != null)
    }

    @Test
    fun `viewModel should extend ViewModel`() = runTest {
        // Then
        assert(viewModel is androidx.lifecycle.ViewModel)
    }
} 