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
        val room = roomRepository.save(Room.newRoom(owner, request.name, request.code, request.expiresDate))
        val participant = UserDto.of(owner.id!!, owner.username, owner.imageCode)

        return Response(HttpStatus.CREATED.value(), HttpStatus.CREATED.reasonPhrase, RoomDto.from(room, listOf(participant)))
    }

    @Transactional(readOnly = true)
    fun getById(id: Long): Room {
        return roomRepository.findById(id).orElseThrow { ResourceNotFoundException() }
    }

    fun getAllRoom(): Response<List<Room>> {
        val rooms = roomRepository.findAll()
        return Response(201, "방 생성 완료", rooms)
    }

    fun getRoomDetail(room_id:Long): Response<RoomDto>{
        val room = roomRepository.findById(room_id).get() // 없을 때 예외 처리 필요
        val participants = userRoomMapService.findUsersByRoomId(room_id)
        return Response(200, "조회 성공", RoomDto(
               room.id!!, room.title, room.expiresDate, room.url, participants))
    }
}