package com.minicommerce.commerce.payment.infrastructure

import org.springframework.stereotype.Component

/**
 * Server-side payment simulation. No real PG or external HTTP call.
 * Approves every payment by default; tests replace this bean to produce rejections.
 */
@Component
class FakePaymentGateway {

    fun pay(orderId: Long, amount: Long): FakePaymentResult = FakePaymentResult.approved()
}
