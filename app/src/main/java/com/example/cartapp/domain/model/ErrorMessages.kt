package com.example.cartapp.domain.model

/**
 * Domain error messages
 * These are used in repository layer where context is not available
 */
object ErrorMessages {
    const val EMPTY_RESPONSE = "empty_response"
    const val CLIENT_ERROR = "client_error"
    const val SERVER_ERROR = "server_error"
    const val UNEXPECTED_HTTP = "unexpected_http"
    const val UNKNOWN_BRAND = "unknown_brand"
} 