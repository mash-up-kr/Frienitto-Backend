package org.frienitto.manitto.controller.v1

import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.frienitto.manitto.controller.swagger.model.RoomDetailInfo
import org.frienitto.manitto.dto.Response
import org.frienitto.manitto.dto.RoomDto
import org.frienitto.manitto.exception.model.ErrorInfo
import org.frienitto.manitto.service.UserRoomMapService
import org.frienitto.manitto.service.UserService
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * 인증이 필요한 모든 Api에는 Controller에 Parameter로 @RequestHeader(name = "X-Authorization") token: String 가 필수 입니다.
 * (파라미터명과 타입이 분명해야함 (token: String))
 */
@RestController
@RequestMapping(value = ["/api/v1"])
class UserController(private val userService: UserService) {

    @ApiOperation(value = "유저가 포함되어 있는 방 리스트 반환", response = RoomDetailInfo::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "OK", response = RoomDetailInfo::class),
        ApiResponse(code = 401, message = "인증 되지 않은 사용자입니다.", response = ErrorInfo::class)])
    @GetMapping("/user/room")
    fun getRoomListByToken(@RequestHeader(name = "X-Authorization") token: String): Response<List<RoomDto>> {
        val myRoomsOrEmpty = userService.getMyRoomsOrEmpty(token)
        return Response(HttpStatus.OK.value(), HttpStatus.OK.reasonPhrase, myRoomsOrEmpty)
    }
}
