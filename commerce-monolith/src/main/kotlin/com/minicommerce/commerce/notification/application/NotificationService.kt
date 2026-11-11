package com.minicommerce.commerce.notification.application

import com.minicommerce.commerce.notification.domain.Notification
import com.minicommerce.commerce.notification.dto.NotificationResponse
import com.minicommerce.commerce.notification.infrastructure.NotificationRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class NotificationService(
    private val notificationRepository: NotificationRepository,
) {

    // Joins the caller's transaction (default propagation), so a failure here rolls back the payment too.
    @Transactional
    fun recordPaymentApproved(memberId: Long, orderId: Long) {
        notificationRepository.save(Notification.paymentApproved(memberId, orderId))
    }

    @Transactional(readOnly = true)
    fun findByMemberId(memberId: Long): List<NotificationResponse> =
        notificationRepository.findAllByMemberIdOrderByIdDesc(memberId)
            .map { NotificationResponse.from(it) }
}
