package org.frienitto.manitto.dto

import org.frienitto.manitto.domain.constant.MissionType
import java.time.LocalDate
import javax.validation.constraints.Future
import javax.validation.constraints.NotBlank

data class MatchRequest(val roomId:Long, val ownerId: Long, val type: MissionType)

data class IssueCodeRequest(val receiverInfo: String, val type: String)

data class VerifyCodeRequest(val receiverInfo: String, val type: String, val code: String)

data class RoomCreateRequest(
        @get:NotBlank
        val title: String,
        @get:NotBlank
        val code:String,
        @get:Future
        val expiresDate: LocalDate
)

data class RoomRetrieveRequest(val userToken: String, val roomId: Long? = null, val title: String? = null)

data class RoomJoinRequest(val title: String, val code: String)

data class RoomExitRequest(val title: String)

data class SignUpDto(val username: String, val description: String, val imageCode: Int, val email: String, val password: String)

data class SignInDto(val email: String, val password: String)