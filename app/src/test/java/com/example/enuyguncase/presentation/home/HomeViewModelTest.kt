package com.example.cartapp.presentation.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.cartapp.domain.model.Category
import com.example.cartapp.domain.model.Dimensions
import com.example.cartapp.domain.model.Meta
import com.example.cartapp.domain.model.Product
import com.example.cartapp.domain.model.ProductPage
import com.example.cartapp.domain.usecase.home.GetCategoriesUseCase
import com.example.cartapp.domain.usecase.home.GetProductsByCategoryUseCase
import com.example.cartapp.domain.usecase.home.GetProductsUseCase
import com.example.cartapp.domain.usecase.home.SearchProductsUseCase
import com.example.cartapp.domain.usecase.home.SortProductsLocallyUseCase
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock

class HomeViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: HomeViewModel
    private lateinit var getProductsUseCase: GetProductsUseCase
    private lateinit var getByCategoryUseCase: GetProductsByCategoryUseCase
    private lateinit var searchProductsUseCase: SearchProductsUseCase
    private lateinit var getCategoriesUseCase: GetCategoriesUseCase
    private lateinit var sortProductsLocallyUseCase: SortProductsLocallyUseCase

    private val testProduct = Product(
        id = 1,
        title = "Test Product",
        description = "Test Description",
        price = 100.0,
        discountPercentage = 10.0,
        rating = 4.5,
        stock = 50,
        brand = "Test Brand",
        category = "smartphones",
        thumbnail = "thumbnail.jpg",
        images = listOf("image1.jpg"),
        tags = listOf("test"),
        sku = "TEST123",
        weight = 150,
        dimensions = Dimensions(7.0, 14.0, 0.7),
        warrantyInformation = "1 year",
        shippingInformation = "Free shipping",
        availabilityStatus = "In Stock",
        reviews = emptyList(),
        returnPolicy = "30 days",
        minimumOrderQuantity = 1,
        meta = Meta("2023-01-01", "2023-01-01", "123456", "QR123")
    )

    private val testProductPage = ProductPage(
        products = listOf(testProduct),
        total = 1
    )

    private val testCategories = listOf(
        Category("smartphones", "Smartphones"),
        Category("laptops", "Laptops")
    )

    @Before
    fun setup() {
        getProductsUseCase = mock()
        getByCategoryUseCase = mock()
        searchProductsUseCase = mock()
        getCategoriesUseCase = mock()
        sortProductsLocallyUseCase = mock()

        viewModel = HomeViewModel(
            getProductsUseCase,
            getByCategoryUseCase,
            searchProductsUseCase,
            getCategoriesUseCase,
            sortProductsLocallyUseCase
        )
    }

    @Test
    fun `initial state should have loading true`() {
        val initialState = viewModel.uiState.value
        assertTrue(initialState.isLoading)
        assertTrue(initialState.products.isEmpty())
        assertEquals(0, initialState.total)
        assertEquals(0, initialState.page)
    }

    @Test
    fun `searchQuery should be initialized as empty string`() {
        assertEquals("", viewModel.searchQuery.value)
    }

    @Test
    fun `searchQuery LiveData should be observable`() {
        val observer = Observer<String> { }
        viewModel.searchQuery.observeForever(observer)
        
        assertEquals("", viewModel.searchQuery.value)
        
        viewModel.searchQuery.removeObserver(observer)
    }

    @Test
    fun `uiState should be observable`() {
        val initialState = viewModel.uiState.value
        assertTrue(initialState.isLoading)
        assertTrue(initialState.products.isEmpty())
    }
} 