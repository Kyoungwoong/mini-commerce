package com.minicommerce.commerce.product.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.PositiveOrZero

data class RegisterProductRequest(
    @field:NotBlank
    val name: String,

    @field:Positive
    val price: Long,

    @field:PositiveOrZero
    val stockQuantity: Int,
)
