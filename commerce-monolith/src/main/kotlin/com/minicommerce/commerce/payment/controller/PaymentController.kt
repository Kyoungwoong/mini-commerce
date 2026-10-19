package com.minicommerce.commerce.payment.controller

import com.minicommerce.commerce.payment.application.PaymentService
import com.minicommerce.commerce.payment.dto.PaymentDetailResponse
import com.minicommerce.commerce.payment.dto.PaymentResponse
import com.minicommerce.commerce.payment.dto.RequestPaymentRequest
import com.minicommerce.common.response.ApiResponse
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/payments")
class PaymentController(
    private val paymentService: PaymentService,
) {

    @PostMapping
    fun requestPayment(
        @Valid @RequestBody request: RequestPaymentRequest,
    ): ApiResponse<PaymentResponse> = ApiResponse.success(paymentService.requestPayment(request))

    @GetMapping("/{paymentId}")
    fun findById(
        @PathVariable paymentId: Long,
    ): ApiResponse<PaymentDetailResponse> = ApiResponse.success(paymentService.findById(paymentId))
}
