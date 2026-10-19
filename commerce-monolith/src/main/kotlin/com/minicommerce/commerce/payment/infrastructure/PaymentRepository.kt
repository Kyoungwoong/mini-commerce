package com.minicommerce.commerce.payment.infrastructure

import com.minicommerce.commerce.payment.domain.Payment
import org.springframework.data.jpa.repository.JpaRepository

interface PaymentRepository : JpaRepository<Payment, Long>
