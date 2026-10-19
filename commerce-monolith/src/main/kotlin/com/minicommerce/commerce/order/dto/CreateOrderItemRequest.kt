package com.minicommerce.commerce.order.dto

import jakarta.validation.constraints.Positive

data class CreateOrderItemRequest(
    @field:Positive
    val productId: Long,

    @field:Positive
    val quantity: Int,
)
