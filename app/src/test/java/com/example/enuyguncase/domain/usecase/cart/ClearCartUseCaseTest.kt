package com.example.cartapp.domain.usecase.cart

import com.example.cartapp.domain.repository.CartRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class ClearCartUseCaseTest {
    
    private lateinit var useCase: ClearCartUseCase
    private lateinit var repository: CartRepository
    
    @Before
    fun setUp() {
        repository = mock()
        useCase = ClearCartUseCase(repository)
    }
    
    @Test
    fun `invoke should call repository clearCart`() = runTest {
        // When
        useCase()
        
        // Then
        verify(repository).clearCart()
    }
} 