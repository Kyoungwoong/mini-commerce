package com.minicommerce.commerce.product.domain

sealed class ProductException(
    val errorCode: ProductErrorCode,
) : RuntimeException(errorCode.message)

class ProductNotFoundException : ProductException(ProductErrorCode.PRODUCT_NOT_FOUND)
