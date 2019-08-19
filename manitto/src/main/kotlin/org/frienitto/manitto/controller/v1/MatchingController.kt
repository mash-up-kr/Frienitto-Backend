package org.frienitto.manitto.controller.v1

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.frienitto.manitto.controller.swagger.model.MatchListInfo
import org.frienitto.manitto.controller.swagger.model.MatchResultInfo
import org.frienitto.manitto.dto.MatchRequest
import org.frienitto.manitto.dto.MatchResultDto
import org.frienitto.manitto.dto.MissionDto
import org.frienitto.manitto.dto.Response
import org.frienitto.manitto.exception.model.ErrorInfo
import org.frienitto.manitto.service.MissionService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

/**
 * 인증이 필요한 모든 Api에는 Controller에 Parameter로 @RequestHeader(name = "X-Authorization") token: String 가 필수 입니다. (파라미터명과 타입이 분명해야함 (token: String))
 */
@RestController
@RequestMapping("/api/v1")
@Api(value = "마니또 매칭 Controller", description = "매칭이 완료된 방의 매칭 상세 정보 API")
class MatchingController(private val missionService: MissionService) {

    @ApiOperation(value = "매칭 시작 API", response = MatchResultInfo::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "OK"),
        ApiResponse(code = 400, message = "매칭을 시작하려면 매칭 인원이 2명 이상이어야 합니다.", response = ErrorInfo::class),
        ApiResponse(code = 401, message = "방장이 아니거나 토큰 값이 유효하지 않습니다.", response = ErrorInfo::class),
        ApiResponse(code = 404, message = "NOT FOUND", response = ErrorInfo::class),
        ApiResponse(code = 5003, message = "해당 요청에서 미션 타입이 적절하지 않습니다.", response = ErrorInfo::class)
    ])
    @PostMapping("/matching")
    fun match(@RequestHeader(name = "X-Authorization") token: String, @RequestBody body: MatchRequest): Response<MatchResultDto> {
        return Response(HttpStatus.OK.value(), HttpStatus.OK.reasonPhrase, missionService.match(body))
    }

    @ApiOperation(value = "매칭된 방의 미션 정보 가져오는 API", response = MatchListInfo::class)
    @ApiResponses(value = [ApiResponse(code = 200, message = "get missions from room")])
    @GetMapping("/matching-info/room/{id}")
    fun getUserMatchingInfo(@RequestHeader(name = "X-Authorization") token: String, @PathVariable(name = "id") roomId: Long): Response<List<MissionDto>> {
        val result = missionService.getUserMissionsByRoomId(roomId)
        return Response(HttpStatus.OK.value(), HttpStatus.OK.reasonPhrase, result)
    }
}