package com.minicommerce.commerce.payment.dto

import com.minicommerce.commerce.order.domain.Order
import com.minicommerce.commerce.payment.domain.Payment

data class PaymentResponse(
    val paymentId: Long,
    val orderId: Long,
    val amount: Long,
    val status: String,
    val orderStatus: String,
) {
    companion object {
        fun from(payment: Payment, order: Order): PaymentResponse = PaymentResponse(
            paymentId = payment.id,
            orderId = payment.orderId,
            amount = payment.amount,
            status = payment.status.name,
            orderStatus = order.status.name,
        )
    }
}
