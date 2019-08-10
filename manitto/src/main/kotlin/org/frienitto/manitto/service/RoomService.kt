package org.frienitto.manitto.service

import org.frienitto.manitto.domain.Room
import org.frienitto.manitto.domain.User
import org.frienitto.manitto.dto.RoomCreateRequest
import org.frienitto.manitto.dto.RoomDto
import org.frienitto.manitto.dto.RoomJoinRequest
import org.frienitto.manitto.dto.RoomRetrieveRequest
import org.frienitto.manitto.exception.NonAuthorizationException
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
        val room = save(Room.newRoom(owner, createRequest.title, createRequest.code, createRequest.expiresDate))
        joinRoomById(owner, room.id!!, room.code)

        return RoomDto.from(room)
    }

    @Transactional
    fun deleteRoom(user: User, title: String) {
        val room = roomRepository.findByTitle(title) ?: return

        if (room.validateOwner(user)) {
            userRoomMapService.disconnectAllByRoomId(room.id!!)
            roomRepository.delete(room)
            return
        }

        userRoomMapService.disconnect(user.id!!, room.id!!)
    }

    @Transactional
    fun joinRoomByTitle(user: User, request: RoomJoinRequest): RoomDto {
        val room = roomRepository.findByTitle(request.title) ?: throw ResourceNotFoundException()

        if (room.code != request.code) {
            throw NonAuthorizationException()
        }

        userRoomMapService.connect(user, room)

        return RoomDto.from(room)
    }

    @Transactional
    fun joinRoomById(user: User, roomId: Long, code: String): RoomDto {
        val room = roomRepository.findById(roomId).orElseThrow { ResourceNotFoundException() }

        if (room.code != code) {
            throw NonAuthorizationException()
        }

        userRoomMapService.connect(user, room)

        return RoomDto.from(room)
    }

    //TODO 페이징 처리 해야함
    @Transactional(readOnly = true)
    fun getRoomList(): List<Room> {
        return roomRepository.findAll()
    }

    @Transactional(readOnly = true)
    fun getRoomInfoWithValidateOwner(retrieveRequest: RoomRetrieveRequest): RoomDto {
        val room = roomRepository.findById(retrieveRequest.roomId).orElseThrow { ResourceNotFoundException() }
        val participants = userRoomMapService.getParticipantsByRoomId(retrieveRequest.roomId)
        val requestUser = userService.getUserByToken(retrieveRequest.userToken)

        return RoomDto.from(room = room, isOwner = room.validateOwner(requestUser), participants = participants)
    }

    @Transactional(readOnly = true)
    fun getRoomById(roomId: Long): Room {
        return roomRepository.findById(roomId).orElseThrow { ResourceNotFoundException() }
    }

    @Transactional
    fun save(room: Room): Room {
        return roomRepository.save(room)
    }
}