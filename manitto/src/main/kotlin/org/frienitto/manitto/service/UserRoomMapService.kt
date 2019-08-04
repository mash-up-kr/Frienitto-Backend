package org.frienitto.manitto.service

import org.frienitto.manitto.domain.Mission
import org.frienitto.manitto.domain.Room
import org.frienitto.manitto.domain.User
import org.frienitto.manitto.domain.UserRoomMap
import org.frienitto.manitto.domain.constant.MissionStatus
import org.frienitto.manitto.domain.constant.MissionType
import org.frienitto.manitto.dto.*
import org.frienitto.manitto.exception.InvalidStatusException
import org.frienitto.manitto.exception.NonAuthorizationException
import org.frienitto.manitto.exception.NotSupportException
import org.frienitto.manitto.exception.ResourceNotFoundException
import org.frienitto.manitto.repository.MissionRepository
import org.frienitto.manitto.repository.RoomRepository
import org.frienitto.manitto.repository.UserRoomMapRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.streams.toList

//TODO RoomService와의 순환 의존성을 제거할 방법을 찾아봅니다.
@Service
class UserRoomMapService(private val userRoomMapRepository: UserRoomMapRepository,
                         private val roomRepository: RoomRepository,
                         private val missionRepository: MissionRepository) {

    @Transactional(readOnly = true)
    fun getParticipantsByRoomId(roomId: Long): List<ParticipantDto> {
        return getByRoomIdWithAll(roomId).stream()
                .map { ParticipantDto.of(it.user.id!!, it.username, it.imageCode) }
                .toList()
    }

    @Transactional
    fun joinRoomByTitle(user: User, request: RoomJoinRequest): RoomDto {
        val room = roomRepository.findByTitle(request.title) ?: throw ResourceNotFoundException()
        validateJoinable(room, request.code)
        userRoomMapRepository.save(UserRoomMap.newUserRoomMap(room, user))

        return RoomDto.from(room)
    }

    @Transactional
    fun joinRoomById(user: User, roomId: Long, code: String): RoomDto {
        val room = roomRepository.findById(roomId).orElseThrow { ResourceNotFoundException() }
        validateJoinable(room, code)
        userRoomMapRepository.save(UserRoomMap.newUserRoomMap(room, user))

        return RoomDto.from(room)
    }

    @Transactional(readOnly = true)
    fun getByRoomIdWithAll(roomId: Long): List<UserRoomMap> {
        val maps = userRoomMapRepository.findByRoomIdWithAllRelationship(roomId)
        return if (maps.isEmpty()) throw ResourceNotFoundException() else maps
    }

    @Transactional
    fun match(matchRequest: MatchRequest): MatchResultDto {
        val room = roomRepository.findById(matchRequest.roomId).orElseThrow { ResourceNotFoundException() }
        room.matched()
        roomRepository.save(room)

        if (matchRequest.type.isUserMission()) {
            return MatchResultDto(
                    room.id!!,
                    room.status,
                    matchUser(matchRequest).asSequence()
                            .map { MissionDto.from(it) }
                            .toList()
            )
        } else {
            throw NotSupportException()
        }
    }

    private fun validateJoinable(room: Room, code: String) {
        if (!room.isMatchingCode(code)) {
            throw NonAuthorizationException()
        }

        if (!room.isJoinableStatus()) {
            throw InvalidStatusException()
        }
    }

    private fun matchUser(matchRequest: MatchRequest): List<Mission> {
        val participants = getParticipantsByRoomId(matchRequest.roomId)
                .asSequence()
                .map { it.id }
                .toList()
                .shuffled()
                .toLongArray()
        val missions = mutableListOf<Mission>()

        for (index in 0 until participants.size) {
            if (index == participants.size - 1) {
                missions.add(Mission.newMission(participants[index], participants[0], matchRequest.roomId, MissionType.USER, MissionStatus.DELIVERY, ""))
                break
            }
            missions.add(Mission.newMission(participants[index], participants[index + 1], matchRequest.roomId, MissionType.USER, MissionStatus.DELIVERY, ""))
        }

        return missionRepository.saveAll(missions)
    }
}