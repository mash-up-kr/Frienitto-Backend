package org.frienitto.manitto.service

import org.frienitto.manitto.domain.Mission
import org.frienitto.manitto.domain.User
import org.frienitto.manitto.domain.UserRoomMap
import org.frienitto.manitto.domain.constant.MissionStatus
import org.frienitto.manitto.domain.constant.MissionType
import org.frienitto.manitto.dto.*
import org.frienitto.manitto.exception.ResourceNotFoundException
import org.frienitto.manitto.repository.MissionRepository
import org.frienitto.manitto.repository.UserRoomMapRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.streams.toList

@Service
class UserRoomMapService(private val userRoomMapRepository: UserRoomMapRepository,
                         private val roomService: RoomService,
                         private val missionRepository: MissionRepository) {

    @Transactional(readOnly = true)
    fun getParticipantsByRoomId(roomId: Long): List<ParticipantDto> {
        return getByRoomId(roomId).stream()
                .map { ParticipantDto.of(it.userId, it.username, it.imageCode) }
                .toList()
    }

    @Transactional
    fun joinRoom(user: User, roomId: Long): Response<RoomDto> {
        val room = roomService.getById(roomId)

        userRoomMapRepository.save(UserRoomMap.newUserRoomMap(room.id!!, user.id!!, user.username, room.expiresDate, user.imageCode))
        val maps = getByRoomId(roomId)
        val participants = maps.stream()
                .map { ParticipantDto.of(it.userId, it.username, it.imageCode) }
                .toList()

        return Response(200, "방 입장 성공", RoomDto.from(room, participants))
    }

    @Transactional(readOnly = true)
    fun getByRoomId(roomId: Long): List<UserRoomMap> {
        val maps = userRoomMapRepository.findByRoomId(roomId)
        return if (maps.isEmpty()) throw ResourceNotFoundException() else maps
    }

    fun matchingStart(matchRequest: MatchRequest): Response<MatchResultDto> {
        val participants = matchRequest.participants
        var a = participants.drop(1) + participants[0]
        var b = participants.take(participants.size - 1) + participants[participants.size - 1]
        val missions = mutableListOf<Mission>()
        for (x in 0 until participants.size) {
            missions.add(Mission.newMission(a[x], b[x], matchRequest.roomId, MissionType.USER, MissionStatus.DELIVERY, "Test"))
        }
        missionRepository.saveAll(missions)
        return Response(200, "매치 성공", MatchResultDto(missions))
    }
}