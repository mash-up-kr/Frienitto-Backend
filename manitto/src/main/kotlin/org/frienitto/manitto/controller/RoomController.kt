package org.frienitto.manitto.controller

import org.frienitto.manitto.domain.Room
import org.frienitto.manitto.domain.RoomResponse
import org.frienitto.manitto.dto.Response
import org.frienitto.manitto.repository.RoomRepository
import org.frienitto.manitto.service.RoomService
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1")
class RoomController(private val roomRepository: RoomService) {

    @PostMapping("/register/room")
    fun createRoom(@Valid @RequestBody room: Room): Response<RoomResponse> {
        roomRepository
        val createRoom: Response<RoomResponse> = Response(201, "방 생성 완료", RoomResponse(
                null, room.title, room.expiresDate, "", room.participants))
        return createRoom
    }

    @GetMapping("join/room/{id}")
    fun joinRoom(@PathVariable("id") room_id: Long): Response<RoomResponse> {
        val room = roomRepository.getOne(room_id)
        return Response(20, "방 입장 성공", RoomResponse(
                null,
                room.title,
                room.expiresDate,
                room.url,
                room.participants
        ))
    }

    @GetMapping("room/{id}")
    fun getRoomDetail(@PathVariable("id") room_id: Long): Response<RoomResponse> {
        val room = roomRepository.getOne(room_id)
        return Response(20, "조회 성공", RoomResponse(
                null,
                room.title,
                room.expiresDate,
                room.url,
                room.participants
        ))
    }

    @GetMapping("room/list")
    fun getRoomList(): Response<List<Room>> {
        val room = roomRepository.findAll() // List<Room>
        return Response(20, "조회 성공", room)
    }

}