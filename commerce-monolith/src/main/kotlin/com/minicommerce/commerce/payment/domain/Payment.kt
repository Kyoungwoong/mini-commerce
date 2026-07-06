package com.minicommerce.commerce.payment.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "payments")
class Payment(
    @Column(nullable = false)
    val orderId: Long,

    @Column(nullable = false)
    val amount: Long,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    val status: PaymentStatus,

    @Column(length = 255)
    val failureReason: String? = null,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    @Column(nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()
}
