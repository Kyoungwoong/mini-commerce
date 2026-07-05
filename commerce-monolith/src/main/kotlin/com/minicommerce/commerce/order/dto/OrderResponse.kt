package com.minicommerce.commerce.order.dto

import com.minicommerce.commerce.order.domain.Order

data class OrderResponse(
    val orderId: Long,
    val memberId: Long,
    val status: String,
    val totalPrice: Long,
    val items: List<OrderItemResponse>,
) {
    companion object {
        fun from(order: Order): OrderResponse = OrderResponse(
            orderId = order.id,
            memberId = order.memberId,
            status = order.status.name,
            totalPrice = order.totalPrice,
            items = order.items.map(OrderItemResponse::from),
        )
    }
}
