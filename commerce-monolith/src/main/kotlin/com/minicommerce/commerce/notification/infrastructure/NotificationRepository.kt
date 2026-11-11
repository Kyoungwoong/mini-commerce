package com.minicommerce.commerce.notification.infrastructure

import com.minicommerce.commerce.notification.domain.Notification
import org.springframework.data.jpa.repository.JpaRepository

interface NotificationRepository : JpaRepository<Notification, Long> {

    fun findAllByMemberIdOrderByIdDesc(memberId: Long): List<Notification>
}
