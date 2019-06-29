package org.frienitto.manitto.dto

import org.frienitto.manitto.util.generateToken
import java.time.LocalDate

data class AccessToken(val token: String, val tokenExpiresDate: LocalDate) {

    companion object {
        private val AFTER_EXPIRED: Long = 6

        fun newToken(key: String): AccessToken {
            return AccessToken(generateToken(key, 32), LocalDate.now().plusMonths(AFTER_EXPIRED))
        }
    }
}