package org.frienitto.manitto.dto

import java.time.LocalDate
import javax.validation.constraints.Future
import javax.validation.constraints.NotBlank

data class MatchRequest(val roomId:Long, val participants: List<Long>)

data class IssueCodeRequest(val receiverInfo: String, val type: String)

data class VerifyCodeRequest(val receiverInfo: String, val type: String, val code: String)

data class RoomRequest(
        @get:NotBlank
        val name:String,
        @get:NotBlank
        val code:String,
        @get:Future
        val expiresDate: LocalDate
)

data class SignUpDto(val username: String, val description: String, val imageCode: Int, val email: String, val password: String)

data class SignInDto(val email: String, val password: String)