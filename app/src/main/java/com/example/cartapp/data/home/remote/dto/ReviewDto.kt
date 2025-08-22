package com.example.cartapp.data.home.remote.dto

data class ReviewDto(
    val rating: Int,
    val comment: String,
    val date: String,
    val reviewerName: String,
    val reviewerEmail: String
)