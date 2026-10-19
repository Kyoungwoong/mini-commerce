package com.minicommerce.commerce.order.domain

sealed class OrderException(
    val errorCode: OrderErrorCode,
) : RuntimeException(errorCode.message)

class OrderNotFoundException : OrderException(OrderErrorCode.ORDER_NOT_FOUND)

class InvalidOrderQuantityException : OrderException(OrderErrorCode.INVALID_ORDER_QUANTITY)

class OrderNotPayableException : OrderException(OrderErrorCode.ORDER_NOT_PAYABLE)
