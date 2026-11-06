package com.minicommerce.commerce.notification.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

/**
 * A notification record stored in the database.
 * Storing a row is not delivery: nothing is sent to an external channel, so there is no SENT status.
 */
@Entity
@Table(name = "notifications")
class Notification(
    @Column(nullable = false)
    val memberId: Long,

    @Column(nullable = false)
    val orderId: Long,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    val type: NotificationType,

    @Column(nullable = false, length = 255)
    val message: String,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    @Column(nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()

    companion object {
        fun paymentApproved(memberId: Long, orderId: Long): Notification = Notification(
            memberId = memberId,
            orderId = orderId,
            type = NotificationType.PAYMENT_APPROVED,
            message = "주문 ${orderId}의 결제가 승인되었습니다.",
        )
    }
}
