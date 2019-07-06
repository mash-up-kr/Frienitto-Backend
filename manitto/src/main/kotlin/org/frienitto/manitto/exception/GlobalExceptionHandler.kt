package org.frienitto.manitto.exception

import org.frienitto.manitto.exception.model.ErrorInfo
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(value = [NonAuthorizationException::class])
    fun nonAuthExceptionHandler(exception: NonAuthorizationException): ErrorInfo {
        return ErrorInfo(exception.errorCode, exception.errorMsg)
    }

    @ExceptionHandler(value = [ResourceNotFoundException::class])
    fun resourceNotFoundExceptionHandler(exception: ResourceNotFoundException): ErrorInfo {
        return ErrorInfo(exception.errorCode, exception.errorMsg)
    }
}