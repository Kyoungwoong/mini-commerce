package com.minicommerce.commerce.order.domain

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "orders")
class Order(
    @Column(nullable = false)
    val memberId: Long,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    val status: OrderStatus = OrderStatus.CREATED,

    items: List<OrderItem>,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    @Column(nullable = false)
    val totalPrice: Long = items.sumOf { it.totalPrice }

    @Column(nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "order_id", nullable = false)
    val items: MutableList<OrderItem> = items.toMutableList()
}
