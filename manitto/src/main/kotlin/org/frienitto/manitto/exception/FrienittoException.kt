package org.frienitto.manitto.exception

import org.springframework.http.HttpStatus
import org.springframework.mail.MailException

open class FrienittoException(val errorCode: Int, val errorMsg: String) : RuntimeException(errorMsg)

class NonAuthorizationException(errorCode: Int = HttpStatus.UNAUTHORIZED.value(), errorMsg: String = HttpStatus.UNAUTHORIZED.reasonPhrase) : FrienittoException(errorCode, errorMsg)

class InvalidStatusException(errorCode: Int = HttpStatus.CONFLICT.value(), errorMsg: String = HttpStatus.CONFLICT.reasonPhrase) : FrienittoException(errorCode, errorMsg)

class ResourceNotFoundException(errorCode: Int = HttpStatus.NOT_FOUND.value(), errorMsg: String = HttpStatus.NOT_FOUND.reasonPhrase) : FrienittoException(errorCode, errorMsg)

class NotSupportException(errorCode: Int = 5003, errorMsg: String = "Not support function now") : FrienittoException(errorCode, errorMsg)

class MailException(errorCode: Int = 5004, errorMsg: String = "Mail Send Failed") : MailException("")

class BadRequestException(errorCode: Int = HttpStatus.BAD_REQUEST.value(), errorMsg: String = HttpStatus.BAD_REQUEST.reasonPhrase) : FrienittoException(errorCode, errorMsg)

class DuplicateDataException(errorCode: Int = HttpStatus.CONFLICT.value(), errorMsg: String = HttpStatus.CONFLICT.reasonPhrase) : FrienittoException(errorCode, errorMsg)