package com.minicommerce.commerce.member

import com.minicommerce.commerce.member.infrastructure.MemberRepository
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.startsWith
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
class MemberApiIntegrationTest(
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val memberRepository: MemberRepository,
) {

    @BeforeEach
    fun setUp() {
        memberRepository.deleteAll()
    }

    @Test
    fun `signup creates member`() {
        mockMvc.post("/api/v1/members") {
            contentType = MediaType.APPLICATION_JSON
            content = """
                {
                  "email": "hiro@example.com",
                  "name": "hiro"
                }
            """.trimIndent()
        }.andExpect {
            status { isOk() }
            jsonPath("$.success", `is`(true))
            jsonPath("$.data.email", `is`("hiro@example.com"))
            jsonPath("$.data.name", `is`("hiro"))
        }
    }

    @Test
    fun `finds signed up member`() {
        val location = mockMvc.post("/api/v1/members") {
            contentType = MediaType.APPLICATION_JSON
            content = """
                {
                  "email": "hiro@example.com",
                  "name": "hiro"
                }
            """.trimIndent()
        }.andReturn().response.getContentAsString()

        val memberId = Regex(""""memberId":(\d+)""")
            .find(location)
            ?.groupValues
            ?.get(1)
            ?: error("memberId was not returned")

        mockMvc.get("/api/v1/members/$memberId")
            .andExpect {
                status { isOk() }
                jsonPath("$.success", `is`(true))
                jsonPath("$.data.memberId", `is`(memberId.toInt()))
                jsonPath("$.data.email", `is`("hiro@example.com"))
                jsonPath("$.data.name", `is`("hiro"))
            }
    }

    @Test
    fun `duplicate email returns error`() {
        val requestBody = """
            {
              "email": "hiro@example.com",
              "name": "hiro"
            }
        """.trimIndent()

        mockMvc.post("/api/v1/members") {
            contentType = MediaType.APPLICATION_JSON
            content = requestBody
        }.andExpect {
            status { isOk() }
        }

        mockMvc.post("/api/v1/members") {
            contentType = MediaType.APPLICATION_JSON
            content = requestBody
        }.andExpect {
            status { isConflict() }
            jsonPath("$.success", `is`(false))
            jsonPath("$.error.code", `is`("MEMBER_EMAIL_DUPLICATED"))
        }
    }

    @Test
    fun `mock login returns token for existing member`() {
        val signupResponse = mockMvc.post("/api/v1/members") {
            contentType = MediaType.APPLICATION_JSON
            content = """
                {
                  "email": "hiro@example.com",
                  "name": "hiro"
                }
            """.trimIndent()
        }.andReturn().response.getContentAsString()

        val memberId = Regex(""""memberId":(\d+)""")
            .find(signupResponse)
            ?.groupValues
            ?.get(1)
            ?: error("memberId was not returned")

        mockMvc.post("/api/v1/members/login") {
            contentType = MediaType.APPLICATION_JSON
            content = """
                {
                  "email": "hiro@example.com"
                }
            """.trimIndent()
        }.andExpect {
            status { isOk() }
            jsonPath("$.success", `is`(true))
            jsonPath("$.data.memberId", `is`(memberId.toInt()))
            jsonPath("$.data.mockAccessToken", startsWith("mock-token-"))
            jsonPath("$.data.mockAccessToken", `is`("mock-token-$memberId"))
        }
    }
}
