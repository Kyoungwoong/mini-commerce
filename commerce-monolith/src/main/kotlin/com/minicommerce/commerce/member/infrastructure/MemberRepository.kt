package com.minicommerce.commerce.member.infrastructure

import com.minicommerce.commerce.member.domain.Member
import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository : JpaRepository<Member, Long> {

    fun existsByEmail(email: String): Boolean

    fun findByEmail(email: String): Member?
}
