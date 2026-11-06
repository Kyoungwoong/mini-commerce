package com.minicommerce.commerce.notification.dto

import com.minicommerce.commerce.notification.domain.Notification
import java.time.LocalDateTime

data class NotificationResponse(
    val notificationId: Long,
    val memberId: Long,
    val orderId: Long,
    val type: String,
    val message: String,
    val createdAt: LocalDateTime,
) {
    companion object {
        fun from(notification: Notification): NotificationResponse = NotificationResponse(
            notificationId = notification.id,
            memberId = notification.memberId,
            orderId = notification.orderId,
            type = notification.type.name,
            message = notification.message,
            createdAt = notification.createdAt,
        )
    }
}
