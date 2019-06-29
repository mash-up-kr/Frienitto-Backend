package org.frienitto.manitto.service

import org.slf4j.LoggerFactory
import org.springframework.mail.MailException
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service

@Service
class EmailService(private val javaMailSender: JavaMailSender) {

    companion object {
        private val log = LoggerFactory.getLogger(EmailService::class.java)
    }

    //TODO to 파라미터 이메일 포맷 validation 추가
    @Async
    fun send(title: String, msg: String, to: String) {
        val message = SimpleMailMessage().apply {
            this.setTo(to)
            this.setSubject(title)
            this.setText(msg)
        }

        try {
            javaMailSender.send(message)
        } catch (e: MailException) {
            log.error("send email error to: $to, title: $title", e)
        }
    }
}
