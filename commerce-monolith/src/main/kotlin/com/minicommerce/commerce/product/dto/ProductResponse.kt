package com.minicommerce.commerce.product.dto

import com.minicommerce.commerce.product.domain.Product

data class ProductResponse(
    val productId: Long,
    val name: String,
    val price: Long,
    val stockQuantity: Int,
) {
    companion object {
        fun from(product: Product): ProductResponse = ProductResponse(
            productId = product.id,
            name = product.name,
            price = product.price,
            stockQuantity = product.stockQuantity,
        )
    }
}
