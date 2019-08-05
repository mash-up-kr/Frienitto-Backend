package org.frienitto.manitto.service

import org.frienitto.manitto.domain.Room
import org.frienitto.manitto.domain.User
import org.frienitto.manitto.domain.UserRoomMap
import org.frienitto.manitto.dto.ParticipantDto
import org.frienitto.manitto.exception.ResourceNotFoundException
import org.frienitto.manitto.repository.UserRoomMapRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.streams.toList

@Service
class UserRoomMapService(private val userRoomMapRepository: UserRoomMapRepository) {

    @Transactional(readOnly = true)
    fun getParticipantsByRoomId(roomId: Long): List<ParticipantDto> {
        return getByRoomIdWithAll(roomId).stream()
                .map { ParticipantDto.of(it.user.id!!, it.user.username, it.user.imageCode, it.user.description) }
                .toList()
    }

    @Transactional(readOnly = true)
    fun getByRoomIdWithAll(roomId: Long): List<UserRoomMap> {
        val maps = userRoomMapRepository.findByRoomIdWithAllRelationship(roomId)
        return if (maps.isEmpty()) throw ResourceNotFoundException() else maps
    }

    @Transactional
    fun connect(user: User, room: Room): UserRoomMap {
        return userRoomMapRepository.save(UserRoomMap.newUserRoomMap(room, user))
    }

    @Transactional
    fun disconnect(userId: Long, roomId: Long) {
        val entity = userRoomMapRepository.findByUserIdAndRoomId(userId, roomId) ?: return
        userRoomMapRepository.delete(entity)
    }

    @Transactional
    fun disconnectAllByRoomId(roomId: Long) {
        userRoomMapRepository.deleteByRoomId(roomId)
    }
}