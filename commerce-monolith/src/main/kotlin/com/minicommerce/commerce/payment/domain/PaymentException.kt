package com.minicommerce.commerce.payment.domain

sealed class PaymentException(
    val errorCode: PaymentErrorCode,
) : RuntimeException(errorCode.message)

class PaymentNotFoundException : PaymentException(PaymentErrorCode.PAYMENT_NOT_FOUND)
