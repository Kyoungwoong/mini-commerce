package com.minicommerce.commerce.product.domain

enum class ProductErrorCode(
    val code: String,
    val message: String,
) {
    PRODUCT_NOT_FOUND(
        code = "PRODUCT_NOT_FOUND",
        message = "Product was not found.",
    ),
    INSUFFICIENT_PRODUCT_STOCK(
        code = "INSUFFICIENT_PRODUCT_STOCK",
        message = "Product stock is not enough.",
    ),
}
