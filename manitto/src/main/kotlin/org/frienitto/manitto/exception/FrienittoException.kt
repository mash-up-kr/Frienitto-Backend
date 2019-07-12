package org.frienitto.manitto.exception

import org.springframework.http.HttpStatus

open class FrienittoException(val errorCode: Int, val errorMsg: String) : RuntimeException(errorMsg)

class NonAuthorizationException(errorCode: Int = HttpStatus.UNAUTHORIZED.value(), errorMsg: String = HttpStatus.UNAUTHORIZED.reasonPhrase) : FrienittoException(errorCode, errorMsg)

class ResourceNotFoundException(errorCode: Int = HttpStatus.NOT_FOUND.value(), errorMsg: String = HttpStatus.NOT_FOUND.reasonPhrase) : FrienittoException(errorCode, errorMsg)

class NotSupportException(errorCode: Int = 5003, errorMsg: String = "Not support function now"): FrienittoException(errorCode, errorMsg)