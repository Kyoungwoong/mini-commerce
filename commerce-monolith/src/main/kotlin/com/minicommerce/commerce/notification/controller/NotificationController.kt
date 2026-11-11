package com.minicommerce.commerce.notification.controller

import com.minicommerce.commerce.notification.application.NotificationService
import com.minicommerce.commerce.notification.dto.NotificationResponse
import com.minicommerce.common.response.ApiResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/notifications")
class NotificationController(
    private val notificationService: NotificationService,
) {

    @GetMapping
    fun findByMemberId(
        @RequestParam memberId: Long,
    ): ApiResponse<List<NotificationResponse>> = ApiResponse.success(notificationService.findByMemberId(memberId))
}
