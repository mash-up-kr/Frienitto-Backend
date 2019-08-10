package org.frienitto.manitto.repository

import org.frienitto.manitto.domain.UserRoomMap

interface UserRoomMapCustomRepository {

    fun findByRoomIdWithAllRelationship(roomId: Long): List<UserRoomMap>
    fun findByUserIdWithAllRelationship(userId: Long): List<UserRoomMap>
    fun deleteByRoomId(roomId: Long): Long
}