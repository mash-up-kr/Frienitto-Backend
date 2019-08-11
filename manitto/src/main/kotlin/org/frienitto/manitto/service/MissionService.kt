package org.frienitto.manitto.service

import org.frienitto.manitto.domain.Mission
import org.frienitto.manitto.domain.constant.MissionStatus
import org.frienitto.manitto.domain.constant.MissionType
import org.frienitto.manitto.dto.MatchRequest
import org.frienitto.manitto.dto.MatchResultDto
import org.frienitto.manitto.dto.MissionDto
import org.frienitto.manitto.exception.NotSupportException
import org.frienitto.manitto.exception.ResourceNotFoundException
import org.frienitto.manitto.repository.MissionRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MissionService(private val missionRepository: MissionRepository,
                     private val roomService: RoomService,
                     private val roomMapService: UserRoomMapService) {

    fun getUserMissionsByRoomId(roomId: Long): List<Mission> {
        return missionRepository.findByRoomId(roomId)
    }

    @Transactional
    fun match(matchRequest: MatchRequest): MatchResultDto {
        val room = roomService.getRoomById(matchRequest.roomId)

        room.matched()
        val matchedRoom = roomService.save(room)

        if (matchRequest.type.isUserMission()) {
            return MatchResultDto(
                    matchedRoom.id!!,
                    matchedRoom.status,
                    matchUser(matchRequest).asSequence()
                            .map { MissionDto.from(it) }
                            .toList()
            )
        } else {
            throw NotSupportException(errorMsg = "해당 요청에서 미션 타입이 적절하지 않습니다.")
        }
    }

    private fun matchUser(matchRequest: MatchRequest): List<Mission> {
        val participants = roomMapService.getParticipantsByRoomId(matchRequest.roomId)
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