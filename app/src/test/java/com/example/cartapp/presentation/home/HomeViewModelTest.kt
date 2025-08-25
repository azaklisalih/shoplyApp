package com.example.cartapp.presentation.home

import com.example.cartapp.domain.model.Product
import com.example.cartapp.domain.usecase.cart.AddToCartUseCase
import com.example.cartapp.domain.usecase.favorite.AddToFavoritesUseCase
import com.example.cartapp.domain.usecase.favorite.ObserveFavoriteIdsUseCase
import com.example.cartapp.domain.usecase.favorite.RemoveFromFavoritesUseCase
import com.example.cartapp.domain.usecase.home.GetProductsUseCase
import com.example.cartapp.domain.usecase.home.SearchProductsUseCase
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
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    @Mock
    private lateinit var mockGetProducts: GetProductsUseCase

    @Mock
    private lateinit var mockSearchProducts: SearchProductsUseCase

    @Mock
    private lateinit var mockAddToCart: AddToCartUseCase

    @Mock
    private lateinit var mockAddToFavorites: AddToFavoritesUseCase

    @Mock
    private lateinit var mockRemoveFromFavorites: RemoveFromFavoritesUseCase

    @Mock
    private lateinit var mockObserveFavoriteIds: ObserveFavoriteIdsUseCase

    private lateinit var viewModel: HomeViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        
        // Default mock returns for init
        whenever(mockGetProducts()).thenReturn(flowOf(emptyList()))
        whenever(mockObserveFavoriteIds()).thenReturn(flowOf(emptySet()))
        
        viewModel = HomeViewModel(
            mockGetProducts,
            mockSearchProducts,
            mockAddToCart,
            mockAddToFavorites,
            mockRemoveFromFavorites,
            mockObserveFavoriteIds
        )
    }

    @Test
    fun `initial state should have loading true`() = runTest {
        // Given - ViewModel is initialized
        
        // When - Check initial state
        val initialState = viewModel.uiState.value
        
        // Then
        assertTrue(initialState.isLoading)
        assertTrue(initialState.products.isEmpty())
        assertEquals(0, initialState.page)
    }

    @Test
    fun `fetchProducts should update state with products`() = runTest {
        // Given
        val mockProducts = listOf(
            Product(
                id = "1",
                name = "Test Product 1",
                image = "test1.jpg",
                price = "99.99",
                description = "Test Description 1",
                model = "Test Model 1",
                brand = "Test Brand 1",
                createdAt = "2024-01-01"
            )
        )
        whenever(mockGetProducts()).thenReturn(flowOf(mockProducts))

        // When
        viewModel.fetchProducts()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(mockProducts, state.products)
        assertEquals(1, state.page)
    }

    @Test
    fun `addToCart should call use case with correct parameters`() = runTest {
        // Given
        val product = Product(
            id = "1",
            name = "Test Product",
            image = "test.jpg",
            price = "99.99",
            description = "Test Description",
            model = "Test Model",
            brand = "Test Brand",
            createdAt = "2024-01-01"
        )

        // When
        viewModel.addToCart(product)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        verify(mockAddToCart).invoke(product, 1)
    }

    @Test
    fun `toggleFavorite should add to favorites when not favorite`() = runTest {
        // Given
        val product = Product(
            id = "1",
            name = "Test Product",
            image = "test.jpg",
            price = "99.99",
            description = "Test Description",
            model = "Test Model",
            brand = "Test Brand",
            createdAt = "2024-01-01"
        )

        // When
        viewModel.toggleFavorite(product)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        verify(mockAddToFavorites).invoke(product)
    }

    @Test
    fun `searchProducts with empty query should call fetchProducts`() = runTest {
        // Given
        val emptyQuery = ""

        // When
        viewModel.searchProducts(emptyQuery)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        // Should call getProducts (fetchProducts internally)
        verify(mockGetProducts).invoke()
    }

    @Test
    fun `searchProducts with valid query should call search use case`() = runTest {
        // Given
        val searchQuery = "test"
        val mockSearchResults = listOf(
            Product(
                id = "1",
                name = "Test Product",
                image = "test.jpg",
                price = "99.99",
                description = "Test Description",
                model = "Test Model",
                brand = "Test Brand",
                createdAt = "2024-01-01"
            )
        )
        whenever(mockSearchProducts.invoke(searchQuery)).thenReturn(flowOf(mockSearchResults))

        // When
        viewModel.searchProducts(searchQuery)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        verify(mockSearchProducts).invoke(searchQuery)
    }

    @Test
    fun `clearFilters should reset filter state`() = runTest {
        // When
        viewModel.clearFilters()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertEquals(null, state.selectedSortBy)
        assertEquals(null, state.selectedSortOrder)
        assertTrue(state.selectedBrands.isEmpty())
        assertTrue(state.selectedModels.isEmpty())
        assertEquals(0, state.page)
    }

    @Test
    fun `toggleBrand should update selected brands`() = runTest {
        // Given
        val brand = "Test Brand"

        // When
        viewModel.toggleBrand(brand)

        // Then
        val state = viewModel.uiState.value
        assertTrue(state.selectedBrands.contains(brand))
        assertTrue(state.selectedModels.isEmpty()) // Should clear models
    }

    @Test
    fun `toggleModel should update selected models`() = runTest {
        // Given
        val model = "Test Model"

        // When
        viewModel.toggleModel(model)

        // Then
        val state = viewModel.uiState.value
        assertTrue(state.selectedModels.contains(model))
    }

    @Test
    fun `setSorting should update sort parameters`() = runTest {
        // Given
        val sortBy = "price"
        val order = "asc"

        // When
        viewModel.setSorting(sortBy, order)

        // Then
        val state = viewModel.uiState.value
        assertEquals(sortBy, state.selectedSortBy)
        assertEquals(order, state.selectedSortOrder)
    }

    @Test
    fun `updateBrandSearch should filter brands`() = runTest {
        // Given
        val searchQuery = "test"

        // When
        viewModel.updateBrandSearch(searchQuery)

        // Then
        val state = viewModel.uiState.value
        assertEquals(searchQuery, state.brandSearchQuery)
    }

    @Test
    fun `refreshHome should clear search and filters`() = runTest {
        // When
        viewModel.refreshHome()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val searchQuery = viewModel.searchQuery.value
        val state = viewModel.uiState.value
        
        assertEquals("", searchQuery)
        assertEquals(null, state.selectedSortBy)
        assertTrue(state.selectedBrands.isEmpty())
    }
} 