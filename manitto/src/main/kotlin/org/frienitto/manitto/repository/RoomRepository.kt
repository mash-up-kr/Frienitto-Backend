package org.frienitto.manitto.repository

import org.frienitto.manitto.domain.Room
import org.frienitto.manitto.domain.User
import org.springframework.data.jpa.repository.JpaRepository

interface RoomRepository : JpaRepository<Room, Long?> {

}