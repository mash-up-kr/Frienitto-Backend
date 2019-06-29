package org.frienitto.manitto.exception

import org.springframework.http.HttpStatus
import java.lang.RuntimeException

open class FrienittoException(val errorCode: Int, val errorMsg: String) : RuntimeException(errorMsg)

class NonAuthorizationException(errorCode: Int = HttpStatus.UNAUTHORIZED.value(), errorMsg: String = HttpStatus.UNAUTHORIZED.reasonPhrase) : FrienittoException(errorCode, errorMsg)