package com.minicommerce.commerce.member.domain

sealed class MemberException(
    val errorCode: MemberErrorCode,
) : RuntimeException(errorCode.message)

class DuplicateMemberEmailException : MemberException(MemberErrorCode.MEMBER_EMAIL_DUPLICATED)

class MemberNotFoundException : MemberException(MemberErrorCode.MEMBER_NOT_FOUND)
