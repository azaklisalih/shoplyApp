package com.example.cartapp

import com.example.cartapp.data.cart.CartMapperTest
import com.example.cartapp.data.favorite.mapper.FavoriteMapperTest
import com.example.cartapp.data.home.mapper.ProductMapperTest
import com.example.cartapp.domain.usecase.home.GetProductsUseCaseTest
import com.example.cartapp.domain.usecase.home.SearchProductsUseCaseTest
import com.example.cartapp.domain.usecase.cart.IncreaseCartQuantityUseCaseTest
import com.example.cartapp.domain.usecase.cart.DecreaseCartQuantityUseCaseTest
import com.example.cartapp.domain.usecase.cart.RemoveCartItemUseCaseTest
import com.example.cartapp.domain.usecase.cart.CheckoutUseCaseTest
import com.example.cartapp.domain.usecase.productdetail.CheckFavoriteUseCaseTest
import com.example.cartapp.domain.usecase.productdetail.RemoveFavoriteUseCaseTest

import org.junit.runner.RunWith
import org.junit.runners.Suite

/**
 * Test Suite that runs all unit tests in the application
 * This allows running all tests together or specific test categories
 */
@RunWith(Suite::class)
@Suite.SuiteClasses(
    // Mapper Tests (Basic tests that work)
    ProductMapperTest::class,
    CartMapperTest::class,
    FavoriteMapperTest::class,

    // Use Case Tests (Working tests)
    GetProductsUseCaseTest::class,
    SearchProductsUseCaseTest::class,
    IncreaseCartQuantityUseCaseTest::class,
    DecreaseCartQuantityUseCaseTest::class,
    RemoveCartItemUseCaseTest::class,
    CheckoutUseCaseTest::class,
    CheckFavoriteUseCaseTest::class,
    RemoveFavoriteUseCaseTest::class
)
class TestSuite 