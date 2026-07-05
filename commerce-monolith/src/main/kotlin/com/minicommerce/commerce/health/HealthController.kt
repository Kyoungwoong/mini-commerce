package com.minicommerce.commerce.health

import com.minicommerce.common.response.ApiResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HealthController {

    @GetMapping("/health")
    fun health(): ApiResponse<String> = ApiResponse.success("OK")
}
