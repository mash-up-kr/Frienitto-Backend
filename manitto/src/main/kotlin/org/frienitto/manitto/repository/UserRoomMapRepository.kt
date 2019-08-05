package org.frienitto.manitto.repository

import org.frienitto.manitto.domain.Room
import org.frienitto.manitto.domain.UserRoomMap
import org.springframework.data.jpa.repository.JpaRepository

interface UserRoomMapRepository : JpaRepository<UserRoomMap, Long?>, UserRoomMapCustomRepository {

    fun findByRoomId(roomId: Long): List<UserRoomMap>
    fun findByUserIdAndRoomId(userId: Long, roomId: Long): UserRoomMap?
}