package org.frienitto.manitto.repository

import org.frienitto.manitto.domain.UserRoomMap
import org.springframework.data.jpa.repository.JpaRepository

interface UserRoomMapRepository : JpaRepository<UserRoomMap, Long?>, UserRoomMapCustomRepository {

    fun findByRoomId(roomId: Long): List<UserRoomMap>
}