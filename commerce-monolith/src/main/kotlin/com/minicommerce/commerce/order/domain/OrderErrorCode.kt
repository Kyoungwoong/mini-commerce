package com.minicommerce.commerce.order.domain

enum class OrderErrorCode(
    val code: String,
    val message: String,
) {
    ORDER_NOT_FOUND(
        code = "ORDER_NOT_FOUND",
        message = "Order was not found.",
    ),
    INVALID_ORDER_QUANTITY(
        code = "INVALID_ORDER_QUANTITY",
        message = "Order item quantity must be greater than 0.",
    ),
    ORDER_NOT_PAYABLE(
        code = "ORDER_NOT_PAYABLE",
        message = "Order is not payable.",
    ),
}
