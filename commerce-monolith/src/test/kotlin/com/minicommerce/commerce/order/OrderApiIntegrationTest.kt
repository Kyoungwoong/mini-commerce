package com.minicommerce.commerce.order

import com.minicommerce.commerce.member.domain.Member
import com.minicommerce.commerce.member.infrastructure.MemberRepository
import com.minicommerce.commerce.order.infrastructure.OrderRepository
import com.minicommerce.commerce.product.domain.Product
import com.minicommerce.commerce.product.infrastructure.ProductRepository
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
class OrderApiIntegrationTest(
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val orderRepository: OrderRepository,
    @Autowired private val memberRepository: MemberRepository,
    @Autowired private val productRepository: ProductRepository,
) {

    @BeforeEach
    fun setUp() {
        orderRepository.deleteAll()
        productRepository.deleteAll()
        memberRepository.deleteAll()
    }

    @Test
    fun `creates order`() {
        val member = saveMember()
        val product = saveProduct(stockQuantity = 10)

        mockMvc.post("/api/v1/orders") {
            contentType = MediaType.APPLICATION_JSON
            content = createOrderRequest(member.id, product.id, quantity = 2)
        }.andExpect {
            status { isOk() }
            jsonPath("$.success", `is`(true))
            jsonPath("$.data.memberId", `is`(member.id.toInt()))
            jsonPath("$.data.status", `is`("CREATED"))
            jsonPath("$.data.totalPrice", `is`(60000))
            jsonPath("$.data.items[0].productId", `is`(product.id.toInt()))
            jsonPath("$.data.items[0].productName", `is`("Keyboard"))
            jsonPath("$.data.items[0].orderPrice", `is`(30000))
            jsonPath("$.data.items[0].quantity", `is`(2))
        }
    }

    @Test
    fun `finds order detail`() {
        val member = saveMember()
        val product = saveProduct(stockQuantity = 10)
        val orderId = createOrder(member.id, product.id, quantity = 2)

        mockMvc.get("/api/v1/orders/$orderId")
            .andExpect {
                status { isOk() }
                jsonPath("$.success", `is`(true))
                jsonPath("$.data.orderId", `is`(orderId.toInt()))
                jsonPath("$.data.memberId", `is`(member.id.toInt()))
                jsonPath("$.data.totalPrice", `is`(60000))
                jsonPath("$.data.items[0].productName", `is`("Keyboard"))
                jsonPath("$.data.items[0].orderPrice", `is`(30000))
            }
    }

    @Test
    fun `lists orders by member`() {
        val member = saveMember()
        val product = saveProduct(stockQuantity = 10)
        val orderId = createOrder(member.id, product.id, quantity = 2)

        mockMvc.get("/api/v1/orders") {
            param("memberId", member.id.toString())
        }.andExpect {
            status { isOk() }
            jsonPath("$.success", `is`(true))
            jsonPath("$.data[0].orderId", `is`(orderId.toInt()))
            jsonPath("$.data[0].memberId", `is`(member.id.toInt()))
            jsonPath("$.data[0].status", `is`("CREATED"))
            jsonPath("$.data[0].totalPrice", `is`(60000))
        }
    }

    @Test
    fun `creating order with missing member returns error`() {
        val product = saveProduct(stockQuantity = 10)

        mockMvc.post("/api/v1/orders") {
            contentType = MediaType.APPLICATION_JSON
            content = createOrderRequest(memberId = 999999, productId = product.id, quantity = 2)
        }.andExpect {
            status { isNotFound() }
            jsonPath("$.success", `is`(false))
            jsonPath("$.error.code", `is`("MEMBER_NOT_FOUND"))
        }
    }

    @Test
    fun `creating order with missing product returns error`() {
        val member = saveMember()

        mockMvc.post("/api/v1/orders") {
            contentType = MediaType.APPLICATION_JSON
            content = createOrderRequest(memberId = member.id, productId = 999999, quantity = 2)
        }.andExpect {
            status { isNotFound() }
            jsonPath("$.success", `is`(false))
            jsonPath("$.error.code", `is`("PRODUCT_NOT_FOUND"))
        }
    }

    @Test
    fun `creating order with insufficient stock returns error`() {
        val member = saveMember()
        val product = saveProduct(stockQuantity = 1)

        mockMvc.post("/api/v1/orders") {
            contentType = MediaType.APPLICATION_JSON
            content = createOrderRequest(member.id, product.id, quantity = 2)
        }.andExpect {
            status { isBadRequest() }
            jsonPath("$.success", `is`(false))
            jsonPath("$.error.code", `is`("INSUFFICIENT_PRODUCT_STOCK"))
        }
    }

    @Test
    fun `creating order decreases product stock`() {
        val member = saveMember()
        val product = saveProduct(stockQuantity = 10)

        createOrder(member.id, product.id, quantity = 2)

        val updatedProduct = productRepository.findById(product.id).orElseThrow()
        kotlin.test.assertEquals(8, updatedProduct.stockQuantity)
    }

    private fun saveMember(): Member = memberRepository.save(
        Member(
            email = "buyer@example.com",
            name = "buyer",
        ),
    )

    private fun saveProduct(stockQuantity: Int): Product = productRepository.save(
        Product(
            name = "Keyboard",
            price = 30000,
            stockQuantity = stockQuantity,
        ),
    )

    private fun createOrder(memberId: Long, productId: Long, quantity: Int): Long {
        val responseBody = mockMvc.post("/api/v1/orders") {
            contentType = MediaType.APPLICATION_JSON
            content = createOrderRequest(memberId, productId, quantity)
        }.andReturn().response.getContentAsString()

        return Regex(""""orderId":(\d+)""")
            .find(responseBody)
            ?.groupValues
            ?.get(1)
            ?.toLong()
            ?: error("orderId was not returned")
    }

    private fun createOrderRequest(memberId: Long, productId: Long, quantity: Int): String = """
        {
          "memberId": $memberId,
          "items": [
            {
              "productId": $productId,
              "quantity": $quantity
            }
          ]
        }
    """.trimIndent()
}
