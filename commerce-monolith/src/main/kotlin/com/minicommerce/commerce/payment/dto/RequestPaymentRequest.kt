package com.minicommerce.commerce.payment.dto

import jakarta.validation.constraints.Positive

data class RequestPaymentRequest(
    @field:Positive
    val orderId: Long,

    val shouldFail: Boolean,
)
