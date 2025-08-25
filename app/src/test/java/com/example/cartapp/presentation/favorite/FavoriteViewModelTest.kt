package com.example.cartapp.presentation.favorite

import com.example.cartapp.domain.usecase.cart.AddToCartUseCase
import com.example.cartapp.domain.usecase.favorite.ConvertFavoriteToProductUseCase
import com.example.cartapp.domain.usecase.favorite.GetFavoritesUseCase
import com.example.cartapp.domain.usecase.favorite.RemoveFromFavoritesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class FavoriteViewModelTest {

    @Mock
    private lateinit var mockGetFavorites: GetFavoritesUseCase

    @Mock
    private lateinit var mockRemoveFromFavorites: RemoveFromFavoritesUseCase

    @Mock
    private lateinit var mockConvertFavoriteToProduct: ConvertFavoriteToProductUseCase

    @Mock
    private lateinit var mockAddToCart: AddToCartUseCase

    private lateinit var viewModel: FavoriteViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        
        // Default mock return for init
        whenever(mockGetFavorites()).thenReturn(flowOf(emptyList()))
        
        viewModel = FavoriteViewModel(
            mockGetFavorites,
            mockRemoveFromFavorites,
            mockConvertFavoriteToProduct,
            mockAddToCart
        )
    }

    @Test
    fun `viewModel should be initialized correctly`() = runTest {
        // Then
        assert(viewModel != null)
    }

    @Test
    fun `initial state should have empty favorites`() = runTest {
        // When
        val initialState = viewModel.uiState.value
        
        // Then
        assert(initialState.favorites.isEmpty())
    }

    @Test
    fun `viewModel should extend ViewModel`() = runTest {
        // Then
        assert(viewModel is androidx.lifecycle.ViewModel)
    }
} 