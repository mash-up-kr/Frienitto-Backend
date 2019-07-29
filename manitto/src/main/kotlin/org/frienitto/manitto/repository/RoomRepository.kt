package org.frienitto.manitto.repository

import org.frienitto.manitto.domain.Room
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.repository.JpaRepository

interface RoomRepository : JpaRepository<Room, Long?> {

    fun findAll(page: PageRequest): Page<Room>

    fun findByTitle(title: String): Room?
}