package com.minicommerce.commerce.order.domain

enum class OrderStatus {
    CREATED,
    PAYMENT_REQUESTED,
    PAID,
    PAYMENT_FAILED,
    CANCELLED,
}
