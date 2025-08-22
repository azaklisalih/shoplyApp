package com.example.cartapp.domain.usecase.home

import com.example.cartapp.domain.model.Category
import com.example.cartapp.domain.repository.ProductRepository
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class GetCategoriesUseCaseTest {

    @Mock
    private lateinit var mockRepository: ProductRepository

    private lateinit var getCategoriesUseCase: GetCategoriesUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        getCategoriesUseCase = GetCategoriesUseCase(mockRepository)
    }

    @Test
    fun `invoke should return list of categories`() = runTest {
        // Given
        val mockCategories = listOf(
            Category("electronics", "Electronics"),
            Category("clothing", "Clothing"),
            Category("books", "Books"),
            Category("home-garden", "Home & Garden")
        )

        whenever(mockRepository.getCategoriesFlow()).thenReturn(flowOf(mockCategories))

        // When
        val result = getCategoriesUseCase()

        // Then
        result.collect { categories ->
            assertEquals(4, categories.size)
            assertEquals("electronics", categories[0].slug)
            assertEquals("Electronics", categories[0].displayName)
            assertEquals("clothing", categories[1].slug)
            assertEquals("Clothing", categories[1].displayName)
            assertEquals("books", categories[2].slug)
            assertEquals("Books", categories[2].displayName)
            assertEquals("home-garden", categories[3].slug)
            assertEquals("Home & Garden", categories[3].displayName)
        }

        verify(mockRepository).getCategoriesFlow()
    }

    @Test
    fun `invoke should handle empty categories list`() = runTest {
        // Given
        val mockCategories = emptyList<Category>()

        whenever(mockRepository.getCategoriesFlow()).thenReturn(flowOf(mockCategories))

        // When
        val result = getCategoriesUseCase()

        // Then
        result.collect { categories ->
            assertEquals(0, categories.size)
        }

        verify(mockRepository).getCategoriesFlow()
    }

    @Test
    fun `invoke should handle single category`() = runTest {
        // Given
        val mockCategories = listOf(
            Category("electronics", "Electronics")
        )

        whenever(mockRepository.getCategoriesFlow()).thenReturn(flowOf(mockCategories))

        // When
        val result = getCategoriesUseCase()

        // Then
        result.collect { categories ->
            assertEquals(1, categories.size)
            assertEquals("electronics", categories[0].slug)
            assertEquals("Electronics", categories[0].displayName)
        }

        verify(mockRepository).getCategoriesFlow()
    }
} 