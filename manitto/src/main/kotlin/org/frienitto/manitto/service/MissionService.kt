package org.frienitto.manitto.service

import org.frienitto.manitto.domain.Mission
import org.frienitto.manitto.domain.constant.MissionStatus
import org.frienitto.manitto.domain.constant.MissionType
import org.frienitto.manitto.dto.MatchRequest
import org.frienitto.manitto.dto.MatchResultDto
import org.frienitto.manitto.dto.MissionDto
import org.frienitto.manitto.dto.ParticipantDto
import org.frienitto.manitto.exception.BadRequestException
import org.frienitto.manitto.exception.NonAuthorizationException
import org.frienitto.manitto.exception.NotSupportException
import org.frienitto.manitto.repository.MissionRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MissionService(private val missionRepository: MissionRepository,
                     private val roomService: RoomService,
                     private val roomMapService: UserRoomMapService,
                     private val userService: UserService) {

    fun getUserMissionsByRoomId(roomId: Long): List<MissionDto> {
        val participants = roomMapService.getParticipantsByRoomId(roomId)
        val missions = missionRepository.findByRoomId(roomId)

        val maps = toParticipantsMap(participants)

        return missions.map { MissionDto.from(it, maps[it.sourceId]!!, maps[it.targetId]!!) }
    }

    @Transactional
    fun match(matchRequest: MatchRequest): MatchResultDto {
        val room = roomService.getRoomById(matchRequest.roomId)
        val user = userService.getUserById(matchRequest.ownerId)

        if (room.validateOwner(user)) {
            throw NonAuthorizationException()
        }

        val participants = roomMapService.getParticipantsByRoomId(matchRequest.roomId)

        if (participants.size < 2) {
            throw BadRequestException(errorMsg = "매칭을 시작하려면 매칭 인원이 2명 이상이어야 합니다.")
        }

        room.matched()
        val matchedRoom = roomService.save(room)

        if (matchRequest.type.isUserMission()) {

            return MatchResultDto(
                    matchedRoom.id!!,
                    matchedRoom.status,
                    matchUser(matchRequest, participants))
        } else {
            throw NotSupportException(errorMsg = "해당 요청에서 미션 타입이 적절하지 않습니다.")
        }
    }

    private fun matchUser(matchRequest: MatchRequest, participants: List<ParticipantDto>): List<MissionDto> {
        val maps: MutableMap<Long, ParticipantDto> = toParticipantsMap(participants)

        val participantIds = participants.asSequence()
                .map { it.id }
                .toList()
                .shuffled()
                .toLongArray()

        val missions = mutableListOf<Mission>()

        for (index in 0 until participantIds.size) {
            if (index == participantIds.size - 1) {
                missions.add(Mission.newMission(participantIds[index], participantIds[0], matchRequest.roomId, MissionType.USER, MissionStatus.DELIVERY, ""))
                break
            }
            missions.add(Mission.newMission(participantIds[index], participantIds[index + 1], matchRequest.roomId, MissionType.USER, MissionStatus.DELIVERY, ""))
        }

        missionRepository.saveAll(missions)

        return missions.map { MissionDto.from(it, maps[it.sourceId]!!, maps[it.targetId]!!) }
    }

    private fun toParticipantsMap(participants: List<ParticipantDto>): MutableMap<Long, ParticipantDto> {
        val maps: MutableMap<Long, ParticipantDto> = mutableMapOf()

        participants.forEach {
            maps[it.id] = it
        }

        return maps
    }
}