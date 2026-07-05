package com.minicommerce.commerce.member.infrastructure

import com.minicommerce.commerce.member.domain.Member
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Profile("!test")
class MemberDataInitializer(
    private val memberRepository: MemberRepository,
) : ApplicationRunner {

    @Transactional
    override fun run(args: ApplicationArguments) {
        if (memberRepository.existsByEmail("hiro@example.com")) {
            return
        }

        memberRepository.save(
            Member(
                email = "hiro@example.com",
                name = "hiro",
            ),
        )
    }
}
