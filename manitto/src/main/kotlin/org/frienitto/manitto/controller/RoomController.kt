package org.frienitto.manitto.controller

import org.frienitto.manitto.domain.Room
import org.frienitto.manitto.dto.*
import org.frienitto.manitto.service.RoomService
import org.frienitto.manitto.service.UserRoomMapService
import org.frienitto.manitto.service.UserService
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1")
class RoomController(
        private val roomService: RoomService,
        private val userService: UserService,
        private val userRoomMapService: UserRoomMapService
) {

    @PostMapping("/register/room")
    fun createRoom(@RequestHeader("X-Authorization") token: String, @Valid @RequestBody request: RoomRequest): Response<RoomDto> {
        val user = userService.getUserByToken(token)
        return roomService.createRoom(user, request)
    }

    @GetMapping("join/room/{id}")
    fun joinRoom(@RequestHeader("X-Authorization") token: String, @PathVariable("id") roomId: Long): Response<RoomDto> {
        val user = userService.getUserByToken(token)
        return userRoomMapService.joinRoom(user, roomId)
    }

    @GetMapping("room/{id}")
    fun getRoomDetail(@PathVariable("id") roomId: Long): Response<RoomDto> {
        return roomService.getRoomDetailById(roomId)
    }

    //TODO 페이징 처리 해야함
    @GetMapping("room/list")
    fun getRoomList(): Response<List<Room>> {
        return roomService.getRoomList()
    }
}