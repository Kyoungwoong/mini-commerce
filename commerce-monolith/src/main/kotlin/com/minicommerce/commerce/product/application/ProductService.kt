package com.minicommerce.commerce.product.application

import com.minicommerce.commerce.product.domain.Product
import com.minicommerce.commerce.product.domain.ProductNotFoundException
import com.minicommerce.commerce.product.dto.ProductResponse
import com.minicommerce.commerce.product.dto.RegisterProductRequest
import com.minicommerce.commerce.product.infrastructure.ProductRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProductService(
    private val productRepository: ProductRepository,
) {

    @Transactional
    fun register(request: RegisterProductRequest): ProductResponse {
        val product = productRepository.save(
            Product(
                name = request.name,
                price = request.price,
                stockQuantity = request.stockQuantity,
            ),
        )

        return ProductResponse.from(product)
    }

    @Transactional(readOnly = true)
    fun findAll(): List<ProductResponse> = productRepository.findAllByOrderByIdAsc()
        .map(ProductResponse::from)

    @Transactional(readOnly = true)
    fun findById(productId: Long): ProductResponse {
        val product = productRepository.findById(productId)
            .orElseThrow { ProductNotFoundException() }

        return ProductResponse.from(product)
    }
}
