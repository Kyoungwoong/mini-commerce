package com.minicommerce.commerce.payment.dto

import com.minicommerce.commerce.payment.domain.Payment

data class PaymentDetailResponse(
    val paymentId: Long,
    val orderId: Long,
    val amount: Long,
    val status: String,
) {
    companion object {
        fun from(payment: Payment): PaymentDetailResponse = PaymentDetailResponse(
            paymentId = payment.id,
            orderId = payment.orderId,
            amount = payment.amount,
            status = payment.status.name,
        )
    }
}
