package com.minicommerce.commerce.member.application

import com.minicommerce.commerce.member.domain.DuplicateMemberEmailException
import com.minicommerce.commerce.member.domain.Member
import com.minicommerce.commerce.member.domain.MemberNotFoundException
import com.minicommerce.commerce.member.dto.MemberResponse
import com.minicommerce.commerce.member.dto.MockLoginResponse
import com.minicommerce.commerce.member.dto.SignupMemberRequest
import com.minicommerce.commerce.member.infrastructure.MemberRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberService(
    private val memberRepository: MemberRepository,
) {

    @Transactional
    fun signup(request: SignupMemberRequest): MemberResponse {
        if (memberRepository.existsByEmail(request.email)) {
            throw DuplicateMemberEmailException()
        }

        val member = memberRepository.save(
            Member(
                email = request.email,
                name = request.name,
            ),
        )

        return MemberResponse.from(member)
    }

    @Transactional(readOnly = true)
    fun findById(memberId: Long): MemberResponse {
        val member = memberRepository.findById(memberId)
            .orElseThrow { MemberNotFoundException() }

        return MemberResponse.from(member)
    }

    @Transactional(readOnly = true)
    fun mockLogin(email: String): MockLoginResponse {
        val member = memberRepository.findByEmail(email)
            ?: throw MemberNotFoundException()

        return MockLoginResponse(
            memberId = member.id,
            mockAccessToken = "mock-token-${member.id}",
        )
    }
}
