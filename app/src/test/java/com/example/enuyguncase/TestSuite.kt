package com.example.cartapp

import com.example.cartapp.domain.model.CartItemTest
import com.example.cartapp.domain.model.FavoriteTest
import com.example.cartapp.domain.model.ProductTest
import com.example.cartapp.domain.usecase.AddToCartUseCaseTest
import com.example.cartapp.domain.usecase.home.GetCategoriesUseCaseTest
import com.example.cartapp.domain.usecase.home.GetProductsUseCaseTest
import com.example.cartapp.domain.usecase.home.SearchProductsUseCaseTest
import com.example.cartapp.domain.usecase.home.GetProductsByCategoryUseCaseTest
import com.example.cartapp.domain.usecase.cart.GetCartItemsUseCaseTest
import com.example.cartapp.domain.usecase.cart.IncreaseCartQuantityUseCaseTest
import com.example.cartapp.domain.usecase.cart.DecreaseCartQuantityUseCaseTest
import com.example.cartapp.domain.usecase.cart.RemoveCartItemUseCaseTest
import com.example.cartapp.domain.usecase.cart.CheckoutUseCaseTest
import com.example.cartapp.domain.usecase.cart.ClearCartUseCaseTest
import com.example.cartapp.domain.usecase.favorite.GetAllFavoritesUseCaseTest
import com.example.cartapp.domain.usecase.favorite.ObserveFavoriteIdsUseCaseTest
import com.example.cartapp.domain.usecase.favorite.ConvertFavoriteToProductUseCaseTest
import com.example.cartapp.domain.usecase.productdetail.AddFavoriteUseCaseTest
import com.example.cartapp.domain.usecase.productdetail.CheckFavoriteUseCaseTest
import com.example.cartapp.domain.usecase.productdetail.GetProductByIdUseCaseTest
import com.example.cartapp.domain.usecase.productdetail.RemoveFavoriteUseCaseTest
import com.example.cartapp.data.home.repository.CartRepositoryImplTest
import com.example.cartapp.data.home.repository.FavoriteRepositoryImplTest
import com.example.cartapp.data.home.mapper.ProductMapperTest
import com.example.cartapp.data.cart.CartMapperTest
import com.example.cartapp.data.favorite.mapper.FavoriteMapperTest
import com.example.cartapp.presentation.home.HomeViewModelTest
import com.example.cartapp.presentation.cart.CartViewModelTest
import com.example.cartapp.presentation.favorite.FavoriteViewModelTest
import com.example.cartapp.presentation.common.StatusBarManagerTest
import com.example.cartapp.presentation.common.CartBadgeManagerTest

import org.junit.runner.RunWith
import org.junit.runners.Suite

/**
 * Test Suite that runs all unit tests in the application
 * This allows running all tests together or specific test categories
 */
@RunWith(Suite::class)
@Suite.SuiteClasses(
    // Domain Model Tests
    ProductTest::class,
    CartItemTest::class,
    FavoriteTest::class,
    
    // Use Case Tests
    AddToCartUseCaseTest::class,
    GetCategoriesUseCaseTest::class,
    GetProductsUseCaseTest::class,
    SearchProductsUseCaseTest::class,
    GetProductsByCategoryUseCaseTest::class,
    GetCartItemsUseCaseTest::class,
    IncreaseCartQuantityUseCaseTest::class,
    DecreaseCartQuantityUseCaseTest::class,
    RemoveCartItemUseCaseTest::class,
    CheckoutUseCaseTest::class,
    ClearCartUseCaseTest::class,
    GetAllFavoritesUseCaseTest::class,
    ObserveFavoriteIdsUseCaseTest::class,
    ConvertFavoriteToProductUseCaseTest::class,
    AddFavoriteUseCaseTest::class,
    CheckFavoriteUseCaseTest::class,
    GetProductByIdUseCaseTest::class,
    RemoveFavoriteUseCaseTest::class,
    
    // Repository Tests
    CartRepositoryImplTest::class,
    FavoriteRepositoryImplTest::class,
    
    // Mapper Tests
    ProductMapperTest::class,
    CartMapperTest::class,
    FavoriteMapperTest::class,
    
    // ViewModel Tests
    HomeViewModelTest::class,
    CartViewModelTest::class,
    FavoriteViewModelTest::class,
    
    // Common Manager Tests
    StatusBarManagerTest::class,
    CartBadgeManagerTest::class
)
class TestSuite 