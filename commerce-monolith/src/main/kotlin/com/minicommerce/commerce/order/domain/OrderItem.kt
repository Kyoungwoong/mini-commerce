package com.minicommerce.commerce.order.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "order_items")
class OrderItem(
    @Column(nullable = false)
    val productId: Long,

    @Column(nullable = false, length = 150)
    val productName: String,

    @Column(nullable = false)
    val orderPrice: Long,

    @Column(nullable = false)
    val quantity: Int,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    val totalPrice: Long
        get() = orderPrice * quantity
}
