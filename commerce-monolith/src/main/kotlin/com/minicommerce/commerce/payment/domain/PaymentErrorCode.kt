package com.minicommerce.commerce.payment.domain

enum class PaymentErrorCode(
    val code: String,
    val message: String,
) {
    PAYMENT_NOT_FOUND(
        code = "PAYMENT_NOT_FOUND",
        message = "Payment was not found.",
    ),
}
