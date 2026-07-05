package com.minicommerce.commerce.common.error

import com.minicommerce.commerce.member.domain.DuplicateMemberEmailException
import com.minicommerce.commerce.member.domain.MemberException
import com.minicommerce.commerce.member.domain.MemberNotFoundException
import com.minicommerce.commerce.product.domain.ProductException
import com.minicommerce.commerce.product.domain.ProductNotFoundException
import com.minicommerce.common.response.ApiResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateMemberEmailException::class)
    fun handleDuplicateMemberEmail(exception: DuplicateMemberEmailException): ResponseEntity<ApiResponse<Nothing>> =
        handleMemberException(exception, HttpStatus.CONFLICT)

    @ExceptionHandler(MemberNotFoundException::class)
    fun handleMemberNotFound(exception: MemberNotFoundException): ResponseEntity<ApiResponse<Nothing>> =
        handleMemberException(exception, HttpStatus.NOT_FOUND)

    @ExceptionHandler(ProductNotFoundException::class)
    fun handleProductNotFound(exception: ProductNotFoundException): ResponseEntity<ApiResponse<Nothing>> =
        handleProductException(exception, HttpStatus.NOT_FOUND)

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleInvalidRequest(): ResponseEntity<ApiResponse<Nothing>> = ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(ApiResponse.failure("INVALID_REQUEST", "Request validation failed."))

    private fun handleMemberException(
        exception: MemberException,
        status: HttpStatus,
    ): ResponseEntity<ApiResponse<Nothing>> = ResponseEntity
        .status(status)
        .body(ApiResponse.failure(exception.errorCode.code, exception.errorCode.message))

    private fun handleProductException(
        exception: ProductException,
        status: HttpStatus,
    ): ResponseEntity<ApiResponse<Nothing>> = ResponseEntity
        .status(status)
        .body(ApiResponse.failure(exception.errorCode.code, exception.errorCode.message))
}
