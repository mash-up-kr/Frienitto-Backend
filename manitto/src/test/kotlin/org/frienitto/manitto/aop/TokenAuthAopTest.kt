package org.frienitto.manitto.aop

import org.frienitto.manitto.SpringTestSupport
import org.frienitto.manitto.controller.v1.RoomController
import org.frienitto.manitto.exception.NonAuthorizationException
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class TokenAuthAopTest : SpringTestSupport() {

    companion object {
        const val TOKEN = "INVALID-TOKEN-99"
    }

    @Autowired
    lateinit var roomController: RoomController

    @Test
    fun `유효 하지 않은 토큰으로 api를 요청 했을 때`() {
        assertThrows(NonAuthorizationException::class.java) { roomController.getRoomList(TOKEN) }
    }
}