package org.frienitto.manitto.dto

import org.frienitto.manitto.domain.Mission
import org.frienitto.manitto.domain.Room
import org.frienitto.manitto.domain.User
import java.time.LocalDate
import javax.validation.constraints.NotBlank

data class RoomDto(val id: Long, @get: NotBlank val title: String, val expiresDate: LocalDate, val url: String, val participants: List<ParticipantDto>?) {

    companion object {

        @JvmStatic
        fun from(room: Room, participants: List<ParticipantDto>?): RoomDto {
            return RoomDto(room.id!!, room.title, room.expiresDate, room.url, participants)
        }

        @JvmStatic
        fun from(room: Room): RoomDto {
            return from(room, null)
        }
    }
}

data class MatchResultDto(val missions: List<Mission>)

data class RegisterToken(val registerToken: String)

data class UserDto(
        val id: Long,
        val username: String,
        val nickname: String,
        val description: String,
        val imageCode: Int,
        val email: String
) {

    companion object {
        fun from(user: User): UserDto {
            return UserDto(user.id!!, user.username, user.nickname ?: "", user.description, user.imageCode, user.email)
        }
    }
}

data class ParticipantDto(val id: Long, val username: String, val imageCode: Int) {

    companion object {
        fun from(user: User): ParticipantDto {
            return ParticipantDto(user.id!!, user.username, user.imageCode)
        }

        fun of(id: Long, username: String, imageCode: Int): ParticipantDto {
            return ParticipantDto(id, username, imageCode)
        }
    }
}