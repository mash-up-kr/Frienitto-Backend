package org.frienitto.manitto.service

import org.frienitto.manitto.domain.Mission
import org.frienitto.manitto.repository.MissionRepository
import org.springframework.stereotype.Service

@Service
class MissionService(private val missionRepository: MissionRepository) {

    fun getUserMissionsByRoomId(roomId: Long): List<Mission> {
        return missionRepository.findByRoomId(roomId)
    }
}