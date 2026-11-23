package com.example.inventarioapp.feature.product.data.remote

data class ProductDto(
    val id: String,
    val name: String,
    val description: String,
    val priceCop: Long,
    val quantity: Int,
    val updatedAt: Long,
    val isDeleted: Boolean
)
