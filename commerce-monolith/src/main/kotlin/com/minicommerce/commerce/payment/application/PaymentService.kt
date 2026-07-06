package com.minicommerce.commerce.payment.application

import com.minicommerce.commerce.order.domain.OrderNotFoundException
import com.minicommerce.commerce.order.infrastructure.OrderRepository
import com.minicommerce.commerce.payment.domain.Payment
import com.minicommerce.commerce.payment.domain.PaymentNotFoundException
import com.minicommerce.commerce.payment.domain.PaymentStatus
import com.minicommerce.commerce.payment.dto.PaymentDetailResponse
import com.minicommerce.commerce.payment.dto.PaymentResponse
import com.minicommerce.commerce.payment.dto.RequestPaymentRequest
import com.minicommerce.commerce.payment.infrastructure.PaymentRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PaymentService(
    private val paymentRepository: PaymentRepository,
    private val orderRepository: OrderRepository,
) {

    @Transactional
    fun requestPayment(request: RequestPaymentRequest): PaymentResponse {
        val order = orderRepository.findById(request.orderId)
            .orElseThrow { OrderNotFoundException() }

        val payment = if (request.shouldFail) {
            order.markPaymentFailed()
            Payment(
                orderId = order.id,
                amount = order.totalPrice,
                status = PaymentStatus.FAILED,
                failureReason = "FAKE_PAYMENT_FAILED",
            )
        } else {
            order.markPaid()
            Payment(
                orderId = order.id,
                amount = order.totalPrice,
                status = PaymentStatus.APPROVED,
            )
        }

        val savedPayment = paymentRepository.save(payment)

        return PaymentResponse.from(savedPayment, order)
    }

    @Transactional(readOnly = true)
    fun findById(paymentId: Long): PaymentDetailResponse {
        val payment = paymentRepository.findById(paymentId)
            .orElseThrow { PaymentNotFoundException() }

        return PaymentDetailResponse.from(payment)
    }
}
