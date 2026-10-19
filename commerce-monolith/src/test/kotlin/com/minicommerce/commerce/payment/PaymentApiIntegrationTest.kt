package com.minicommerce.commerce.payment

import com.minicommerce.commerce.member.domain.Member
import com.minicommerce.commerce.member.infrastructure.MemberRepository
import com.minicommerce.commerce.order.domain.Order
import com.minicommerce.commerce.order.domain.OrderItem
import com.minicommerce.commerce.order.domain.OrderStatus
import com.minicommerce.commerce.order.infrastructure.OrderRepository
import com.minicommerce.commerce.payment.infrastructure.PaymentRepository
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
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
) {

    @BeforeEach
    fun setUp() {
        paymentRepository.deleteAll()
        orderRepository.deleteAll()
        memberRepository.deleteAll()
    }

    @Test
    fun `approves payment`() {
        val order = saveOrder()

        mockMvc.post("/api/v1/payments") {
            contentType = MediaType.APPLICATION_JSON
            content = paymentRequest(order.id, shouldFail = false)
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

        requestPayment(order.id, shouldFail = false)

        val updatedOrder = orderRepository.findById(order.id).orElseThrow()
        kotlin.test.assertEquals(OrderStatus.PAID, updatedOrder.status)
    }

    @Test
    fun `fails payment as business result`() {
        val order = saveOrder()

        mockMvc.post("/api/v1/payments") {
            contentType = MediaType.APPLICATION_JSON
            content = paymentRequest(order.id, shouldFail = true)
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

        requestPayment(order.id, shouldFail = true)

        val updatedOrder = orderRepository.findById(order.id).orElseThrow()
        kotlin.test.assertEquals(OrderStatus.PAYMENT_FAILED, updatedOrder.status)
    }

    @Test
    fun `finds payment detail`() {
        val order = saveOrder()
        val paymentId = requestPayment(order.id, shouldFail = false)

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
        mockMvc.post("/api/v1/payments") {
            contentType = MediaType.APPLICATION_JSON
            content = paymentRequest(orderId = 999999, shouldFail = false)
        }.andExpect {
            status { isNotFound() }
            jsonPath("$.success", `is`(false))
            jsonPath("$.error.code", `is`("ORDER_NOT_FOUND"))
        }
    }

    @Test
    fun `payment request for already paid order returns error`() {
        val order = saveOrder()
        requestPayment(order.id, shouldFail = false)

        mockMvc.post("/api/v1/payments") {
            contentType = MediaType.APPLICATION_JSON
            content = paymentRequest(order.id, shouldFail = false)
        }.andExpect {
            status { isBadRequest() }
            jsonPath("$.success", `is`(false))
            jsonPath("$.error.code", `is`("ORDER_NOT_PAYABLE"))
        }
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

    private fun requestPayment(orderId: Long, shouldFail: Boolean): Long {
        val responseBody = mockMvc.post("/api/v1/payments") {
            contentType = MediaType.APPLICATION_JSON
            content = paymentRequest(orderId, shouldFail)
        }.andReturn().response.getContentAsString()

        return Regex(""""paymentId":(\d+)""")
            .find(responseBody)
            ?.groupValues
            ?.get(1)
            ?.toLong()
            ?: error("paymentId was not returned")
    }

    private fun paymentRequest(orderId: Long, shouldFail: Boolean): String = """
        {
          "orderId": $orderId,
          "shouldFail": $shouldFail
        }
    """.trimIndent()
}
