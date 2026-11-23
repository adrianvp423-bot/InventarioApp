package com.example.inventarioapp.feature.product.data.mapper

import com.example.inventarioapp.feature.product.data.local.ProductEntity
import com.example.inventarioapp.feature.product.domain.model.Product

fun ProductEntity.toDomain(): Product = Product(
    id = id,
    name = name,
    description = description,
    priceCop = priceCop,
    quantity = quantity,
    updatedAt = updatedAt
)

fun Product.toEntity(): ProductEntity = ProductEntity(
    id = id,
    name = name,
    description = description,
    priceCop = priceCop,
    quantity = quantity,
    updatedAt = updatedAt,
    pending = true,
    isDeleted = false
)
