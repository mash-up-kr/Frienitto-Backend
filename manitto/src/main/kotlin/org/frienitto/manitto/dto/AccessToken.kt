package org.frienitto.manitto.dto

import io.swagger.annotations.Api
import org.frienitto.manitto.util.generateToken
import java.time.LocalDate
@Api(value="접근 토큰", description = "API에 접근하기 위한 토큰")
data class AccessToken(val token: String, val tokenExpiresDate: LocalDate) {

    companion object {
        private val AFTER_EXPIRED: Long = 6

        fun newToken(key: String): AccessToken {
            return AccessToken(generateToken(key, 32), LocalDate.now().plusMonths(AFTER_EXPIRED))
        }
    }
}