package org.frienitto.manitto.service

import org.frienitto.manitto.SpringTestSupport
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class EmailServiceTest : SpringTestSupport() {

    companion object {
        private val TITLE = "이메일 전송 테스트"
        private val TO = "gksxodnd007@naver.com"
        private val MSG = "이메일 전송 테스트"
    }

    @Autowired
    lateinit var emailService: EmailService

    @Test
    fun `이메일 전송 테스트`() {
        emailService.send(TITLE, MSG, TO)
    }
}