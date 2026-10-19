package com.minicommerce.commerce.product.infrastructure

import com.minicommerce.commerce.product.domain.Product
import org.springframework.data.jpa.repository.JpaRepository

interface ProductRepository : JpaRepository<Product, Long> {

    fun existsByName(name: String): Boolean

    fun findAllByOrderByIdAsc(): List<Product>
}
