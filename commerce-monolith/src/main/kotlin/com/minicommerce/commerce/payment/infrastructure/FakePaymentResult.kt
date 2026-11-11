package com.minicommerce.commerce.payment.infrastructure

data class FakePaymentResult(
    val approved: Boolean,
    val failureReason: String? = null,
) {
    companion object {
        fun approved(): FakePaymentResult = FakePaymentResult(approved = true)

        fun rejected(reason: String): FakePaymentResult = FakePaymentResult(
            approved = false,
            failureReason = reason,
        )
    }
}
