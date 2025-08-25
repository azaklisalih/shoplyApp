package com.example.cartapp.domain.model

enum class ErrorMessage(val key: String) {
    // General Errors
    UNKNOWN("error_unknown"),
    NETWORK_ERROR("error_network"),
    SERVER_ERROR("error_server"),
    CLIENT_ERROR("error_client"),
    EMPTY_RESPONSE("error_empty_response"),
    UNEXPECTED_HTTP("error_unexpected_http"),
    
    // Cart Errors
    FAILED_UPDATE_QUANTITY("error_failed_update_quantity"),
    FAILED_REMOVE_CART("error_failed_remove_cart"),
    FAILED_CLEAR_CART("error_failed_clear_cart"),
    
    // Favorite Errors
    FAILED_REMOVE_FAVORITES("error_failed_remove_favorites"),
    FAILED_ADD_CART("error_failed_add_cart"),
    FAILED_TOGGLE_FAVORITE("error_failed_toggle_favorite"),
    
    // Filter Errors
    FAILED_LOAD_FILTER_DATA("error_failed_load_filter_data"),
    
    // Checkout Errors
    FAILED_PLACE_ORDER("error_failed_place_order"),
    
    // Validation Errors
    NAME_REQUIRED("checkout_name_required"),
    EMAIL_REQUIRED("checkout_email_required"),
    EMAIL_INVALID("checkout_email_invalid"),
    PHONE_REQUIRED("checkout_phone_required"),
    PHONE_INVALID("checkout_phone_invalid");
    
    companion object {
        fun fromErrorType(errorType: ErrorType): ErrorMessage = when (errorType) {
            is ErrorType.UnknownError -> UNKNOWN
            is ErrorType.NetworkError -> NETWORK_ERROR
            is ErrorType.ServerError -> SERVER_ERROR
            is ErrorType.ClientError -> CLIENT_ERROR
            is ErrorType.EmptyResponse -> EMPTY_RESPONSE
            is ErrorType.FailedUpdateQuantity -> FAILED_UPDATE_QUANTITY
            is ErrorType.FailedRemoveCart -> FAILED_REMOVE_CART
            is ErrorType.FailedClearCart -> FAILED_CLEAR_CART
            is ErrorType.FailedRemoveFavorites -> FAILED_REMOVE_FAVORITES
            is ErrorType.FailedAddCart -> FAILED_ADD_CART
            is ErrorType.FailedToggleFavorite -> FAILED_TOGGLE_FAVORITE
            is ErrorType.FailedLoadFilterData -> FAILED_LOAD_FILTER_DATA
            is ErrorType.FailedPlaceOrder -> FAILED_PLACE_ORDER
            else -> UNKNOWN
        }
    }
} 