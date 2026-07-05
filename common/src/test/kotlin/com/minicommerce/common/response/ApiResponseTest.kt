package com.minicommerce.common.response

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ApiResponseTest {

    @Test
    fun `creates successful common response`() {
        val response = ApiResponse.success("OK")

        assertTrue(response.success)
        assertEquals("OK", response.data)
        assertNull(response.error)
    }
}
