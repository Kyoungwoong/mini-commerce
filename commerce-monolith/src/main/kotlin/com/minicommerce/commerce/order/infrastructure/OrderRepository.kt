package com.minicommerce.commerce.order.infrastructure

import com.minicommerce.commerce.order.domain.Order
import org.springframework.data.jpa.repository.JpaRepository

interface OrderRepository : JpaRepository<Order, Long> {

    fun findAllByMemberIdOrderByIdAsc(memberId: Long): List<Order>
}
