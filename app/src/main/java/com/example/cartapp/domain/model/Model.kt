package com.example.cartapp.domain.model

@JvmInline
value class Model(val value: String) : Comparable<Model> {
    init {
        require(value.isNotBlank()) { "Model cannot be blank" }
    }
    
    override fun toString(): String = value
    
    override fun compareTo(other: Model): Int = value.compareTo(other.value)
} 