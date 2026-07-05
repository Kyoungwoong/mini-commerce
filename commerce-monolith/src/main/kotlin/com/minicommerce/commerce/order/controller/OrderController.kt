package com.minicommerce.commerce.order.controller

import com.minicommerce.commerce.order.application.OrderService
import com.minicommerce.commerce.order.dto.CreateOrderRequest
import com.minicommerce.commerce.order.dto.OrderResponse
import com.minicommerce.commerce.order.dto.OrderSummaryResponse
import com.minicommerce.common.response.ApiResponse
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/orders")
class OrderController(
    private val orderService: OrderService,
) {

    @PostMapping
    fun create(
        @Valid @RequestBody request: CreateOrderRequest,
    ): ApiResponse<OrderResponse> = ApiResponse.success(orderService.create(request))

    @GetMapping("/{orderId}")
    fun findById(
        @PathVariable orderId: Long,
    ): ApiResponse<OrderResponse> = ApiResponse.success(orderService.findById(orderId))

    @GetMapping
    fun findAllByMemberId(
        @RequestParam memberId: Long,
    ): ApiResponse<List<OrderSummaryResponse>> = ApiResponse.success(orderService.findAllByMemberId(memberId))
}
