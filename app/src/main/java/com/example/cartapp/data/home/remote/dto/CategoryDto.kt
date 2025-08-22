package com.example.cartapp.data.home.remote.dto

import com.example.cartapp.domain.model.Category

data class CategoryDto(
    val slug: String,
    val name: String,
    val url: String
) {
    fun toDomain(): Category =
        Category(slug = slug, displayName = name)
}
