package com.minicommerce.commerce.payment.application

import com.minicommerce.commerce.notification.application.NotificationService
import com.minicommerce.commerce.order.domain.OrderNotFoundException
import com.minicommerce.commerce.order.infrastructure.OrderRepository
import com.minicommerce.commerce.payment.domain.Payment
import com.minicommerce.commerce.payment.domain.PaymentNotFoundException
import com.minicommerce.commerce.payment.domain.PaymentStatus
import com.minicommerce.commerce.payment.dto.PaymentDetailResponse
import com.minicommerce.commerce.payment.dto.PaymentResponse
import com.minicommerce.commerce.payment.dto.RequestPaymentRequest
import com.minicommerce.commerce.payment.infrastructure.FakePaymentGateway
import com.minicommerce.commerce.payment.infrastructure.PaymentRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PaymentService(
    private val paymentRepository: PaymentRepository,
    private val orderRepository: OrderRepository,
    private val fakePaymentGateway: FakePaymentGateway,
    private val notificationService: NotificationService,
) {

    @Transactional
    fun requestPayment(request: RequestPaymentRequest): PaymentResponse {
        val order = orderRepository.findById(request.orderId)
            .orElseThrow { OrderNotFoundException() }

        val result = fakePaymentGateway.pay(order.id, order.totalPrice)

        val payment = if (result.approved) {
            order.markPaid()
            Payment(
                orderId = order.id,
                amount = order.totalPrice,
                status = PaymentStatus.APPROVED,
            )
        } else {
            order.markPaymentFailed()
            Payment(
                orderId = order.id,
                amount = order.totalPrice,
                status = PaymentStatus.FAILED,
                failureReason = result.failureReason ?: "FAKE_PAYMENT_FAILED",
            )
        }

        val savedPayment = paymentRepository.save(payment)

        // Synchronous, in-process call inside this payment transaction (Phase 2 baseline).
        if (savedPayment.status == PaymentStatus.APPROVED) {
            notificationService.recordPaymentApproved(order.memberId, order.id)
        }

        return PaymentResponse.from(savedPayment, order)
    }

    @Transactional(readOnly = true)
    fun findById(paymentId: Long): PaymentDetailResponse {
        val payment = paymentRepository.findById(paymentId)
            .orElseThrow { PaymentNotFoundException() }

        return PaymentDetailResponse.from(payment)
    }
}
