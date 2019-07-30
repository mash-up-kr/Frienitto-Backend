package org.frienitto.manitto.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import org.frienitto.manitto.domain.Mission
import org.frienitto.manitto.domain.Room
import org.frienitto.manitto.domain.User
import org.frienitto.manitto.domain.constant.MissionStatus
import org.frienitto.manitto.domain.constant.MissionType
import org.frienitto.manitto.domain.constant.RoomStatus
import java.time.LocalDate
import javax.validation.constraints.NotBlank

data class RoomDto(
        val id: Long,
        @get: NotBlank
        val title: String,
        val status: RoomStatus,
        @get: JsonProperty("is_owner")
        val isOwner: Boolean?,
        val expiresDate: LocalDate,
        val participants: List<ParticipantDto>?) {

    companion object {

        @JvmStatic
        fun from(room: Room, isOwner: Boolean? = null, participants: List<ParticipantDto>? = null): RoomDto {
            return RoomDto(room.id!!, room.title, room.status, isOwner, room.expiresDate, participants)
        }
    }
}

data class MatchResultDto(val roomId: Long, val roomStatus: RoomStatus, val missions: List<MissionDto>)

data class MissionDto(val id: Long, val sourceId: Long, val targetId: Long, val description: String, val status: MissionStatus, val type: MissionType) {

    companion object {
        fun from(mission: Mission): MissionDto {
            return MissionDto(mission.id!!, mission.sourceId, mission.targetId, mission.description, mission.status, mission.type)
        }
    }
}

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