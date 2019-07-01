package org.frienitto.manitto.dto

import org.frienitto.manitto.domain.Mission
import org.frienitto.manitto.domain.Room
import org.frienitto.manitto.domain.User
import java.time.LocalDate
import javax.validation.constraints.NotBlank

data class RoomDto(val id: Long, @get: NotBlank val title: String, val expiresDate: LocalDate, val url: String, val participants: List<UserDto>) {

    companion object {
        fun from(room: Room, participants: List<UserDto>): RoomDto {
            return RoomDto(room.id!!, room.title, room.expiresDate, room.url, participants)
        }
    }
}

data class MatchResultDto(val missions: List<Mission>)

data class RegisterToken(val registerToken: String)

data class UserDto(
        val id: Long? = null,
        val username: String? = null,
        val nickname: String? = null,
        val description: String? = null,
        val imageCode: Int? = null,
        val email: String? = null
) {

    companion object {
        fun from(user: User): UserDto {
            return UserDto(user.id, user.username, user.nickname, user.description, user.imageCode, user.email)
        }

        fun of(id: Long, username: String, imageCode: Int): UserDto {
            return UserDto(id = id, username = username, imageCode = imageCode)
        }
    }
}