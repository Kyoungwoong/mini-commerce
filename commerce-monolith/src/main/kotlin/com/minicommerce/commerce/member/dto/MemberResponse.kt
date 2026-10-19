package com.minicommerce.commerce.member.dto

import com.minicommerce.commerce.member.domain.Member

data class MemberResponse(
    val memberId: Long,
    val email: String,
    val name: String,
) {
    companion object {
        fun from(member: Member): MemberResponse = MemberResponse(
            memberId = member.id,
            email = member.email,
            name = member.name,
        )
    }
}
