package org.frienitto.manitto.repository

import org.frienitto.manitto.domain.UserRoomMap
import org.springframework.data.jpa.repository.JpaRepository

interface UsersRoomRepository : JpaRepository<UserRoomMap, Long?>