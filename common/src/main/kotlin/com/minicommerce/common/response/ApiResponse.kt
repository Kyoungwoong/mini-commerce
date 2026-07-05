package com.minicommerce.common.response

data class ApiResponse<T>(
    val success: Boolean,
    val data: T? = null,
    val error: ErrorResponse? = null,
) {
    companion object {
        fun <T> success(data: T): ApiResponse<T> = ApiResponse(
            success = true,
            data = data,
        )

        fun failure(code: String, message: String): ApiResponse<Nothing> = ApiResponse(
            success = false,
            error = ErrorResponse(code = code, message = message),
        )
    }
}
