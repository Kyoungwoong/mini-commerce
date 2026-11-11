package com.minicommerce.commerce.notification

import com.minicommerce.commerce.member.domain.Member
import com.minicommerce.commerce.member.infrastructure.MemberRepository
import com.minicommerce.commerce.notification.application.NotificationService
import com.minicommerce.commerce.notification.infrastructure.NotificationRepository
import com.minicommerce.commerce.order.domain.Order
import com.minicommerce.commerce.order.domain.OrderItem
import com.minicommerce.commerce.order.domain.OrderStatus
import com.minicommerce.commerce.order.infrastructure.OrderRepository
import com.minicommerce.commerce.payment.infrastructure.FakePaymentGateway
import com.minicommerce.commerce.payment.infrastructure.FakePaymentResult
import com.minicommerce.commerce.payment.infrastructure.PaymentRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.willAnswer
import org.mockito.BDDMockito.willThrow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Experiments on the Phase 2 baseline: Payment calls Notification synchronously inside one transaction.
 * Failures and delays are injected only here, by replacing NotificationService with a test double.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PaymentNotificationCouplingTest(
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val notificationRepository: NotificationRepository,
    @Autowired private val paymentRepository: PaymentRepository,
    @Autowired private val orderRepository: OrderRepository,
    @Autowired private val memberRepository: MemberRepository,
) {

    @MockitoBean
    private lateinit var fakePaymentGateway: FakePaymentGateway

    @MockitoBean
    private lateinit var notificationService: NotificationService

    @BeforeEach
    fun setUp() {
        notificationRepository.deleteAll()
        paymentRepository.deleteAll()
        orderRepository.deleteAll()
        memberRepository.deleteAll()
        given(fakePaymentGateway.pay(anyLong(), anyLong())).willReturn(FakePaymentResult.approved())
    }

    @Test
    fun `notification failure rolls back the approved payment and the paid order status`() {
        // The order was committed by an earlier request; only this payment request's changes are at stake.
        val order = saveOrder()
        willThrow(IllegalStateException("notification store is down"))
            .given(notificationService).recordPaymentApproved(anyLong(), anyLong())

        // GlobalExceptionHandler has no generic handler, so the exception escapes the controller (HTTP 500 in a real server).
        assertThrows<Exception> { requestPayment(order.id) }

        assertEquals(OrderStatus.CREATED, orderRepository.findById(order.id).orElseThrow().status)
        assertTrue(paymentRepository.findAll().isEmpty())
        assertTrue(notificationRepository.findAll().isEmpty())
    }

    @Test
    fun `payment response waits until the synchronous notification call finishes`() {
        val order = saveOrder()
        val notificationStarted = CountDownLatch(1)
        val releaseNotification = CountDownLatch(1)
        willAnswer {
            notificationStarted.countDown()
            releaseNotification.await(10, TimeUnit.SECONDS)
            null
        }.given(notificationService).recordPaymentApproved(anyLong(), anyLong())

        val startedAt = System.nanoTime()
        val paymentCall = CompletableFuture.supplyAsync { requestPayment(order.id) }

        assertTrue(notificationStarted.await(10, TimeUnit.SECONDS), "notification was never called")
        Thread.sleep(300)
        assertFalse(paymentCall.isDone, "payment responded before the notification finished")
        // Not committed yet: another transaction still sees the order as CREATED.
        assertEquals(OrderStatus.CREATED, orderRepository.findById(order.id).orElseThrow().status)

        releaseNotification.countDown()
        val status = paymentCall.get(10, TimeUnit.SECONDS)
        val elapsedMillis = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startedAt)

        assertEquals(200, status)
        assertTrue(elapsedMillis >= 300, "payment took ${elapsedMillis}ms")
        assertEquals(OrderStatus.PAID, orderRepository.findById(order.id).orElseThrow().status)
        println("[latency experiment] payment response took ${elapsedMillis}ms while notification was blocked")
    }

    private fun requestPayment(orderId: Long): Int = mockMvc.post("/api/v1/payments") {
        contentType = MediaType.APPLICATION_JSON
        content = """{"orderId": $orderId}"""
    }.andReturn().response.status

    private fun saveOrder(): Order {
        val member = memberRepository.save(Member(email = "buyer@example.com", name = "buyer"))

        return orderRepository.save(
            Order(
                memberId = member.id,
                items = listOf(
                    OrderItem(productId = 1, productName = "Keyboard", orderPrice = 30000, quantity = 2),
                ),
            ),
        )
    }
}
