package com.minicommerce.commerce.order.application

import com.minicommerce.commerce.member.domain.MemberNotFoundException
import com.minicommerce.commerce.member.infrastructure.MemberRepository
import com.minicommerce.commerce.order.domain.InvalidOrderQuantityException
import com.minicommerce.commerce.order.domain.Order
import com.minicommerce.commerce.order.domain.OrderItem
import com.minicommerce.commerce.order.domain.OrderNotFoundException
import com.minicommerce.commerce.order.dto.CreateOrderRequest
import com.minicommerce.commerce.order.dto.OrderResponse
import com.minicommerce.commerce.order.dto.OrderSummaryResponse
import com.minicommerce.commerce.order.infrastructure.OrderRepository
import com.minicommerce.commerce.product.domain.ProductNotFoundException
import com.minicommerce.commerce.product.infrastructure.ProductRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OrderService(
    private val orderRepository: OrderRepository,
    private val memberRepository: MemberRepository,
    private val productRepository: ProductRepository,
) {

    @Transactional
    fun create(request: CreateOrderRequest): OrderResponse {
        if (!memberRepository.existsById(request.memberId)) {
            throw MemberNotFoundException()
        }

        val orderItems = request.items.map { itemRequest ->
            if (itemRequest.quantity <= 0) {
                throw InvalidOrderQuantityException()
            }

            val product = productRepository.findById(itemRequest.productId)
                .orElseThrow { ProductNotFoundException() }

            product.decreaseStock(itemRequest.quantity)

            OrderItem(
                productId = product.id,
                productName = product.name,
                orderPrice = product.price,
                quantity = itemRequest.quantity,
            )
        }

        val order = orderRepository.save(
            Order(
                memberId = request.memberId,
                items = orderItems,
            ),
        )

        return OrderResponse.from(order)
    }

    @Transactional(readOnly = true)
    fun findById(orderId: Long): OrderResponse {
        val order = orderRepository.findById(orderId)
            .orElseThrow { OrderNotFoundException() }

        return OrderResponse.from(order)
    }

    @Transactional(readOnly = true)
    fun findAllByMemberId(memberId: Long): List<OrderSummaryResponse> {
        if (!memberRepository.existsById(memberId)) {
            throw MemberNotFoundException()
        }

        return orderRepository.findAllByMemberIdOrderByIdAsc(memberId)
            .map(OrderSummaryResponse::from)
    }
}
