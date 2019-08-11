package org.frienitto.manitto.controller.v1

import io.swagger.annotations.*
import org.frienitto.manitto.controller.swagger.model.RoomDetailInfo
import org.frienitto.manitto.controller.swagger.model.RoomListInfo
import org.frienitto.manitto.dto.*
import org.frienitto.manitto.exception.model.ErrorInfo
import org.frienitto.manitto.service.RoomService
import org.frienitto.manitto.service.UserRoomMapService
import org.frienitto.manitto.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
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
        private val userService: UserService
) {
    @ApiOperation(value = "방 등록하기", response = RoomDetailInfo::class)
    @ApiResponses(value = [ApiResponse(code = 201, message = "방 등록 완료", response = ErrorInfo::class),
        ApiResponse(code = 401, message = "인증 되지 않은 사용자입니다.", response = ErrorInfo::class),
        ApiResponse(code = 404, message = "등록된 방을 찾을 수 없습니다.", response = ErrorInfo::class),
        ApiResponse(code = 405, message = "방 생성 코드가 적합하지 않습니다.", response = ErrorInfo::class)
    ])
    @PostMapping("/register/room")
    fun createRoom(@RequestHeader("X-Authorization") token: String, @Valid @RequestBody createRequest: RoomCreateRequest): Response<RoomDto> {
        val user = userService.getUserByToken(token)

        return Response(HttpStatus.CREATED.value(), HttpStatus.CREATED.reasonPhrase, roomService.createRoom(user, createRequest))
    }

    @ApiOperation(value = "방 입장하기")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "OK", response = RoomDetailInfo::class),
        ApiResponse(code = 401, message = "인증 되지 않은 사용자입니다.", response = ErrorInfo::class),
        ApiResponse(code = 404, message = "요청 하신 방을 찾을 수가 없습니다.", response = ErrorInfo::class),
        ApiResponse(code = 405, message = "방 생성 코드가 적합하지 않습니다.", response = ErrorInfo::class),
        ApiResponse(code = 409, message = "입장 가능한 상태가 아닙니다.", response = ErrorInfo::class)])
    @PostMapping("/join/room")
    fun joinRoom(@RequestHeader("X-Authorization") token: String, @RequestBody request: RoomJoinRequest): Response<RoomDto> {
        val user = userService.getUserByToken(token)
        return Response(HttpStatus.OK.value(), HttpStatus.OK.reasonPhrase, roomService.joinRoomByTitle(user, request))
    }

    @ApiOperation(value = "자세한 방 정보 가져오기", response = RoomDetailInfo::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "OK", response = RoomDetailInfo::class),
        ApiResponse(code = 401, message = "인증 되지 않은 사용자입니다.", response = ErrorInfo::class),
        ApiResponse(code = 404, message = "요청 하신 방을 찾을 수가 없습니다.", response = ErrorInfo::class)])
    @GetMapping("/room/{id}")
    fun getRoomDetail(@RequestHeader(name = "X-Authorization") token: String, @PathVariable("id") roomId: Long): Response<RoomDto> {
        return Response(HttpStatus.OK.value(), HttpStatus.OK.reasonPhrase, roomService.getRoomInfoWithValidateOwner(RoomRetrieveRequest(token, roomId)))
    }
    @ApiOperation(value = "방 삭제", response = Nothing::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "OK", response = Nothing::class),
        ApiResponse(code = 401, message = "인증 되지 않은 사용자입니다.", response = ErrorInfo::class),
        ApiResponse(code = 404, message = "요청 하신 방을 찾을 수가 없습니다.", response = ErrorInfo::class)])
    @DeleteMapping("/exit/room")
    fun exitRoom(@RequestHeader(name = "X-Authorization") token: String, @RequestBody request: RoomExitRequest): ResponseEntity<Response<Unit>> {
        val user = userService.getUserByToken(token)
        roomService.deleteRoom(user, request.title)

        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(Response(HttpStatus.ACCEPTED.value(), HttpStatus.ACCEPTED.reasonPhrase))
    }

    //TODO 페이징 처리 해야함
    @ApiOperation(value = "방 리스트 가져오기", response = RoomListInfo::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "OK", response = RoomListInfo::class),
        ApiResponse(code = 401, message = "인증 되지 않은 사용자입니다.", response = ErrorInfo::class)])
    @GetMapping("/room/list")
    fun getRoomList(@RequestHeader(name = "X-Authorization") token: String): Response<List<RoomDto>> {
        userService.getUserByToken(token)
        val result = roomService.getRoomList().stream()
                .map { RoomDto.from(it) }
                .toList()

        return Response(HttpStatus.OK.value(), HttpStatus.OK.reasonPhrase, result)
    }
}