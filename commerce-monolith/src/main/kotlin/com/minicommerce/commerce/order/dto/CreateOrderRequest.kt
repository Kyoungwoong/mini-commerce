package com.minicommerce.commerce.order.dto

import jakarta.validation.Valid
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Positive

data class CreateOrderRequest(
    @field:Positive
    val memberId: Long,

    @field:Valid
    @field:NotEmpty
    val items: List<CreateOrderItemRequest>,
)
