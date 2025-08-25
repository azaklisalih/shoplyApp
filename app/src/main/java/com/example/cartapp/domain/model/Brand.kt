package com.example.cartapp.domain.model

@JvmInline
value class Brand(val value: String) : Comparable<Brand> {
    init {
        require(value.isNotBlank()) { "Brand cannot be blank" }
    }
    
    override fun toString(): String = value
    
    override fun compareTo(other: Brand): Int = value.compareTo(other.value)
} 