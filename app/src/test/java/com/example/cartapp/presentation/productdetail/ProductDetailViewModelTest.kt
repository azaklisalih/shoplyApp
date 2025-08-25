package com.example.cartapp.presentation.productdetail

import com.example.cartapp.domain.model.Product
import com.example.cartapp.domain.usecase.cart.AddToCartUseCase
import com.example.cartapp.domain.usecase.cart.GetCartItemsUseCase
import com.example.cartapp.domain.usecase.favorite.AddToFavoritesUseCase
import com.example.cartapp.domain.usecase.favorite.IsFavoriteUseCase
import com.example.cartapp.domain.usecase.favorite.RemoveFromFavoritesUseCase
import com.example.cartapp.domain.usecase.home.GetProductByIdUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
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
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlinx.coroutines.flow.MutableStateFlow
import com.example.cartapp.presentation.ui_state.ProductDetailUIState

@OptIn(ExperimentalCoroutinesApi::class)
class ProductDetailViewModelTest {

    @Mock
    private lateinit var mockGetProductById: GetProductByIdUseCase

    @Mock
    private lateinit var mockAddToCart: AddToCartUseCase



    @Mock
    private lateinit var mockAddToFavorites: AddToFavoritesUseCase

    @Mock
    private lateinit var mockRemoveFromFavorites: RemoveFromFavoritesUseCase

    @Mock
    private lateinit var mockIsFavorite: IsFavoriteUseCase

    @Mock
    private lateinit var mockGetCartItems: GetCartItemsUseCase

    private lateinit var viewModel: ProductDetailViewModel
    private val testDispatcher = StandardTestDispatcher()
    


    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        
        // Default mock returns
        whenever(mockGetCartItems()).thenReturn(flowOf(emptyList()))
        whenever(mockIsFavorite.invoke(any())).thenReturn(flowOf(false))
        
        viewModel = ProductDetailViewModel(
            mockGetProductById,
            mockAddToCart,
            mockGetCartItems,
            mockAddToFavorites,
            mockRemoveFromFavorites,
            mockIsFavorite
        )
    }

    @Test
    fun `initial state should be loading`() = runTest {
        // Given - ViewModel is initialized
        
        // When - Check initial state
        val initialState = viewModel.uiState.value
        
        // Then
        assertTrue(initialState.isLoading)
        assertFalse(initialState.isFavorite)
        assertFalse(initialState.isInCart)
    }

    @Test
    fun `loadProduct should update state with product`() = runTest {
        // Given
        val productId = "1"
        val mockProduct = Product(
            id = productId,
            name = "Test Product",
            image = "test.jpg",
            price = "99.99",
            description = "Test Description",
            model = "Test Model",
            brand = "Test Brand",
            createdAt = "2024-01-01"
        )
        whenever(mockGetProductById.invoke(productId)).thenReturn(flowOf(mockProduct))

        // When
        viewModel.loadProduct(productId)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(mockProduct, state.product)
    }

    @Test
    fun `addToCart should call use case when product is loaded`() = runTest {
        // Given
        val productId = "1"
        val mockProduct = Product(
            id = productId,
            name = "Test Product",
            image = "test.jpg",
            price = "99.99",
            description = "Test Description",
            model = "Test Model",
            brand = "Test Brand",
            createdAt = "2024-01-01"
        )
        whenever(mockGetProductById.invoke(productId)).thenReturn(flowOf(mockProduct))
        
        // When
        viewModel.loadProduct(productId)
        testDispatcher.scheduler.advanceUntilIdle()
        viewModel.addToCart()
        
        // Then
        verify(mockAddToCart).invoke(any(), any())
    }

    @Test
    fun `toggleFavorite should call appropriate use case`() = runTest {
        // Given
        whenever(mockIsFavorite.invoke(any())).thenReturn(flowOf(true))
        
        // When
        viewModel.toggleFavorite()
        
        // Then
        verify(mockRemoveFromFavorites).invoke(any())
    }
} 