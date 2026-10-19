package com.minicommerce.commerce.order.dto

import com.minicommerce.commerce.order.domain.Order

data class OrderSummaryResponse(
    val orderId: Long,
    val memberId: Long,
    val status: String,
    val totalPrice: Long,
) {
    companion object {
        fun from(order: Order): OrderSummaryResponse = OrderSummaryResponse(
            orderId = order.id,
            memberId = order.memberId,
            status = order.status.name,
            totalPrice = order.totalPrice,
        )
    }
}
