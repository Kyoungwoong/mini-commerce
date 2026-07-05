package com.minicommerce.commerce.member.domain

enum class MemberErrorCode(
    val code: String,
    val message: String,
) {
    MEMBER_EMAIL_DUPLICATED(
        code = "MEMBER_EMAIL_DUPLICATED",
        message = "Member email is already registered.",
    ),
    MEMBER_NOT_FOUND(
        code = "MEMBER_NOT_FOUND",
        message = "Member was not found.",
    ),
}
