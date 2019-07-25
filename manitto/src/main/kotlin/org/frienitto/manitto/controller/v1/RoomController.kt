package org.frienitto.manitto.controller.v1

import org.frienitto.manitto.dto.Response
import org.frienitto.manitto.dto.RoomDto
import org.frienitto.manitto.dto.RoomJoinRequest
import org.frienitto.manitto.dto.RoomRequest
import org.frienitto.manitto.service.RoomService
import org.frienitto.manitto.service.UserRoomMapService
import org.frienitto.manitto.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.validation.Valid
import kotlin.streams.toList

/**
 * 인증이 필요한 모든 Api에는 Controller에 Parameter로 @RequestHeader(name = "X-Authorization") token: String 가 필수 입니다. (파라미터명과 타입이 분명해야함 (token: String))
 */
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

    @PostMapping("/join/room")
    fun joinRoom(@RequestHeader("X-Authorization") token: String, @RequestBody request: RoomJoinRequest): Response<RoomDto> {
        val user = userService.getUserByToken(token)
        return Response(HttpStatus.OK.value(), HttpStatus.OK.reasonPhrase, userRoomMapService.joinRoomByTitle(user, request))
    }

    @GetMapping("/room/{id}")
    fun getRoomDetail(@RequestHeader(name = "X-Authorization") token: String, @PathVariable("id") roomId: Long): Response<RoomDto> {
        return roomService.getRoomDetailById(roomId)
    }

    //TODO 페이징 처리 해야함
    @GetMapping("/room/list")
    fun getRoomList(@RequestHeader(name = "X-Authorization") token: String): Response<List<RoomDto>> {
        return Response(
                HttpStatus.OK.value(),
                HttpStatus.OK.reasonPhrase,
                roomService.getRoomList()
                        .stream()
                        .map { RoomDto.from(it) }
                        .toList()
        )
    }
}