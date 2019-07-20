package org.frienitto.manitto.service

import org.frienitto.manitto.domain.Room
import org.frienitto.manitto.domain.User
import org.frienitto.manitto.dto.*
import org.frienitto.manitto.exception.ResourceNotFoundException
import org.frienitto.manitto.repository.RoomRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RoomService(private val roomRepository: RoomRepository, private val userRoomMapService: UserRoomMapService) {

    @Transactional
    fun createRoom(owner: User, request: RoomRequest): Response<RoomDto> {
        val room = roomRepository.save(Room.newRoom(owner, request.title, request.code, request.expiresDate))
        userRoomMapService.joinRoomById(owner, room.id!!, room.code)

        return Response(HttpStatus.CREATED.value(), HttpStatus.CREATED.reasonPhrase, RoomDto.from(room))
    }

    //TODO 페이징 처리 해야함
    @Transactional(readOnly = true)
    fun getRoomList(): List<Room> {
        return roomRepository.findAll()
    }

    @Transactional(readOnly = true)
    fun getRoomDetailById(roomId: Long): Response<RoomDto>{
        val room = roomRepository.findById(roomId).orElseThrow { ResourceNotFoundException() }
        val participants = userRoomMapService.getParticipantsByRoomId(roomId)

        return Response(HttpStatus.OK.value(), HttpStatus.OK.reasonPhrase, RoomDto.from(room, participants))
    }
}