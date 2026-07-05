package com.minicommerce.commerce.order.dto

import com.minicommerce.commerce.order.domain.OrderItem

data class OrderItemResponse(
    val productId: Long,
    val productName: String,
    val orderPrice: Long,
    val quantity: Int,
) {
    companion object {
        fun from(orderItem: OrderItem): OrderItemResponse = OrderItemResponse(
            productId = orderItem.productId,
            productName = orderItem.productName,
            orderPrice = orderItem.orderPrice,
            quantity = orderItem.quantity,
        )
    }
}
