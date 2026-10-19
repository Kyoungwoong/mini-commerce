package com.minicommerce.commerce.product

import com.minicommerce.commerce.product.infrastructure.ProductRepository
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.greaterThan
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
class ProductApiIntegrationTest(
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val productRepository: ProductRepository,
) {

    @BeforeEach
    fun setUp() {
        productRepository.deleteAll()
    }

    @Test
    fun `registers product`() {
        mockMvc.post("/api/v1/products") {
            contentType = MediaType.APPLICATION_JSON
            content = keyboardRequest()
        }.andExpect {
            status { isOk() }
            jsonPath("$.success", `is`(true))
            jsonPath("$.data.productId", greaterThan(0))
            jsonPath("$.data.name", `is`("Keyboard"))
            jsonPath("$.data.price", `is`(30000))
            jsonPath("$.data.stockQuantity", `is`(100))
        }
    }

    @Test
    fun `lists registered products`() {
        registerKeyboard()

        mockMvc.get("/api/v1/products")
            .andExpect {
                status { isOk() }
                jsonPath("$.success", `is`(true))
                jsonPath("$.data[0].name", `is`("Keyboard"))
                jsonPath("$.data[0].price", `is`(30000))
                jsonPath("$.data[0].stockQuantity", `is`(100))
            }
    }

    @Test
    fun `finds product detail`() {
        val productId = registerKeyboard()

        mockMvc.get("/api/v1/products/$productId")
            .andExpect {
                status { isOk() }
                jsonPath("$.success", `is`(true))
                jsonPath("$.data.productId", `is`(productId.toInt()))
                jsonPath("$.data.name", `is`("Keyboard"))
                jsonPath("$.data.price", `is`(30000))
                jsonPath("$.data.stockQuantity", `is`(100))
            }
    }

    @Test
    fun `missing product returns error`() {
        mockMvc.get("/api/v1/products/999999")
            .andExpect {
                status { isNotFound() }
                jsonPath("$.success", `is`(false))
                jsonPath("$.error.code", `is`("PRODUCT_NOT_FOUND"))
            }
    }

    @Test
    fun `invalid product request returns error`() {
        mockMvc.post("/api/v1/products") {
            contentType = MediaType.APPLICATION_JSON
            content = """
                {
                  "name": "",
                  "price": 0,
                  "stockQuantity": -1
                }
            """.trimIndent()
        }.andExpect {
            status { isBadRequest() }
            jsonPath("$.success", `is`(false))
            jsonPath("$.error.code", `is`("INVALID_REQUEST"))
        }
    }

    private fun registerKeyboard(): Long {
        val responseBody = mockMvc.post("/api/v1/products") {
            contentType = MediaType.APPLICATION_JSON
            content = keyboardRequest()
        }.andReturn().response.getContentAsString()

        return Regex(""""productId":(\d+)""")
            .find(responseBody)
            ?.groupValues
            ?.get(1)
            ?.toLong()
            ?: error("productId was not returned")
    }

    private fun keyboardRequest(): String = """
        {
          "name": "Keyboard",
          "price": 30000,
          "stockQuantity": 100
        }
    """.trimIndent()
}
