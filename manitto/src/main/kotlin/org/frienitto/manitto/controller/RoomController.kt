package org.frienitto.manitto.controller

import org.frienitto.manitto.domain.Room
import org.frienitto.manitto.domain.User
import org.frienitto.manitto.dto.*
import org.frienitto.manitto.service.RoomService
import org.frienitto.manitto.service.UserRoomMapService
import org.frienitto.manitto.service.UserService
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1")
class RoomController(private val roomService: RoomService, private val userService: UserService, private val userRoomMapService: UserRoomMapService) {

    @PostMapping("/register/room")
    fun createRoom(@RequestHeader("X-Authorization") token: String, @Valid @RequestBody request: RoomRequest): Response<RoomResponse> {
        val user = userService.getUserByToken(token)
        return roomService.createRoom(user, request)
    }

    @GetMapping("join/room/{id}")
    fun joinRoom(@RequestHeader("X-Authorization") token: String, @PathVariable("id") room_id: Long): Response<RoomResponse> {
        val user = userService.getUserByToken(token)
        return userRoomMapService.joinRoomsByRoomId(user, room_id)
    }

    @GetMapping("room/{id}")
    fun getRoomDetail(@PathVariable("id") room_id: Long): Response<RoomResponse> {
        return roomService.getRoomDetail(room_id)
    }

    @GetMapping("room/list")
    fun getRoomList(): Response<List<Room>> {
        return roomService.getAllRoom()
    }

    @PostMapping("matching")
    fun matchStart(@RequestBody body: MatchRequest): Response<MatchResponse> {
        return userRoomMapService.matchingStart(body)
    }
}