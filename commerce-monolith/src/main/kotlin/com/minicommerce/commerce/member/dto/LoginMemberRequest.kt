package com.minicommerce.commerce.member.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class LoginMemberRequest(
    @field:Email
    @field:NotBlank
    val email: String,
)
