package org.frienitto.manitto.repository

import org.frienitto.manitto.domain.Room
import org.springframework.data.jpa.repository.JpaRepository

interface RoomRepository : JpaRepository<Room, Long?> {

    fun findByTitle(title: String): Room?
}