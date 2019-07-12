package org.frienitto.manitto.exception

import org.frienitto.manitto.exception.model.ErrorInfo
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(value = [NonAuthorizationException::class])
    fun nonAuthExceptionHandler(exception: NonAuthorizationException): ResponseEntity<ErrorInfo> {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ErrorInfo(exception.errorCode, exception.errorMsg))
    }

    @ExceptionHandler(value = [ResourceNotFoundException::class])
    fun resourceNotFoundExceptionHandler(exception: ResourceNotFoundException): ResponseEntity<ErrorInfo> {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ErrorInfo(exception.errorCode, exception.errorMsg))
    }

    @ExceptionHandler(value = [NotSupportException::class])
    fun notSupportExceptionHandler(exception: NotSupportException): ResponseEntity<ErrorInfo> {
        return ResponseEntity.ok(ErrorInfo(exception.errorCode, exception.errorMsg))
    }
}