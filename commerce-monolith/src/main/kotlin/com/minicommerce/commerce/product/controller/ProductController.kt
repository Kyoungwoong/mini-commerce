package com.minicommerce.commerce.product.controller

import com.minicommerce.commerce.product.application.ProductService
import com.minicommerce.commerce.product.dto.ProductResponse
import com.minicommerce.commerce.product.dto.RegisterProductRequest
import com.minicommerce.common.response.ApiResponse
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/products")
class ProductController(
    private val productService: ProductService,
) {

    @PostMapping
    fun register(
        @Valid @RequestBody request: RegisterProductRequest,
    ): ApiResponse<ProductResponse> = ApiResponse.success(productService.register(request))

    @GetMapping
    fun findAll(): ApiResponse<List<ProductResponse>> = ApiResponse.success(productService.findAll())

    @GetMapping("/{productId}")
    fun findById(
        @PathVariable productId: Long,
    ): ApiResponse<ProductResponse> = ApiResponse.success(productService.findById(productId))
}
