package org.frienitto.manitto.controller.v1

import io.swagger.annotations.*
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
@Api(value = "마니또 방 관련 Controller", description = "방 생성 & 입장 & 조회 API")
class RoomController(
        private val roomService: RoomService,
        private val userService: UserService,
        private val userRoomMapService: UserRoomMapService
) {
    @ApiOperation(value = "방 등록하기", response = Response::class)
    @ApiResponses(value = [ApiResponse(code = 201, message = "Successfully register room")])
    @PostMapping("/register/room")
    fun createRoom(@RequestHeader("X-Authorization") token: String, @Valid @RequestBody request: RoomRequest): Response<RoomDto> {
        val user = userService.getUserByToken(token)
        return roomService.createRoom(user, request)
    }

    @ApiOperation(value = "방 입장하기", response = Response::class)
    @ApiResponses(value = [ApiResponse(code = 200, message = "Successfully Join room")])
    @PostMapping("/join/room")
    fun joinRoom(@RequestHeader("X-Authorization") token: String, @RequestBody request: RoomJoinRequest): Response<RoomDto> {
        val user = userService.getUserByToken(token)
        return Response(HttpStatus.OK.value(), HttpStatus.OK.reasonPhrase, userRoomMapService.joinRoomByTitle(user, request))
    }

    @ApiOperation(value = "자세한 방 정보 가져오기", response = Response::class)
    @ApiResponses(value = [ApiResponse(code = 200, message = "Successfully get detailed room info")])
    @GetMapping("/room/{id}")
    fun getRoomDetail(@RequestHeader(name = "X-Authorization") token: String, @PathVariable("id") roomId: Long): Response<RoomDto> {
        return roomService.getRoomDetailById(roomId)
    }

    //TODO 페이징 처리 해야함
    @ApiOperation(value = "방 리스트 가져오기", response = Response::class)
    @ApiResponses(value = [ApiResponse(code = 200, message = "Successfully get room list")])
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