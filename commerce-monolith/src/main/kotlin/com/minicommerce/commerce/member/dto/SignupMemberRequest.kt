package com.minicommerce.commerce.member.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class SignupMemberRequest(
    @field:Email
    @field:NotBlank
    val email: String,

    @field:NotBlank
    val name: String,
)
