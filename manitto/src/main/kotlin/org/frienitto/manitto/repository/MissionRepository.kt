package org.frienitto.manitto.repository

import org.frienitto.manitto.domain.Mission
import org.springframework.data.jpa.repository.JpaRepository

interface MissionRepository: JpaRepository<Mission, Long?> {

    fun findByRoomId(roomId: Long): List<Mission>
}