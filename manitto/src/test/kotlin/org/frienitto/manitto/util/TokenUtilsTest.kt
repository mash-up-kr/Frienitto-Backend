package org.frienitto.manitto.util

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TokenUtilsTest {

    @Test
    fun `토큰 길이 테스트`() {
        assertEquals(8, generateToken(System.currentTimeMillis().toString(), 8).length)
    }
}