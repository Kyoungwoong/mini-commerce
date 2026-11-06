package com.minicommerce.commerce.notification

import com.minicommerce.commerce.member.domain.Member
import com.minicommerce.commerce.member.infrastructure.MemberRepository
import com.minicommerce.commerce.notification.domain.NotificationType
import com.minicommerce.commerce.notification.infrastructure.NotificationRepository
import com.minicommerce.commerce.order.domain.Order
import com.minicommerce.commerce.order.domain.OrderItem
import com.minicommerce.commerce.order.domain.OrderStatus
import com.minicommerce.commerce.order.infrastructure.OrderRepository
import com.minicommerce.commerce.payment.domain.PaymentStatus
import com.minicommerce.commerce.payment.infrastructure.FakePaymentGateway
import com.minicommerce.commerce.payment.infrastructure.FakePaymentResult
import com.minicommerce.commerce.payment.infrastructure.PaymentRepository
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class NotificationApiIntegrationTest(
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val notificationRepository: NotificationRepository,
    @Autowired private val paymentRepository: PaymentRepository,
    @Autowired private val orderRepository: OrderRepository,
    @Autowired private val memberRepository: MemberRepository,
) {

    @MockitoBean
    private lateinit var fakePaymentGateway: FakePaymentGateway

    @BeforeEach
    fun setUp() {
        notificationRepository.deleteAll()
        paymentRepository.deleteAll()
        orderRepository.deleteAll()
        memberRepository.deleteAll()
    }

    @Test
    fun `approved payment records exactly one notification for the order and member`() {
        val order = saveOrder()
        given(fakePaymentGateway.pay(anyLong(), anyLong())).willReturn(FakePaymentResult.approved())

        requestPayment(order.id)

        assertEquals(OrderStatus.PAID, orderRepository.findById(order.id).orElseThrow().status)
        assertEquals(PaymentStatus.APPROVED, paymentRepository.findAll().single().status)

        val notification = notificationRepository.findAll().single()
        assertEquals(order.memberId, notification.memberId)
        assertEquals(order.id, notification.orderId)
        assertEquals(NotificationType.PAYMENT_APPROVED, notification.type)
    }

    @Test
    fun `rejected payment records no payment approved notification`() {
        val order = saveOrder()
        given(fakePaymentGateway.pay(anyLong(), anyLong()))
            .willReturn(FakePaymentResult.rejected("FAKE_PAYMENT_FAILED"))

        requestPayment(order.id)

        assertEquals(OrderStatus.PAYMENT_FAILED, orderRepository.findById(order.id).orElseThrow().status)
        assertEquals(PaymentStatus.FAILED, paymentRepository.findAll().single().status)
        assertTrue(notificationRepository.findAll().isEmpty())
    }

    @Test
    fun `finds recorded notifications by member`() {
        val order = saveOrder()
        given(fakePaymentGateway.pay(anyLong(), anyLong())).willReturn(FakePaymentResult.approved())
        requestPayment(order.id)

        mockMvc.get("/api/v1/notifications") {
            param("memberId", order.memberId.toString())
        }.andExpect {
            status { isOk() }
            jsonPath("$.success", `is`(true))
            jsonPath("$.data", hasSize<Any>(1))
            jsonPath("$.data[0].memberId", `is`(order.memberId.toInt()))
            jsonPath("$.data[0].orderId", `is`(order.id.toInt()))
            jsonPath("$.data[0].type", `is`("PAYMENT_APPROVED"))
            jsonPath("$.data[0].message", `is`("주문 ${order.id}의 결제가 승인되었습니다."))
        }
    }

    private fun requestPayment(orderId: Long) {
        mockMvc.post("/api/v1/payments") {
            contentType = MediaType.APPLICATION_JSON
            content = """{"orderId": $orderId}"""
        }.andExpect {
            status { isOk() }
        }
    }

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
