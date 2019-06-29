package org.frienitto.manitto.service

import org.frienitto.manitto.domain.Participant
import org.frienitto.manitto.repository.UsersRoomRepository
import org.springframework.stereotype.Service

@Service
class UsersRoomMapService(private val roomMapRepository: UsersRoomRepository) {

    fun getParticipantBy(roomId: Long): List<Participant>{
        val room = roomMapRepository.findById(roomId)
        return room.map { Participant(it.id, it.username, it.) }
    }
}