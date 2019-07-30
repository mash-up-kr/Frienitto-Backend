package org.frienitto.manitto.service

import org.frienitto.manitto.domain.Room
import org.frienitto.manitto.domain.User
import org.frienitto.manitto.dto.RoomCreateRequest
import org.frienitto.manitto.dto.RoomDto
import org.frienitto.manitto.dto.RoomRetrieveRequest
import org.frienitto.manitto.exception.ResourceNotFoundException
import org.frienitto.manitto.repository.RoomRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RoomService(private val roomRepository: RoomRepository,
                  private val userRoomMapService: UserRoomMapService,
                  private val userService: UserService) {

    @Transactional
    fun createRoom(owner: User, createRequest: RoomCreateRequest): RoomDto {
        val room = roomRepository.save(Room.newRoom(owner, createRequest.title, createRequest.code, createRequest.expiresDate))
        userRoomMapService.joinRoomById(owner, room.id!!, room.code)

        return RoomDto.from(room)
    }

    //TODO 페이징 처리 해야함
    @Transactional(readOnly = true)
    fun getRoomList(): List<Room> {
        return roomRepository.findAll()
    }

    @Transactional(readOnly = true)
    fun getRoomDetailById(retrieveRequest: RoomRetrieveRequest): RoomDto {
        val room = roomRepository.findById(retrieveRequest.roomId).orElseThrow { ResourceNotFoundException() }
        val participants = userRoomMapService.getParticipantsByRoomId(retrieveRequest.roomId)
        val requestUser = userService.getUserByToken(retrieveRequest.userToken)

        return RoomDto.from(room = room, isOwner = room.validateOwner(requestUser), participants = participants)
    }
}