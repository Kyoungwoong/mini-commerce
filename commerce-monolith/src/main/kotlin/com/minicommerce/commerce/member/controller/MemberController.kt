package com.minicommerce.commerce.member.controller

import com.minicommerce.commerce.member.application.MemberService
import com.minicommerce.commerce.member.dto.LoginMemberRequest
import com.minicommerce.commerce.member.dto.MemberResponse
import com.minicommerce.commerce.member.dto.MockLoginResponse
import com.minicommerce.commerce.member.dto.SignupMemberRequest
import com.minicommerce.common.response.ApiResponse
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/members")
class MemberController(
    private val memberService: MemberService,
) {

    @PostMapping
    fun signup(
        @Valid @RequestBody request: SignupMemberRequest,
    ): ApiResponse<MemberResponse> = ApiResponse.success(memberService.signup(request))

    @GetMapping("/{memberId}")
    fun findById(
        @PathVariable memberId: Long,
    ): ApiResponse<MemberResponse> = ApiResponse.success(memberService.findById(memberId))

    @PostMapping("/login")
    fun login(
        @Valid @RequestBody request: LoginMemberRequest,
    ): ApiResponse<MockLoginResponse> = ApiResponse.success(memberService.mockLogin(request.email))
}
