package com.example.cartapp.domain.model

sealed class ErrorType {
    data object None : ErrorType()
    data object NetworkError : ErrorType()
    data object ServerError : ErrorType()
    data object ClientError : ErrorType()
    data object EmptyResponse : ErrorType()
    data object UnknownError : ErrorType()
    
    // Cart Errors
    data object FailedUpdateQuantity : ErrorType()
    data object FailedRemoveCart : ErrorType()
    data object FailedClearCart : ErrorType()
    
    // Favorite Errors
    data object FailedRemoveFavorites : ErrorType()
    data object FailedAddCart : ErrorType()
    data object FailedToggleFavorite : ErrorType()
    
    // Filter Errors
    data object FailedLoadFilterData : ErrorType()
    
    // Checkout Errors
    data object FailedPlaceOrder : ErrorType()
    
    data class CustomError(val message: String) : ErrorType()
} 