package com.minicommerce.commerce.payment

import com.minicommerce.commerce.member.domain.Member
import com.minicommerce.commerce.member.infrastructure.MemberRepository
import com.minicommerce.commerce.notification.infrastructure.NotificationRepository
import com.minicommerce.commerce.order.domain.Order
import com.minicommerce.commerce.order.domain.OrderItem
import com.minicommerce.commerce.order.domain.OrderStatus
import com.minicommerce.commerce.order.infrastructure.OrderRepository
import com.minicommerce.commerce.payment.infrastructure.FakePaymentGateway
import com.minicommerce.commerce.payment.infrastructure.FakePaymentResult
import com.minicommerce.commerce.payment.infrastructure.PaymentRepository
import org.hamcrest.Matchers.`is`
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

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PaymentApiIntegrationTest(
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val paymentRepository: PaymentRepository,
    @Autowired private val orderRepository: OrderRepository,
    @Autowired private val memberRepository: MemberRepository,
    @Autowired private val notificationRepository: NotificationRepository,
) {

    // The server decides the payment result; tests replace the fake gateway to choose it.
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
    fun `approves payment`() {
        val order = saveOrder()
        gatewayApproves()

        mockMvc.post("/api/v1/payments") {
            contentType = MediaType.APPLICATION_JSON
            content = paymentRequest(order.id)
        }.andExpect {
            status { isOk() }
            jsonPath("$.success", `is`(true))
            jsonPath("$.data.orderId", `is`(order.id.toInt()))
            jsonPath("$.data.amount", `is`(60000))
            jsonPath("$.data.status", `is`("APPROVED"))
            jsonPath("$.data.orderStatus", `is`("PAID"))
        }
    }

    @Test
    fun `approved payment updates order status to paid`() {
        val order = saveOrder()
        gatewayApproves()

        requestPayment(order.id)

        val updatedOrder = orderRepository.findById(order.id).orElseThrow()
        kotlin.test.assertEquals(OrderStatus.PAID, updatedOrder.status)
    }

    @Test
    fun `fails payment as business result`() {
        val order = saveOrder()
        gatewayRejects()

        mockMvc.post("/api/v1/payments") {
            contentType = MediaType.APPLICATION_JSON
            content = paymentRequest(order.id)
        }.andExpect {
            status { isOk() }
            jsonPath("$.success", `is`(true))
            jsonPath("$.data.orderId", `is`(order.id.toInt()))
            jsonPath("$.data.amount", `is`(60000))
            jsonPath("$.data.status", `is`("FAILED"))
            jsonPath("$.data.orderStatus", `is`("PAYMENT_FAILED"))
        }
    }

    @Test
    fun `failed payment updates order status to payment failed`() {
        val order = saveOrder()
        gatewayRejects()

        val paymentId = requestPayment(order.id)

        val updatedOrder = orderRepository.findById(order.id).orElseThrow()
        val payment = paymentRepository.findById(paymentId).orElseThrow()
        kotlin.test.assertEquals(OrderStatus.PAYMENT_FAILED, updatedOrder.status)
        kotlin.test.assertEquals("FAKE_PAYMENT_FAILED", payment.failureReason)
    }

    @Test
    fun `client cannot choose payment result with legacy shouldFail field`() {
        val order = saveOrder()
        gatewayApproves()

        mockMvc.post("/api/v1/payments") {
            contentType = MediaType.APPLICATION_JSON
            content = """
                {
                  "orderId": ${order.id},
                  "shouldFail": true
                }
            """.trimIndent()
        }.andExpect {
            status { isOk() }
            jsonPath("$.data.status", `is`("APPROVED"))
            jsonPath("$.data.orderStatus", `is`("PAID"))
        }
    }

    @Test
    fun `finds payment detail`() {
        val order = saveOrder()
        gatewayApproves()
        val paymentId = requestPayment(order.id)

        mockMvc.get("/api/v1/payments/$paymentId")
            .andExpect {
                status { isOk() }
                jsonPath("$.success", `is`(true))
                jsonPath("$.data.paymentId", `is`(paymentId.toInt()))
                jsonPath("$.data.orderId", `is`(order.id.toInt()))
                jsonPath("$.data.amount", `is`(60000))
                jsonPath("$.data.status", `is`("APPROVED"))
            }
    }

    @Test
    fun `payment request for missing order returns error`() {
        gatewayApproves()

        mockMvc.post("/api/v1/payments") {
            contentType = MediaType.APPLICATION_JSON
            content = paymentRequest(orderId = 999999)
        }.andExpect {
            status { isNotFound() }
            jsonPath("$.success", `is`(false))
            jsonPath("$.error.code", `is`("ORDER_NOT_FOUND"))
        }
    }

    @Test
    fun `payment request for already paid order returns error`() {
        val order = saveOrder()
        gatewayApproves()
        requestPayment(order.id)

        mockMvc.post("/api/v1/payments") {
            contentType = MediaType.APPLICATION_JSON
            content = paymentRequest(order.id)
        }.andExpect {
            status { isBadRequest() }
            jsonPath("$.success", `is`(false))
            jsonPath("$.error.code", `is`("ORDER_NOT_PAYABLE"))
        }
    }

    private fun gatewayApproves() {
        given(fakePaymentGateway.pay(anyLong(), anyLong())).willReturn(FakePaymentResult.approved())
    }

    private fun gatewayRejects() {
        given(fakePaymentGateway.pay(anyLong(), anyLong()))
            .willReturn(FakePaymentResult.rejected("FAKE_PAYMENT_FAILED"))
    }

    private fun saveOrder(): Order {
        val member = memberRepository.save(
            Member(
                email = "buyer@example.com",
                name = "buyer",
            ),
        )

        return orderRepository.save(
            Order(
                memberId = member.id,
                items = listOf(
                    OrderItem(
                        productId = 1,
                        productName = "Keyboard",
                        orderPrice = 30000,
                        quantity = 2,
                    ),
                ),
            ),
        )
    }

    private fun requestPayment(orderId: Long): Long {
        val responseBody = mockMvc.post("/api/v1/payments") {
            contentType = MediaType.APPLICATION_JSON
            content = paymentRequest(orderId)
        }.andReturn().response.getContentAsString()

        return Regex(""""paymentId":(\d+)""")
            .find(responseBody)
            ?.groupValues
            ?.get(1)
            ?.toLong()
            ?: error("paymentId was not returned")
    }

    private fun paymentRequest(orderId: Long): String = """
        {
          "orderId": $orderId
        }
    """.trimIndent()
}
